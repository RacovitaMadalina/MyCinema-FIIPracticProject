package ro.fiipractic.mycinema.services;

import ro.fiipractic.mycinema.entities.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    List<Person> getPersons();

    Person getPersonById(Long id);

    Person savePerson(Person person);

    Person updatePerson(Person person);

    void deletePerson(Person person);
}
