package br.com.ilia.digital.folhadeponto.persistence.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

@Entity(name="registro")
public class Registro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8216181404964299507L;

	@Id
	private Long id;
	
	private String dia;
	
	@ElementCollection
	@CollectionTable(name = "registro_horarios", joinColumns = @JoinColumn(name = "registro_id"))
	@Column(name = "horario")
	private Set<String> horarios = new HashSet<>();
	
	@ManyToMany(mappedBy = "registros")
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

	public Set<String> getHorarios() {
		return horarios;
	}

	public void setHorarios(Set<String> horarios) {
		this.horarios = horarios;
	}

	public Set<Relatorio> getRelatorios() {
		return relatorios;
	}

	public void setRelatorios(Set<Relatorio> relatorios) {
		this.relatorios = relatorios;
	}
	
}
