package org.phrasebook.phrasebook.controller;

import org.phrasebook.phrasebook.dto.PlaceRequest;
import org.phrasebook.phrasebook.model.Place;
import org.phrasebook.phrasebook.service.PhrasebookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PhrasebookService service;

    public PlaceController(PhrasebookService service) {
        this.service = service;
    }

    @GetMapping
    public List<Place> all() {
        return service.getAllPlaces();
    }

    @PostMapping
    public Place add(@RequestBody PlaceRequest req) {
        return service.addPlace(req.name());
    }

    @PutMapping("/{id}")
    public Place update(@PathVariable Long id, @RequestBody PlaceRequest req) {
        return service.updatePlace(id, req.name());
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        service.deletePlace(id);
        return Map.of("message", "Deleted successfully");
    }
}