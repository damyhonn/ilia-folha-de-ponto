package br.com.ilia.digital.folhadeponto.persistence.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.ilia.digital.folhadeponto.service.util.formatter.JsonDateSerializer;

@Entity(name="registro")
public class Registro implements Serializable, Comparable<Registro> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8216181404964299507L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	
	private String dia;
	
	@ElementCollection
	@CollectionTable(name = "registro_horarios", joinColumns = @JoinColumn(name = "registro_id"))
	@Column(name = "horario")
	@JsonSerialize(contentUsing = JsonDateSerializer.class)
	@OrderBy("id")
	private SortedSet<Date> horarios = new TreeSet<>();
	
	@ManyToMany(mappedBy = "registros")
	@JsonIgnore
	@OrderBy("id")
	private SortedSet<Relatorio> relatorios;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public SortedSet<Date> getHorarios() {
		return horarios;
	}

	public void setHorarios(SortedSet<Date> horarios) {
		this.horarios = horarios;
	}

	public SortedSet<Relatorio> getRelatorios() {
		return relatorios;
	}

	public void setRelatorios(SortedSet<Relatorio> relatorios) {
		this.relatorios = relatorios;
	}

	@Override
	public int compareTo(Registro o) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date data1 = formato.parse(this.dia);
			Date data2 = formato.parse(o.getDia());
			return data1.compareTo(data2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
}
