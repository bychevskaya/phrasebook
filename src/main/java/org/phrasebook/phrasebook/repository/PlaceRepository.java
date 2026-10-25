package org.phrasebook.phrasebook.repository;

import org.phrasebook.phrasebook.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    boolean existsByName(String name);
}