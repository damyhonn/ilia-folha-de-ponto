package br.com.ilia.digital.folhadeponto.service.util;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeUtil {

	Date entrada, saidaAlmoco, retornoAlmoco, saida = new Date();

	public Duration calculaHorasTrabalhadas(Set<Date> horario) {
		int idx = 0;
		for (Iterator<Date> iterator = horario.iterator(); iterator.hasNext();) {
			Date batida = (Date) iterator.next();
			
			switch (idx) {
			case 0:
				this.entrada = batida;
				idx++;
				break;
			case 1:
				this.saidaAlmoco = batida;
				idx++;
				break;
			case 2:
				this.retornoAlmoco = batida;
				idx++;
				break;
			case 3:
				this.saida = batida;
				idx++;
				break;
			default:
				break;
			}
		}
		Duration horasTrabalhadasManha = Duration.between(this.entrada.toInstant(), this.saidaAlmoco.toInstant());
		Duration horasTrabalhadasTarde = Duration.between(this.retornoAlmoco.toInstant(), this.saida.toInstant());

		return horasTrabalhadasManha.plus(horasTrabalhadasTarde);
	}

	public int diasUteisDoMes(YearMonth anoMes) {
		List<LocalDate> listaDosDiasUteisDoMes = new ArrayList<LocalDate>();
		Set<LocalDate> feriados = new HashSet<>();
		feriados.addAll(getFeriadosFixos(anoMes.getYear()));
		feriados.addAll(getFeriadosMoveis(anoMes.getYear()));
		for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
			LocalDate data = anoMes.atDay(dia);

			if (data.getDayOfWeek() != DayOfWeek.SATURDAY
					&& data.getDayOfWeek() != DayOfWeek.SUNDAY
					&& !feriados.contains(data)) {
				listaDosDiasUteisDoMes.add(data);
			}
		}
		return listaDosDiasUteisDoMes.size();
	}
	
	/*
	 * feriados que acontecem todo ano na mesma data, gerar lista para o ano específico
	 */
	public Set<LocalDate> getFeriadosFixos(int year) {
	    Set<LocalDate> dates = new HashSet<>();

	    // Confraternização Universal;
	    if (!isSabadoOuDomingo(LocalDate.of(year, 1, 1))) {
	    	dates.add(LocalDate.of(year, 1, 1));
	    }
	    // Tiradentes
	    if (!isSabadoOuDomingo(LocalDate.of(year, 4, 21))) {
	    	dates.add(LocalDate.of(year, 4, 21));
	    }
	    // Dia do Trabalhador
	    if (!isSabadoOuDomingo(LocalDate.of(year, 5, 1))) {
	    	dates.add(LocalDate.of(year, 5, 1));
	    }
	    // 7 de setembro
	    if (!isSabadoOuDomingo(LocalDate.of(year, 9, 7))) {
	    	dates.add(LocalDate.of(year, 9, 7));
	    }
	    // Nossa Senhora Aparecida
	    if (!isSabadoOuDomingo(LocalDate.of(year, 10, 12))) {
	    	dates.add(LocalDate.of(year, 10, 12));
	    }
	    // Finados
	    if (!isSabadoOuDomingo(LocalDate.of(year, 11, 2))) {
	    	dates.add(LocalDate.of(year, 11, 2));
	    }
	    // Proclamação da República
	    if (!isSabadoOuDomingo(LocalDate.of(year, 11, 15))) {
	    	dates.add(LocalDate.of(year, 11, 15));
	    }
	    // natal
	    if (!isSabadoOuDomingo(LocalDate.of(year, 12, 25))) {
	    	dates.add(LocalDate.of(year, 12, 25));
	    }
	    return dates;
	}
	
	/* 
	 * Calcula páscoa, carnaval, corpus christi e sexta-feira santa
	 */
	public Set<LocalDate> getFeriadosMoveis(int year) {
	    Set<LocalDate> dates = new HashSet<>();

	    LocalDate pascoa;
	    LocalDate carnaval;
	    LocalDate corpusChristi;
	    LocalDate sextaFeiraSanta;

	    int a = year % 19;
	    int b = year / 100;
	    int c = year % 100;
	    int d = b / 4;
	    int e = b % 4;
	    int f = (b + 8) / 25;
	    int g = (b - f + 1) / 3;
	    int h = (19 * a + b - d - g + 15) % 30;
	    int i = c / 4;
	    int k = c % 4;
	    int l = (32 + 2 * e + 2 * i - h - k) % 7;
	    int m = (a + 11 * h + 22 * l) / 451;
	    int month = (h + l - 7 * m + 114) / 31;
	    int day = ((h + l - 7 * m + 114) % 31) + 1;

	    pascoa = LocalDate.of(year, month, day);

	    // Carnaval 47 dias antes da pascoa (sempre cai na terça)
	    carnaval = pascoa.minusDays(47);

	    // CorpusChristi 60 dias apos a pascoa
	    corpusChristi = pascoa.plusDays(60);

	    sextaFeiraSanta = pascoa.minusDays(2);

	    // páscoa cai sempre no domingo, entao não precisaria adicionar como feriado
	    // dates.add(pascoa);

	    // carnaval: adicionar um dia antes e depois (emenda de segunda e quarta-feira de cinzas)
	    dates.add(carnaval);
	    dates.add(carnaval.minusDays(1)); // emenda a segunda-feira
//	    dates.add(carnaval.plusDays(1)); // quarta-feira de cinzas

	    // corpus christi, emendar (adicionar a sexta)
	    if (!isSabadoOuDomingo(corpusChristi)) {
	    	dates.add(corpusChristi);
	    }
	    
	    dates.add(sextaFeiraSanta);

	    return dates;
	}
	
	private boolean isSabadoOuDomingo(LocalDate data) {
		if (data.getDayOfWeek() == DayOfWeek.SATURDAY ||
				data.getDayOfWeek() == DayOfWeek.SUNDAY) {
			return true;
		}
		
		return false;
	}
}
