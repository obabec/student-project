package com.redhat.restdemo.model.service;

import com.redhat.restdemo.model.entity.Ownership;
import com.redhat.restdemo.model.repository.BookRepository;
import com.redhat.restdemo.model.repository.LibraryRepository;
import com.redhat.restdemo.model.repository.OwnershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
public class OwnershipServiceImpl implements OwnershipService {
    @Autowired
    private OwnershipRepository ownershipRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Iterable<Ownership> findAll() {
        return ownershipRepository.findAll();
    }

    @Override
    public Ownership add(Ownership ownership) {
        if (ownership.getBookId() == null || ownership.getLibraryId() == null ||
                !bookRepository.existsById(ownership.getBookId()) ||
                !libraryRepository.existsById(ownership.getLibraryId())) {
            throw new IllegalArgumentException();
        }
        return ownershipRepository.save(ownership);
    }

    @Override
    public Ownership delete(Integer id) {
        if (!ownershipRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        Optional<Ownership> ownership = ownershipRepository.findById(id);
        ownershipRepository.deleteById(id);
        return ownership.get();
    }
}