package com.example.repository;

import com.example.model.Voiture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long>{}