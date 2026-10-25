package org.phrasebook.phrasebook.repository;

import org.phrasebook.phrasebook.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    boolean existsByName(String name);
}