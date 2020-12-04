package br.com.ilia.digital.folhadeponto.service;

import java.util.List;

import br.com.ilia.digital.folhadeponto.persistence.model.Alocacao;
import br.com.ilia.digital.folhadeponto.persistence.model.Momento;
import br.com.ilia.digital.folhadeponto.persistence.model.Registro;

public interface FolhaDePontoService {

	Alocacao getAlocacao(Long id);
	String retornoTeste();
	Registro registraBatida(Momento momento);
	List<Momento> getMomentos();
	List<Registro> getRegistros();
}
