package br.com.ilia.digital.folhadeponto.service;

import br.com.ilia.digital.folhadeponto.persistence.model.Alocacao;
import br.com.ilia.digital.folhadeponto.persistence.model.Momento;
import br.com.ilia.digital.folhadeponto.persistence.model.Registro;
import br.com.ilia.digital.folhadeponto.persistence.model.Relatorio;

public interface FolhaDePontoService {
	Registro insereBatida(Momento momento);
	Alocacao insereAlocacao(Alocacao alocacao);
	Relatorio geraRelatorioMensal(String mes);
}
