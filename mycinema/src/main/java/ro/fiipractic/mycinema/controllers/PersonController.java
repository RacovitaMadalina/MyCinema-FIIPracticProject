package ro.fiipractic.mycinema.controllers;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fiipractic.mycinema.dtos.PersonDto;
import ro.fiipractic.mycinema.entities.Person;
import ro.fiipractic.mycinema.exceptions.NotFoundException;
import ro.fiipractic.mycinema.services.PersonService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(PersonController.class.getName());

    /**
     * Endpoint for getting all persons from the table 'persons'
     * @return a list of all persons in 'persons' table
     */
    @GetMapping
    public ResponseEntity<List<PersonDto>> getPersons() {
        logger.info("GET MAPPING: api/persons");
        return new ResponseEntity<>(personService.getPersons().stream()
                                                  .map(person -> modelMapper.map(person, PersonDto.class))
                                                  .collect(Collectors.toList()),
                                    HttpStatus.OK);
    }

    /**
     * Endpoint for getting a specific person based on its id
     * @param id the id of the person whose infos have to be displayed
     * @throws NotFoundException when the id of the person given as path variable wasn't found in the DB
     * @return the person with the id given as param
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable("id") Long id) throws NotFoundException {
        logger.info("GET MAPPING: api/persons/" + id);
        Person personDB = personService.getPersonById(id);
        if(personDB == null){
            throw new NotFoundException(String.format("The person with the id=%s was not found in the DB.", id));
        }
        return new ResponseEntity<>(modelMapper.map(personDB, PersonDto.class), HttpStatus.OK);
    }

    /**
     * Endpoint for inserting a new person in the database
     * @param personDto The DTO with the completed fields corresponding to the new person
     * @return the location of the new created person
     * @throws URISyntaxException
     */
    @PostMapping
    public ResponseEntity<PersonDto> savePerson(@RequestBody PersonDto personDto) throws URISyntaxException {
        logger.info("POST MAPPING: api/persons");
        Person person = personService.savePerson(modelMapper.map(personDto, Person.class));
        return ResponseEntity.created(new URI("/api/persons/" + person.getId()))
                             .body(modelMapper.map(person, PersonDto.class));
    }

    /**
     * Endpoint for updating a person whose id was given as path variable
     * @param id the id of the person that has t be updated
     * @param personDtoToBeUdated the body containing the fields (with modifications) of the person with the given param id
     * @return the corresponding person whose fields were now updated
     * @throws NotFoundException when the id given as path variable is
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<PersonDto> updatePerson(@PathVariable("id") Long id, @RequestBody PersonDto personDtoToBeUdated) throws NotFoundException {
        logger.info("PUT MAPPING: api/persons/" + id);
        Person personDB = personService.getPersonById(id);
        if (personDB == null) {
            throw new NotFoundException(String.format("Person with id=%s was not found in the database.", id));
        }
        Person mappedPersonToBeUpdated = modelMapper.map(personDtoToBeUdated, Person.class);
        modelMapper.map(mappedPersonToBeUpdated, personDB);
        return new ResponseEntity<>(modelMapper.map(personService.updatePerson(personDB), PersonDto.class), HttpStatus.ACCEPTED);
    }

    /**
     * Endpoint for removing a specific person from DB
     * @param id the id of the person that has to be deleted from 'persons' table
     * @throws NotFoundException when the id of the person given as path variable wasn't found in the DB
     * @return no content
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) throws NotFoundException {
        logger.info("DELETE MAPPING: api/persons" + id);
        Person personDB = personService.getPersonById(id);

        if (personDB == null) {
            throw new NotFoundException(String.format("Person with id=%s was not found.", id));
        }

        personService.deletePerson(personDB);
        return ResponseEntity.noContent().build();
    }
}
