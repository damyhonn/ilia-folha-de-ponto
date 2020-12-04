package br.com.ilia.digital.folhadeponto.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ilia.digital.folhadeponto.persistence.model.Momento;

public interface MomentoRepository extends JpaRepository<Momento, Long> {
	
}
