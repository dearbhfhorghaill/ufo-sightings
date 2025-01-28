package org.ufo.sightings.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ufo.sightings.entities.Sighting;

// This will be AUTO IMPLEMENTED by Spring into a Bean called sightingRepository
// CRUD refers Create, Read, Update, Delete

public interface SightingRepository extends CrudRepository<Sighting, Integer>, PagingAndSortingRepository<Sighting, Integer> {
//    CashCard findByIdAndOwner(Long id, String owner);
//
//    Page<CashCard> findByOwner(String owner, PageRequest pageRequest);
//
//    boolean existsByIdAndOwner(Long id, String owner);
}
