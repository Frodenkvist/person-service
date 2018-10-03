package com.personservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personservice.common.exception.PersonMissingException;
import com.personservice.common.model.Person;
import com.personservice.controller.PersonController;
import com.personservice.service.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
@ActiveProfiles("test")
public class PersonControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonService personService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getPersons() throws Exception {
        Person person1 = new Person("19900101-0001", "Nisse", "Hult");
        Person person2 = new Person("19900101-0002", "Klas", "Olas");
        Person person3 = new Person("19900101-0003", "Stig", "Helmer");
        Person person4 = new Person("19900101-0004", "Olle", "Nörge");

        List<Person> persons = Arrays.asList(person1, person2, person3, person4);

        when(personService.getPersons()).thenReturn(persons);

        MvcResult result = mvc.perform(get("/v1/person")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Person> returnPersons = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Person>>() {});

        assertNotNull(returnPersons);
        assertEquals(persons, returnPersons);
    }

    @Test
    public void getPerson() throws Exception {
        Person person = new Person("19900101-0001", "Nisse", "Hult");

        when(personService.getPerson(person.getPersonnummer())).thenReturn(person);

        MvcResult result = mvc.perform(get("/v1/person/" + person.getPersonnummer())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Person returnPerson = objectMapper.readValue(result.getResponse().getContentAsString(),
                Person.class);

        assertNotNull(returnPerson);
        assertEquals(person, returnPerson);
    }

    @Test
    public void getMissingPerson() throws Exception {
        when(personService.getPerson("19900101-0001")).thenThrow(PersonMissingException.class);

        mvc.perform(get("/v1/person/19900101-0001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void createPerson() throws Exception {
        Person person = new Person("19900101-0001", "Nisse", "Hult");

        when(personService.createPerson(person)).thenReturn(person);

        MvcResult result = mvc.perform(post("/v1/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andReturn();

        Person returnPerson = objectMapper.readValue(result.getResponse().getContentAsString(),
                Person.class);

        assertNotNull(returnPerson);
        assertEquals(person, returnPerson);
    }

    @Test
    public void deletePerson() throws Exception {
        mvc.perform(delete("/v1/person/19900101-0001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMissingPerson() throws Exception {
        doThrow(PersonMissingException.class).when(personService).deletePerson("19900101-0001");

        mvc.perform(delete("/v1/person/19900101-0001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
