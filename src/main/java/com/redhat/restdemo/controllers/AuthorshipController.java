package com.redhat.restdemo.controllers;

import com.redhat.restdemo.model.entity.Author;
import com.redhat.restdemo.model.entity.Authorship;
import com.redhat.restdemo.model.service.AuthorshipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authorship")
public class AuthorshipController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private AuthorshipService authorshipService;

    @GetMapping
    public ResponseEntity<Iterable<Authorship>> getAllAuthorship() {
        LOGGER.info("Authorship listed");
        Iterable<Authorship> authors = authorshipService.findAll();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Authorship> addAuthorship(@RequestBody Authorship authorship) {
        try {
            Authorship createdAuthorship = authorshipService.add(authorship);
            LOGGER.info("Authorship successfully added!");
            return new ResponseEntity<>(createdAuthorship, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.info(e.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Authorship> deleteAuthorship(@PathVariable Integer id) {
        try {
            Authorship deletedAuthorship = authorshipService.delete(id);
            LOGGER.info("Authorship successfully deleted!");
            return new ResponseEntity<>(deletedAuthorship, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
