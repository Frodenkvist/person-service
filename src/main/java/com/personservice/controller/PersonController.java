package com.personservice.controller;

import com.personservice.common.exception.PersonMissingException;
import com.personservice.controller.util.JS;
import com.personservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getPersons() {
        return JS.message(HttpStatus.OK, personService.getPersons());
    }

    @GetMapping("/{personnummer}")
    @ResponseBody
    public ResponseEntity<?> getPerson(@PathVariable String personnummer) {
        try {
            return JS.message(HttpStatus.OK, personService.getPerson(personnummer));
        } catch (PersonMissingException e) {
            return JS.message(HttpStatus.NOT_FOUND, "Unable to find person with personnnumer: " + personnummer);
        }
    }
}
