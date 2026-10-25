package org.phrasebook.phrasebook.repository;

import org.phrasebook.phrasebook.model.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhraseRepository extends JpaRepository<Phrase, Long> {
    List<Phrase> findByTopicId(Long topicId);
    List<Phrase> findByPlaceId(Long placeId);
    List<Phrase> findByTopicIdAndPlaceId(Long topicId, Long placeId);

    boolean existsByTopicId(Long topicId);
    boolean existsByPlaceId(Long placeId);
}