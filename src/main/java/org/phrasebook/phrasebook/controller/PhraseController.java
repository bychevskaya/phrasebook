package org.phrasebook.phrasebook.controller;

import org.phrasebook.phrasebook.dto.PhraseRequest;
import org.phrasebook.phrasebook.model.Phrase;
import org.phrasebook.phrasebook.service.PhrasebookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/phrases")
public class PhraseController {

    private final PhrasebookService service;

    public PhraseController(PhrasebookService service) {
        this.service = service;
    }

    /**
     * GET /api/phrases
     * GET /api/phrases?topicId=1
     * GET /api/phrases?placeId=2
     * GET /api/phrases?topicId=1&placeId=2&q=hello
     */
    @GetMapping
    public List<Phrase> list(@RequestParam(required = false) Long topicId,
                             @RequestParam(required = false) Long placeId,
                             @RequestParam(required = false) String q) {
        return service.searchPhrases(topicId, placeId, q);
    }

    @GetMapping("/{id}")
    public Phrase byId(@PathVariable Long id) {
        return service.getPhrase(id);
    }

    @PostMapping
    public Phrase add(@RequestBody PhraseRequest req) {
        return service.addPhrase(req);
    }

    @PutMapping("/{id}")
    public Phrase update(@PathVariable Long id, @RequestBody PhraseRequest req) {
        return service.updatePhrase(id, req);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        service.deletePhrase(id);
        return Map.of("message", "Deleted successfully");
    }
}