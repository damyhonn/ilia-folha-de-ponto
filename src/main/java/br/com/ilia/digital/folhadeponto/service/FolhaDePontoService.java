package br.com.ilia.digital.folhadeponto.service;

import java.util.List;

import br.com.ilia.digital.folhadeponto.persistence.model.Alocacao;
import br.com.ilia.digital.folhadeponto.persistence.model.Momento;
import br.com.ilia.digital.folhadeponto.persistence.model.Registro;
import br.com.ilia.digital.folhadeponto.persistence.model.Relatorio;

public interface FolhaDePontoService {
	Registro registraBatida(Momento momento);
	List<Momento> getMomentos();
	List<Registro> getRegistros();
	Alocacao registraAlocacao(Alocacao alocacao);
	Relatorio getRelatorio(String mes);
}
