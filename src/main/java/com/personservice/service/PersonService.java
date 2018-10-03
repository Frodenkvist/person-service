package com.personservice.service;

import com.personservice.common.exception.PersonMissingException;
import com.personservice.common.model.Person;
import com.personservice.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public Person getPerson(String personNumber) throws PersonMissingException {
        return personRepository.findPersonByPersonnummer(personNumber).orElseThrow(() ->
                new PersonMissingException("Unable to find person with person number: " + personNumber, personNumber));
    }

    public List<Person> getPersons() {
        return personRepository.findAll();
    }
}
