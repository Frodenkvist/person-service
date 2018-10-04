package com.personservice.service;

import com.common.rabbitmq.RabbitMQRouting;
import com.personservice.common.exception.PersonMissingException;
import com.personservice.common.model.Person;
import com.personservice.repository.PersonRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Person getPerson(String personNumber) throws PersonMissingException {
        return personRepository.findPersonByPersonnummer(personNumber).orElseThrow(() ->
                new PersonMissingException("Unable to find person with person number: " + personNumber, personNumber));
    }

    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    public Person createPerson(Person person) {
        Person createdPerson =  personRepository.save(person);

        rabbitTemplate.convertAndSend(RabbitMQRouting.Exchange.PERSON.name(), RabbitMQRouting.Person.CREATE.name(), person);

        return createdPerson;
    }
    public void deletePerson(String personnummer) throws PersonMissingException {
        Long rowsDeleted = personRepository.deletePersonByPersonnummer(personnummer);
        if (rowsDeleted == 0) {
            throw new PersonMissingException("Unable to find person to delete with person number: " + personnummer, personnummer);
        }
    }
}
