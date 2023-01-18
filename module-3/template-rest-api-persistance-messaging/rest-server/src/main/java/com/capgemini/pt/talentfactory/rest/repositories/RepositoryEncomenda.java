package com.capgemini.pt.talentfactory.rest.repositories;

import com.capgemini.pt.talentfactory.rest.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryEncomenda extends JpaRepository<Encomenda, Long> {

}