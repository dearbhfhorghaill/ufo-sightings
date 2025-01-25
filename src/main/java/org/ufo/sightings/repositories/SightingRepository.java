package org.ufo.sightings.repositories;

import org.springframework.data.repository.CrudRepository;
import org.ufo.sightings.entities.Sightings;

// This will be AUTO IMPLEMENTED by Spring into a Bean called sightingRepository
// CRUD refers Create, Read, Update, Delete

public interface SightingRepository extends CrudRepository<Sightings, Integer> {
}
