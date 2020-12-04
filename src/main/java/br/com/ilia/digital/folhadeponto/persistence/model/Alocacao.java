package br.com.ilia.digital.folhadeponto.persistence.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity(name="alocacao")
public class Alocacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5334395868574242925L;

	@Id
	private Long id;
	
	private String dia;
	
	private String tempo;
	
	@Column(name="nome_projeto")
	private String nomeProjeto;
	
	@ManyToMany(mappedBy = "alocacoes")
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

	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		this.tempo = tempo;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}
	
}
