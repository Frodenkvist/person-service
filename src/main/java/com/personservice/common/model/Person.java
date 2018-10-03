package com.personservice.common.model;

public class Person {

    private String personNummer;
    private String name;

    public Person(String personNummer, String name) {
        this.personNummer = personNummer;
        this.name = name;
    }

    public String getPersonNummer() {
        return personNummer;
    }

    public void setPersonNummer(String personNummer) {
        this.personNummer = personNummer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
