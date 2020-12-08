package br.com.ilia.digital.folhadeponto.service;

import br.com.ilia.digital.folhadeponto.persistence.model.Alocacao;
import br.com.ilia.digital.folhadeponto.persistence.model.Momento;
import br.com.ilia.digital.folhadeponto.persistence.model.Registro;
import br.com.ilia.digital.folhadeponto.persistence.model.Relatorio;

public interface FolhaDePontoService {
	Registro registraBatida(Momento momento);
	Alocacao registraAlocacao(Alocacao alocacao);
	Relatorio getRelatorio(String mes);
}
