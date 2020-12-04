package br.com.ilia.digital.folhadeponto.persistence.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="mensagem")
public class Mensagem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 412862543888084547L;

	@Id
	private Long id;
	
	private String mensagem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}
