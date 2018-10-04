package com.personservice.controller;

import com.common.util.JSON;
import com.personservice.common.exception.PersonMissingException;
import com.personservice.common.model.Person;
import com.personservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getPersons() {
        return JSON.message(HttpStatus.OK, personService.getPersons());
    }

    @GetMapping("/{personnummer}")
    @ResponseBody
    public ResponseEntity<?> getPerson(@PathVariable String personnummer) {
        try {
            return JSON.message(HttpStatus.OK, personService.getPerson(personnummer));
        } catch (PersonMissingException e) {
            return JSON.message(HttpStatus.NOT_FOUND, "Unable to find person with personnnumer: " + personnummer);
        }
    }

    @PostMapping
    public ResponseEntity<?> createPerson(@RequestBody @Valid Person person) {
        return JSON.message(HttpStatus.OK, personService.createPerson(person));
    }

    @DeleteMapping("/{personnummer}")
    public ResponseEntity<?> deletePerson(@PathVariable String personnummer) {
        try {
            personService.deletePerson(personnummer);
            return JSON.message(HttpStatus.OK, "Person with personnummer " + personnummer + " deleted");
        } catch (PersonMissingException e) {
            return JSON.message(HttpStatus.NOT_FOUND, "Unable to delete person with personnnumer: " + personnummer);
        }
    }
}
