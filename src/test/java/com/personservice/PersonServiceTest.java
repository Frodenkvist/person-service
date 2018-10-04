package com.personservice;

import com.personservice.common.exception.PersonMissingException;
import com.personservice.common.model.Person;
import com.personservice.repository.PersonRepository;
import com.personservice.service.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class PersonServiceTest {
    @TestConfiguration
    static class PersonServiceTestContextConfiguration {
        @Bean
        public PersonService personService() {
            return new PersonService();
        }
    }

    @Autowired
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    public void getPerson() throws PersonMissingException {
        Person person = new Person("19900101-0001", "Nisse", "Hult");

        when(personRepository.findPersonByPersonnummer(person.getPersonnummer())).thenReturn(Optional.of(person));

        Person returnPerson = personService.getPerson(person.getPersonnummer());

        assertNotNull(returnPerson);
        assertEquals(person, returnPerson);
    }

    @Test(expected = PersonMissingException.class)
    public void getMissingPerson() throws PersonMissingException {
        when(personRepository.findPersonByPersonnummer("19900101-0001")).thenReturn(Optional.empty());

        personService.getPerson("19900101-0001");
    }

    @Test
    public void getPersons() {
        Person person1 = new Person("19900101-0001", "Nisse", "Hult");
        Person person2 = new Person("19900101-0002", "Klas", "Olas");
        Person person3 = new Person("19900101-0003", "Stig", "Helmer");
        Person person4 = new Person("19900101-0004", "Olle", "Nörge");

        List<Person> persons = Arrays.asList(person1, person2, person3, person4);

        when(personRepository.findAll()).thenReturn(persons);

        List<Person> returnPersons = personService.getPersons();

        assertNotNull(returnPersons);
        assertEquals(persons, returnPersons);
    }

    @Test
    public void createPerson() {
        Person person = new Person("19900101-0001", "Nisse", "Hult");

        when(personRepository.save(person)).thenReturn(person);

        Person returnPerson = personService.createPerson(person);

        assertNotNull(returnPerson);
        assertEquals(person, returnPerson);
    }

    @Test
    public void deletePerson() throws PersonMissingException {
        when(personRepository.deletePersonByPersonnummer("19900101-0001")).thenReturn(1L);

        personService.deletePerson("19900101-0001");
    }

    @Test(expected = PersonMissingException.class)
    public void deleteMissingPerson() throws PersonMissingException {
        when(personRepository.deletePersonByPersonnummer("19900101-0001")).thenReturn(0L);

        personService.deletePerson("19900101-0001");
    }
}
