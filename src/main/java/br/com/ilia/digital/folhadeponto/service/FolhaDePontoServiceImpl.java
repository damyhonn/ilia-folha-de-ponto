package br.com.ilia.digital.folhadeponto.service;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.ilia.digital.folhadeponto.persistence.model.Alocacao;
import br.com.ilia.digital.folhadeponto.persistence.model.AlocacaoRelatorio;
import br.com.ilia.digital.folhadeponto.persistence.model.Momento;
import br.com.ilia.digital.folhadeponto.persistence.model.Registro;
import br.com.ilia.digital.folhadeponto.persistence.model.Relatorio;
import br.com.ilia.digital.folhadeponto.persistence.repository.AlocacaoRepository;
import br.com.ilia.digital.folhadeponto.persistence.repository.MomentoRepository;
import br.com.ilia.digital.folhadeponto.persistence.repository.RegistroRepository;
import br.com.ilia.digital.folhadeponto.persistence.repository.RelatorioRepository;
import br.com.ilia.digital.folhadeponto.service.exception.FolhaDePontoException;
import br.com.ilia.digital.folhadeponto.service.util.MensagensUtil;
import br.com.ilia.digital.folhadeponto.service.util.TimeUtil;

@Service
public class FolhaDePontoServiceImpl implements FolhaDePontoService {

	private final int NUMERO_MAXIMO_BATIDAS_DIARIAS = 4;
	private final int SEGUNDA_BATIDA = 2;
	private final int HORAS_EM_SEGUNDOS = 3600;
	private final String GMT_BRAZIL = "-03:00";
	private final String CARGA_HORARIA_DIARIA = "PT8H";
	private final int CARGA_HORARIA_DIARIA_INT = 8;
	
	@Autowired
	private AlocacaoRepository alocacaoRepository;
	
	@Autowired
	private MomentoRepository momentoRepository;
	
	@Autowired
	private RegistroRepository registroRepository;
	
	@Autowired
	private RelatorioRepository relatorioRepository;
	
	@Autowired
	private MensagensUtil mensagensUtil;
	
	@Autowired
	private TimeUtil timeUtil;

