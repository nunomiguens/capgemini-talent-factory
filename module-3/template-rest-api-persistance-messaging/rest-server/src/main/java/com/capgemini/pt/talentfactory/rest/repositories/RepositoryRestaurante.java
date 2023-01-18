package com.capgemini.pt.talentfactory.rest.repositories;

import com.capgemini.pt.talentfactory.rest.models.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryRestaurante extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findByNome(String nome);
}
