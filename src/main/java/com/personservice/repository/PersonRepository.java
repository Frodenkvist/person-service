package com.personservice.repository;

import com.personservice.common.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, String> {
    Optional<Person> findPersonByPersonnummer(String personnummer);
}
