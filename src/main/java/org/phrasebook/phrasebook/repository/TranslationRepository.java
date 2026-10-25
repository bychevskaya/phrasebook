package org.phrasebook.phrasebook.repository;

import org.phrasebook.phrasebook.model.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRepository extends JpaRepository<Translation, Long> {
    boolean existsByLanguageId(Long languageId);
}