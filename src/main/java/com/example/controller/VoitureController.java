package com.example.controller;




import com.example.exception.ResourceNotFoundException;
import com.example.model.Voiture;
import com.example.repository.VoitureRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoitureController {

    @Autowired
    private VoitureRepository voitureRepository;

    @GetMapping("/voitures")
    public Page<Voiture> getAllPosts(Pageable pageable) {
        return voitureRepository.findAll(pageable);
    }

    @PostMapping("/voitures")
    public Voiture createPost(@Validated @RequestBody Voiture voiture) {
        return voitureRepository.save(voiture);
    }

    @PutMapping("/voitures/{voitureId}")
    public Voiture updatePost(@PathVariable Long voitureId, @Validated @RequestBody Voiture vRequest) {
        return voitureRepository.findById(voitureId).map(voiture -> {
            voiture.setName(vRequest.getName());
            return voitureRepository.save(voiture);
        }).orElseThrow(() -> new ResourceNotFoundException("VoitureId " + voitureId + " not found"));
    }


    @DeleteMapping("/voitures/{voitureId}")
    public ResponseEntity<?> deletePost(@PathVariable Long voitureId) {
        return voitureRepository.findById(voitureId).map(voiture -> {
            voitureRepository.delete(voiture);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("VoitureId " + voitureId + " not found"));
    }

}