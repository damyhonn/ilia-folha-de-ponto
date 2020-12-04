package br.com.ilia.digital.folhadeponto.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ilia.digital.folhadeponto.persistence.model.Registro;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

}
