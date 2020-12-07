package br.com.ilia.digital.folhadeponto.persistence.model;

import java.io.Serializable;
import java.time.Duration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "alocacao_relatorio")
public class AlocacaoRelatorio implements Serializable, Comparable<AlocacaoRelatorio> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3346144101567066908L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	
	@Column(name="nome_projeto")
	private String nomeProjeto;
	
	@Column(name="tempo")
	private String tempo;
	
	@ManyToOne
    @JsonIgnore
	private Relatorio relatorio;

	public AlocacaoRelatorio() {
	}
	
	public AlocacaoRelatorio(String nomeProjeto, String tempo) {
		super();
		this.nomeProjeto = nomeProjeto;
		this.tempo = tempo;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}

	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		this.tempo = tempo;
	}	
	
	public Relatorio getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(Relatorio relatorio) {
		this.relatorio = relatorio;
	}
	
	@Override
	public int compareTo(AlocacaoRelatorio o) {
		return Duration.parse(this.tempo).compareTo(Duration.parse(o.getTempo()));
	}
}
