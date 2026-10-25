package org.phrasebook.phrasebook.repository;

import org.phrasebook.phrasebook.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    boolean existsByName(String name);
    boolean existsByCode(String code);
}
