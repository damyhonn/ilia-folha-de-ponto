package br.com.ilia.digital.folhadeponto.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ilia.digital.folhadeponto.persistence.model.Relatorio;

public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {
	Relatorio findByMes(String mes);
}