	@Override
	public Registro registraBatida(Momento momento) throws FolhaDePontoException, DateTimeException {
		
		validaMomento(momento);
		
		LocalDateTime dateTimeBatida = LocalDateTime.parse(momento.getDataHora() + this.GMT_BRAZIL, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		DateTimeFormatter formatoYYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
		Date horarioBatida = Date.from(dateTimeBatida.atZone(ZoneId.systemDefault()).toInstant());
		
		Registro registro = registroRepository.findByDia(formatoYYYY_MM_DD.format(dateTimeBatida));
		
		if (null == registro) { //Primeiro registro para o dia informado
			registro = new Registro();
			registro.setDia(formatoYYYY_MM_DD.format(dateTimeBatida));
			SortedSet<Date> horarios = new TreeSet<Date>();
			horarios.add(horarioBatida);
			registro.setHorarios(horarios);
			validaRegistro(registro, dateTimeBatida);
			momentoRepository.save(momento);
			registroRepository.save(registro);
		} else {
			validaRegistro(registro, dateTimeBatida);
			SortedSet<Date> horarios = new TreeSet<Date>();
			horarios.addAll(registro.getHorarios());
			horarios.add(horarioBatida);
			registro.setHorarios(horarios);
			momentoRepository.save(momento);
			registroRepository.save(registro);
			atualizaRegistroRelatorio(registro);
		}
		
		return registro;
	}
	
	@Override
	public Alocacao registraAlocacao(Alocacao alocacao) {
		validaAlocacao(alocacao);
		atualizaAlocacaoRelatorio(alocacao);
		return alocacaoRepository.save(alocacao);
	}
	
	@Override
	public Relatorio getRelatorio(String mes) {
		Relatorio relatorio = relatorioRepository.findByMes(mes);
		if (null == relatorio) {
			throw new FolhaDePontoException(mensagensUtil.getProperty("relatorio.nao.encontrado"), HttpStatus.NOT_FOUND);
		}
		return relatorio;
	}
	
	private void atualizaAlocacaoRelatorio(Alocacao alocacao) {
		YearMonth ym = YearMonth.parse(alocacao.getDia().substring(0, 7));
		Relatorio relatorio = relatorioRepository.findByMes(ym.toString());
		Duration horasAlocadas = Duration.parse(alocacao.getTempo());
		boolean alocacaoAtualizada = false;
		if (null != relatorio) {
			if (null != relatorio.getAlocacoes() && 
					relatorio.getAlocacoes().isEmpty()) { //primeira alocação do relatório
				Set<AlocacaoRelatorio> alocacoes = new TreeSet<>();
				AlocacaoRelatorio alocacaoRelatorio = new AlocacaoRelatorio();
				alocacaoRelatorio.setNomeProjeto(alocacao.getNomeProjeto());
				alocacaoRelatorio.setTempo(alocacao.getTempo());
				alocacoes.add(alocacaoRelatorio);
				relatorio.setAlocacoes(alocacoes);
			} else {
				Set<AlocacaoRelatorio> alocacoes = relatorio.getAlocacoes();
				for (Iterator<AlocacaoRelatorio> iterator = alocacoes.iterator(); iterator.hasNext();) {
					AlocacaoRelatorio aloc = (AlocacaoRelatorio) iterator.next();
					if (alocacao.getNomeProjeto().equalsIgnoreCase(aloc.getNomeProjeto())) {
						Duration duracaoAtual = Duration.parse(aloc.getTempo());
						Duration duracaoAtualizada = duracaoAtual.plus(horasAlocadas);
						aloc.setTempo(duracaoAtualizada.toString());
						alocacaoAtualizada = true;
					}
				}
				if (!alocacaoAtualizada) {
					relatorio.getAlocacoes().add(new AlocacaoRelatorio(alocacao.getNomeProjeto(), alocacao.getTempo()));
				}
			}
			relatorioRepository.save(relatorio);
		}
	}
	
	private void atualizaRegistroRelatorio(Registro registro) {
		/*
		 * Atualiza o relatório diariamente 
		 * apenas se o registro constar as 4 batidas do dia
		 */
		if (registro.getHorarios().size() == this.NUMERO_MAXIMO_BATIDAS_DIARIAS) {
			YearMonth ym = YearMonth.parse(registro.getDia().substring(0, 7));
			Relatorio relatorio = relatorioRepository.findByMes(ym.toString());
			int qtdDiasUteis = timeUtil.diasUteisDoMes(ym);
			Duration horasDevidasMes = Duration.ofHours(this.CARGA_HORARIA_DIARIA_INT * qtdDiasUteis); // Baseado na quantidade de dias úteis do mês em questão
			Duration horasTrabalhadas = timeUtil.calculaHorasTrabalhadas(registro.getHorarios());
			Duration horasExcedentes = Duration.ZERO;
			if (horasTrabalhadas.compareTo(Duration.parse(this.CARGA_HORARIA_DIARIA)) > 0) {
				horasExcedentes = horasTrabalhadas.minus(Duration.parse(CARGA_HORARIA_DIARIA));
				horasTrabalhadas = Duration.parse(this.CARGA_HORARIA_DIARIA);
			}
			if (null == relatorio) { //Primeiro registro do mês
				relatorio = new Relatorio();
				relatorio.setMes(ym.toString());
				relatorio.setHorasTrabalhadas(horasTrabalhadas.truncatedTo(ChronoUnit.HOURS).toString());
				relatorio.setHorasExcedentes(horasExcedentes.truncatedTo(ChronoUnit.HOURS).toString());
				horasDevidasMes = horasDevidasMes.minus(horasTrabalhadas).minus(horasExcedentes);
				relatorio.setHorasDevidas(horasDevidasMes.truncatedTo(ChronoUnit.HOURS).toString()); 
				Set<Registro> registros = new TreeSet<>();
				registros.add(registro);
				relatorio.setRegistros(registros);
				relatorioRepository.save(relatorio);
			} else {
				horasTrabalhadas = Duration.parse(relatorio.getHorasTrabalhadas())
						.plus(horasTrabalhadas);
				horasExcedentes = Duration.parse(relatorio.getHorasExcedentes())
						.plus(horasExcedentes);
				relatorio.setHorasTrabalhadas(horasTrabalhadas.toString());
				relatorio.setHorasExcedentes(horasExcedentes.toString());
				relatorio.setHorasDevidas(Duration.parse(relatorio.getHorasDevidas())
						.minus(horasTrabalhadas).truncatedTo(ChronoUnit.HOURS).toString());
				relatorio.getRegistros().add(registro);
				relatorioRepository.save(relatorio);
			}
		}
	}
	
	private void validaAlocacao(Alocacao alocacao) {
		Registro registro = registroRepository.findByDia(alocacao.getDia());
		//Valida se o dia informado possui registro de ponto
		if (null == registro) {
			throw new FolhaDePontoException(mensagensUtil.getProperty("nao.existe.registro.para.dia.informado"), HttpStatus.BAD_REQUEST);
		}
		//Valida se o registro de pontos do dia está completo
		if (registro.getHorarios().size() < this.NUMERO_MAXIMO_BATIDAS_DIARIAS) {
			throw new FolhaDePontoException(mensagensUtil.getProperty("registro.de.ponto.dia.incompleto"), HttpStatus.BAD_REQUEST);
		}
		//Valida se o tempo a ser alocado é maior que o trabalhado no dia
		Duration horasTrabalhadas = timeUtil.calculaHorasTrabalhadas(registro.getHorarios());
		if (Duration.parse(alocacao.getTempo()).compareTo(horasTrabalhadas) > 0) {
			throw new FolhaDePontoException(mensagensUtil.getProperty("nao.pode.alocar.tempo.maior.trabalhado"), HttpStatus.BAD_REQUEST);
		}
	}

	private void validaMomento(Momento momento) {
		//Valida se o horário já foi registrado
		if (null != momentoRepository.findByDataHora(momento.getDataHora())) {
			throw new FolhaDePontoException(mensagensUtil.getProperty("horario.ja.registrado"), HttpStatus.CONFLICT);
		}
		//Valida se o campo obrigatório foi informado
		if (null == momento.getDataHora()) {
			throw new FolhaDePontoException(mensagensUtil.getProperty("campo.obrigatorio.nao.informado"), HttpStatus.BAD_REQUEST);
		}
	}
	
	private void validaRegistro(Registro registro, LocalDateTime dateTimeBatida) {
		Date horarioBatida = Date.from(dateTimeBatida.atZone(ZoneId.systemDefault()).toInstant());
		//Valida se o número de batidas foi excedido
		if (registro.getHorarios().size() >= this.NUMERO_MAXIMO_BATIDAS_DIARIAS) {
			throw new FolhaDePontoException(mensagensUtil.getProperty("quatro.horarios.por.dia"), HttpStatus.FORBIDDEN);
		}
		//Valida se houve mais de 1 hora de almoço
		if (registro.getHorarios().size() == this.SEGUNDA_BATIDA) {
			Calendar saidaAlmoco = Calendar.getInstance();
			saidaAlmoco.setTime(registro.getHorarios().stream().skip(1).findFirst().orElse(null)); //Retorna o segundo horário batido (ultimo elemento inserido no conjunto)
			Date saidaAlmocoDate = saidaAlmoco.getTime();
			Duration duracao = Duration.between(saidaAlmocoDate.toInstant(), horarioBatida.toInstant());
			
			if (duracao.getSeconds() < this.HORAS_EM_SEGUNDOS) {
				throw new FolhaDePontoException(mensagensUtil.getProperty("uma.hora.almoco"), HttpStatus.FORBIDDEN);
			}
		}
		
		//Valida se a batida é em um sábado ou domingo
		if (dateTimeBatida.getDayOfWeek().equals(DayOfWeek.SATURDAY) || 
				dateTimeBatida.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			throw new FolhaDePontoException(mensagensUtil.getProperty("sabado.domingo.nao.permitidos"), HttpStatus.FORBIDDEN);
		}
	}

	@Override
	public List<Registro> getRegistros() {
		return registroRepository.findAll();
	}
	
	@Override
	public List<Momento> getMomentos() {
		return momentoRepository.findAll();
	}
}
