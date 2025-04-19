package org.ufo.sightings.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ufo.sightings.entities.Sighting;

// This will be AUTO IMPLEMENTED by Spring into a Bean called sightingRepository
// CRUD refers Create, Read, Update, Delete

public interface SightingRepository extends CrudRepository<Sighting, Integer>, PagingAndSortingRepository<Sighting, Integer> {
    Sighting findByIdAndAgent(int id, String agent);

    Page<Sighting> findByAgent(String agent, PageRequest pageRequest);

    boolean existsByIdAndAgent(int id, String agent);
}
