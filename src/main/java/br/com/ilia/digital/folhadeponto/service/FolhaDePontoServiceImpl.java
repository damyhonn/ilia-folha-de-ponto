package br.com.ilia.digital.folhadeponto.service;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.ilia.digital.folhadeponto.persistence.model.Alocacao;
import br.com.ilia.digital.folhadeponto.persistence.model.Momento;
import br.com.ilia.digital.folhadeponto.persistence.model.Registro;
import br.com.ilia.digital.folhadeponto.persistence.repository.AlocacaoRepository;
import br.com.ilia.digital.folhadeponto.persistence.repository.MomentoRepository;
import br.com.ilia.digital.folhadeponto.persistence.repository.RegistroRepository;
import br.com.ilia.digital.folhadeponto.service.exception.FolhaDePontoException;
import br.com.ilia.digital.folhadeponto.service.util.MensagensUtil;

@Service
public class FolhaDePontoServiceImpl implements FolhaDePontoService {

	private final int NUMERO_MAXIMO_BATIDAS_DIARIAS = 3;
	private final int SEGUNDA_BATIDA = 2;
	private final int HORAS_EM_SEGUNDOS = 3600;
	
	@Autowired
	private AlocacaoRepository alocacaoRepository;
	
	@Autowired
	private MomentoRepository momentoRepository;
	
	@Autowired
	private RegistroRepository registroRepository;
	
	@Autowired
	private MensagensUtil mensagensUtil;
	
	@Override
	public Alocacao getAlocacao(Long id) {
		return alocacaoRepository.getOne(id);
	}

	@Override
	public String retornoTeste() {
		return "Tá funcionando!";
	}

	@Override
	public Registro registraBatida(Momento momento) throws FolhaDePontoException, DateTimeException {
		
		validaMomento(momento);
		
		LocalDateTime dateTimeBatida = LocalDateTime.parse(momento.getDataHora() + "-03:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		DateTimeFormatter formatoYYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
		Date horarioBatida = Date.from(dateTimeBatida.atZone(ZoneId.systemDefault()).toInstant());
		
		Registro registro = registroRepository.findByDia(formatoYYYY_MM_DD.format(dateTimeBatida));
		
		if (null == registro) { //Primeiro registro para o dia informado
			registro = new Registro();
			registro.setDia(formatoYYYY_MM_DD.format(dateTimeBatida));
			Set<Date> horarios = new TreeSet<Date>();
			horarios.add(horarioBatida);
			registro.setHorarios(horarios);
			validaRegistro(registro, dateTimeBatida);
			momentoRepository.save(momento);
			registroRepository.save(registro);
		} else {
			validaRegistro(registro, dateTimeBatida);
			Set<Date> horarios = new TreeSet<Date>();
			horarios.addAll(registro.getHorarios());
			horarios.add(horarioBatida);
			registro.setHorarios(horarios);
			momentoRepository.save(momento);
			registroRepository.save(registro);
		}
		
		return registro;
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
		if (registro.getHorarios().size() > this.NUMERO_MAXIMO_BATIDAS_DIARIAS) {
			throw new FolhaDePontoException(mensagensUtil.getProperty("quatro.horarios.por.dia"), HttpStatus.FORBIDDEN);
		}
		//Valida se houve mais de 1 hora de almoço
		if (registro.getHorarios().size() == this.SEGUNDA_BATIDA) {
			Calendar saidaAlmoco = Calendar.getInstance();
			saidaAlmoco.setTime(registro.getHorarios().stream().findFirst().orElse(null)); //Retorna o segundo horário batido (ultimo elemento inserido no conjunto)
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
