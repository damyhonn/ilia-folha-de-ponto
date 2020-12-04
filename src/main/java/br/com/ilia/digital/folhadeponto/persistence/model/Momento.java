package br.com.ilia.digital.folhadeponto.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="momento")
public class Momento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1034466536707530072L;

	@Id
	private Long id;
	
	@Column(name="data_hora")
	private String dataHora;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDataHora() {
		return dataHora;
	}

	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}

}