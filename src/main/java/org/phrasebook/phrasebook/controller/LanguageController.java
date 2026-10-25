package org.phrasebook.phrasebook.controller;

import org.phrasebook.phrasebook.dto.LanguageRequest;
import org.phrasebook.phrasebook.model.Language;
import org.phrasebook.phrasebook.service.PhrasebookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final PhrasebookService service;

    public LanguageController(PhrasebookService service) {
        this.service = service;
    }

    @GetMapping
    public List<Language> all() {
        return service.getAllLanguages();
    }

    @PostMapping
    public Language add(@RequestBody LanguageRequest req) {
        return service.addLanguage(req.name(), req.code());
    }

    @PutMapping("/{id}")
    public Language update(@PathVariable Long id, @RequestBody LanguageRequest req) {
        return service.updateLanguage(id, req.name(), req.code());
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        service.deleteLanguage(id);
        return Map.of("message", "Deleted successfully");
    }
}
