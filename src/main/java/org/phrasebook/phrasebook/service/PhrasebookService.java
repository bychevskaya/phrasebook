package org.phrasebook.phrasebook.service;

import org.phrasebook.phrasebook.dto.PhraseRequest;
import org.phrasebook.phrasebook.dto.TranslationItem;
import org.phrasebook.phrasebook.model.*;
import org.phrasebook.phrasebook.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhrasebookService {

    private final LanguageRepository languageRepo;
    private final TopicRepository topicRepo;
    private final PlaceRepository placeRepo;
    private final PhraseRepository phraseRepo;
    private final TranslationRepository translationRepo;

    public PhrasebookService(LanguageRepository languageRepo,
                             TopicRepository topicRepo,
                             PlaceRepository placeRepo,
                             PhraseRepository phraseRepo,
                             TranslationRepository translationRepo) {
        this.languageRepo = languageRepo;
        this.topicRepo = topicRepo;
        this.placeRepo = placeRepo;
        this.phraseRepo = phraseRepo;
        this.translationRepo = translationRepo;
    }

    // ======================= LANGUAGE =======================

    @Transactional(readOnly = true)
    public List<Language> getAllLanguages() {
        return languageRepo.findAll();
    }

    @Transactional
    public Language addLanguage(String name, String code) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Language name is required");
        }
        if (code == null || code.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Language code is required");
        }
        String n = name.trim();
        String c = code.trim().toLowerCase();
        if (languageRepo.existsByName(n)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Language already exists");
        }
        if (languageRepo.existsByCode(c)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Language code already exists");
        }
        Language l = new Language();
        l.setName(n);
        l.setCode(c);
        return languageRepo.save(l);
    }

    @Transactional
    public Language updateLanguage(Long id, String name, String code) {
        Language l = languageRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Language not found"));
        if (name == null || name.isBlank() || code == null || code.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name and code are required");
        }
        String n = name.trim();
        String c = code.trim().toLowerCase();
        if (!n.equals(l.getName()) && languageRepo.existsByName(n)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Language already exists");
        }
        if (!c.equals(l.getCode()) && languageRepo.existsByCode(c)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Language code already exists");
        }
        l.setName(n);
        l.setCode(c);
        return languageRepo.save(l);
    }

    @Transactional
    public void deleteLanguage(Long id) {
        Language l = languageRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Language not found"));
        if (translationRepo.existsByLanguageId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete: language is used in translations");
        }
        languageRepo.delete(l);
    }

    // ======================= TOPIC =======================

    @Transactional(readOnly = true)
    public List<Topic> getAllTopics() {
        return topicRepo.findAll();
    }

    @Transactional
    public Topic addTopic(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Topic name is required");
        }
        String n = name.trim();
        if (topicRepo.existsByName(n)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Topic already exists");
        }
        Topic t = new Topic();
        t.setName(n);
        return topicRepo.save(t);
    }

    @Transactional
    public Topic updateTopic(Long id, String name) {
        Topic t = topicRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Topic name is required");
        }
        String n = name.trim();
        if (!n.equals(t.getName()) && topicRepo.existsByName(n)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Topic already exists");
        }
        t.setName(n);
        return topicRepo.save(t);
    }

    @Transactional
    public void deleteTopic(Long id) {
        Topic t = topicRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));
        if (phraseRepo.existsByTopicId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete: topic has phrases");
        }
        topicRepo.delete(t);
    }

    // ======================= PLACE =======================

    @Transactional(readOnly = true)
    public List<Place> getAllPlaces() {
        return placeRepo.findAll();
    }

    @Transactional
    public Place addPlace(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Place name is required");
        }
        String n = name.trim();
        if (placeRepo.existsByName(n)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Place already exists");
        }
        Place p = new Place();
        p.setName(n);
        return placeRepo.save(p);
    }

    @Transactional
    public Place updatePlace(Long id, String name) {
        Place p = placeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found"));
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Place name is required");
        }
        String n = name.trim();
        if (!n.equals(p.getName()) && placeRepo.existsByName(n)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Place already exists");
        }
        p.setName(n);
        return placeRepo.save(p);
    }

    @Transactional
    public void deletePlace(Long id) {
        Place p = placeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found"));
        if (phraseRepo.existsByPlaceId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete: place is used by phrases");
        }
        placeRepo.delete(p);
    }

    // ======================= PHRASE =======================

    /**
     * Поиск фраз с фильтрами:
     *   topicId — только из этой тематики
     *   placeId — только для этого места
     *   q       — подстрока в тексте фразы ИЛИ в любом её переводе (без учёта регистра)
     */
    @Transactional(readOnly = true)
    public List<Phrase> searchPhrases(Long topicId, Long placeId, String q) {
        List<Phrase> list;
        if (topicId != null && placeId != null) {
            list = phraseRepo.findByTopicIdAndPlaceId(topicId, placeId);
        } else if (topicId != null) {
            list = phraseRepo.findByTopicId(topicId);
        } else if (placeId != null) {
            list = phraseRepo.findByPlaceId(placeId);
        } else {
            list = phraseRepo.findAll();
        }

        if (q != null && !q.isBlank()) {
            String needle = q.trim().toLowerCase();
            list = list.stream()
                    .filter(p -> p.getText().toLowerCase().contains(needle)
                            || p.getTranslations().stream()
                            .anyMatch(t -> t.getText().toLowerCase().contains(needle)))
                    .collect(Collectors.toList());
        }
        return list;
    }

    @Transactional(readOnly = true)
    public Phrase getPhrase(Long id) {
        return phraseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phrase not found"));
    }

    @Transactional
    public Phrase addPhrase(PhraseRequest req) {
        validatePhrase(req);

        Phrase phrase = new Phrase();
        phrase.setText(req.text().trim());
        phrase.setTopic(findTopic(req.topicId()));
        phrase.setPlace(findPlace(req.placeId()));

        addTranslations(phrase, req.translations());

        return phraseRepo.save(phrase);
    }

    @Transactional
    public Phrase updatePhrase(Long id, PhraseRequest req) {
        validatePhrase(req);

        Phrase phrase = phraseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phrase not found"));

        phrase.setText(req.text().trim());
        phrase.setTopic(findTopic(req.topicId()));
        phrase.setPlace(findPlace(req.placeId()));

        // Самый простой способ обновить переводы:
        //   1) удалить все старые,
        //   2) очистить коллекцию,
        //   3) добавить новые.
        List<Translation> old = new ArrayList<>(phrase.getTranslations());
        phrase.getTranslations().clear();
        translationRepo.deleteAll(old);

        addTranslations(phrase, req.translations());

        return phraseRepo.save(phrase);
    }

    @Transactional
    public void deletePhrase(Long id) {
        Phrase phrase = phraseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phrase not found"));
        // cascade=ALL на translations удалит их вместе с фразой
        phraseRepo.delete(phrase);
    }

    // ======================= helpers =======================

    private void validatePhrase(PhraseRequest req) {
        if (req.text() == null || req.text().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phrase text is required");
        }
        if (req.topicId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Topic is required");
        }
    }

    private Topic findTopic(Long id) {
        return topicRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));
    }

    private Place findPlace(Long id) {
        if (id == null) return null;
        return placeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found"));
    }

    private void addTranslations(Phrase phrase, List<TranslationItem> items) {
        if (items == null) return;
        for (TranslationItem item : items) {
            if (item.text() == null || item.text().isBlank()) continue;

            Language lang = languageRepo.findById(item.languageId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Language not found: " + item.languageId()));

            Translation t = new Translation();
            t.setPhrase(phrase);
            t.setLanguage(lang);
            t.setText(item.text().trim());
            phrase.getTranslations().add(t);
        }
    }
}