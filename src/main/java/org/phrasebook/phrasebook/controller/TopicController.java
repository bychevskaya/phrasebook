package org.phrasebook.phrasebook.controller;

import org.phrasebook.phrasebook.dto.TopicRequest;
import org.phrasebook.phrasebook.model.Topic;
import org.phrasebook.phrasebook.service.PhrasebookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final PhrasebookService service;

    public TopicController(PhrasebookService service) {
        this.service = service;
    }

    @GetMapping
    public List<Topic> all() {
        return service.getAllTopics();
    }

    @PostMapping
    public Topic add(@RequestBody TopicRequest req) {
        return service.addTopic(req.name());
    }

    @PutMapping("/{id}")
    public Topic update(@PathVariable Long id, @RequestBody TopicRequest req) {
        return service.updateTopic(id, req.name());
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        service.deleteTopic(id);
        return Map.of("message", "Deleted successfully");
    }
}