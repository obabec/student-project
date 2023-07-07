package com.redhat.restdemo.controllers;

import com.redhat.restdemo.model.entity.Ownership;
import com.redhat.restdemo.model.service.OwnershipService;
import com.redhat.restdemo.model.service.OwnershipServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/ownership")
public class OwnershipController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private OwnershipService ownershipService;

    @GetMapping
    public ResponseEntity<Iterable<Ownership>> getAllOwnerships() {
        LOGGER.info("Authorship listed");
        return new ResponseEntity<>(ownershipService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Ownership> addOwnership(@RequestBody Ownership ownership) {
        try {
            ownershipService.add(ownership);
            return new ResponseEntity<>(ownership, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Ownership> deleteOwnership(@PathVariable Integer id) {
        try {
            Ownership ownership = ownershipService.delete(id);
            LOGGER.info("Ownership deleted successfully");
            return new ResponseEntity<>(ownership, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}