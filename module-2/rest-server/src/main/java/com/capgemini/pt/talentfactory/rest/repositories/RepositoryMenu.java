package com.capgemini.pt.talentfactory.rest.repositories;

import com.capgemini.pt.talentfactory.rest.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryMenu extends JpaRepository<Menu, Long> {
    List<Menu> findByNome(String nome);
}
