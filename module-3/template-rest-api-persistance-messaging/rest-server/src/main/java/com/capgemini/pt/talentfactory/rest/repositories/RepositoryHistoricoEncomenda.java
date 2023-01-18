package com.capgemini.pt.talentfactory.rest.repositories;

import com.capgemini.pt.talentfactory.rest.models.HistoricoEncomenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryHistoricoEncomenda extends JpaRepository<HistoricoEncomenda, Long> {
    Optional<HistoricoEncomenda> findEncomendaById(Long id);
}
