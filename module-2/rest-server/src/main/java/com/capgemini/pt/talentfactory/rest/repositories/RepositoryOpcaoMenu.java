package com.capgemini.pt.talentfactory.rest.repositories;

import com.capgemini.pt.talentfactory.rest.models.OpcaoMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryOpcaoMenu extends JpaRepository<OpcaoMenu, Long> {

}
