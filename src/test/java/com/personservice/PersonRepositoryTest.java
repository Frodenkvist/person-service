package com.personservice;

import com.personservice.common.model.Person;
import com.personservice.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class PersonRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void findPersonByPersonnummer() {
        Person person1 = new Person("19900101-0001", "Nisse", "Hult");
        Person person2 = new Person("19900101-0002", "Klas", "Olas");
        Person person3 = new Person("19900101-0003", "Stig", "Helmer");
        Person person4 = new Person("19900101-0004", "Olle", "Nörge");

        entityManager.persist(person1);
        entityManager.persist(person2);
        entityManager.persist(person3);
        entityManager.persist(person4);

        Optional<Person> person = personRepository.findPersonByPersonnummer(person2.getPersonnummer());

        assertTrue(person.isPresent());
        assertEquals(person2, person.get());
    }

    @Test
    public void deletePersonByPersonnummer() {
        Person person1 = new Person("19900101-0001", "Nisse", "Hult");
        Person person2 = new Person("19900101-0002", "Klas", "Olas");
        Person person3 = new Person("19900101-0003", "Stig", "Helmer");
        Person person4 = new Person("19900101-0004", "Olle", "Nörge");

        entityManager.persist(person1);
        entityManager.persist(person2);
        entityManager.persist(person3);
        entityManager.persist(person4);

        Long deleted = personRepository.deletePersonByPersonnummer(person2.getPersonnummer());

        assertNotNull(deleted);
        assertEquals(1L, deleted.longValue());

        assertNotNull(entityManager.find(Person.class, person1.getPersonnummer()));
        assertNull(entityManager.find(Person.class, person2.getPersonnummer()));
        assertNotNull(entityManager.find(Person.class, person3.getPersonnummer()));
        assertNotNull(entityManager.find(Person.class, person4.getPersonnummer()));
    }
}
