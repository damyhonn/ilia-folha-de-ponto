package br.com.ilia.digital.folhadeponto.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ilia.digital.folhadeponto.persistence.model.Alocacao;

public interface AlocacaoRepository extends JpaRepository<Alocacao, Long> {
	Optional<Alocacao> findById(Long id);
}
