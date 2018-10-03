package com.personservice.service;

import com.personservice.common.exception.PersonMissingException;
import com.personservice.common.model.Person;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PersonService {
    private static List<Person> persons = Arrays.asList(
            new Person("19880301-1234", "John Doe"),
            new Person("19900801-1234", "Jane Doe")
    );

    public Person getPerson(String personNumber) throws PersonMissingException {
        return persons
                .stream()
                .filter(person -> person.getPersonNummer().equals(personNumber))
                .findFirst()
                .orElseThrow(() -> new PersonMissingException("Unable to find person with person number: " + personNumber, personNumber));
    }
}
