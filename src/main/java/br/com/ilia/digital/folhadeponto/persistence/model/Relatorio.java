package br.com.ilia.digital.folhadeponto.persistence.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity(name="relatorio")
public class Relatorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4582907938862697086L;

	@Id
	private Long id;
	
	private String mes;
	
	@Column(name="horas_trabalhadas")
	private String horasTrabalhadas;
	
	@Column(name="horas_excedentes")
	private String horasExcedentes;
	
	@Column(name="horas_devidas")
	private String horasDevidas;
	
	@ManyToMany
	@JoinTable(name="relatorio_registro",
	joinColumns = {@JoinColumn(name = "relatorio_id")},
	inverseJoinColumns = {@JoinColumn(name = "registro_id")})
	private Set<Registro> registros;
	
	@ManyToMany
	@JoinTable(name="relatorio_alocacao",
	joinColumns = {@JoinColumn(name = "relatorio_id")},
	inverseJoinColumns = {@JoinColumn(name = "alocacao_id")})
	private Set<Alocacao> alocacoes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getHorasTrabalhadas() {
		return horasTrabalhadas;
	}

	public void setHorasTrabalhadas(String horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
	}

	public String getHorasExcedentes() {
		return horasExcedentes;
	}

	public void setHorasExcedentes(String horasExcedentes) {
		this.horasExcedentes = horasExcedentes;
	}

	public String getHorasDevidas() {
		return horasDevidas;
	}

	public void setHorasDevidas(String horasDevidas) {
		this.horasDevidas = horasDevidas;
	}

	public Set<Registro> getRegistros() {
		return registros;
	}

	public void setRegistros(Set<Registro> registros) {
		this.registros = registros;
	}

	public Set<Alocacao> getAlocacoes() {
		return alocacoes;
	}

	public void setAlocacoes(Set<Alocacao> alocacoes) {
		this.alocacoes = alocacoes;
	}
}
