package br.com.ilia.digital.folhadeponto.persistence.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.ilia.digital.folhadeponto.service.util.formatter.JsonDateSerializer;

@Entity(name="registro")
public class Registro implements Serializable {

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
	private Set<Date> horarios = new TreeSet<>();
	
	@ManyToMany(mappedBy = "registros")
	@JsonIgnore
	private Set<Relatorio> relatorios;

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

	public Set<Date> getHorarios() {
		return horarios;
	}

	public void setHorarios(Set<Date> horarios) {
		this.horarios = horarios;
	}

	public Set<Relatorio> getRelatorios() {
		return relatorios;
	}

	public void setRelatorios(Set<Relatorio> relatorios) {
		this.relatorios = relatorios;
	}
	
}
