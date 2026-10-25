package org.phrasebook.phrasebook.dto;

import java.util.List;

public record PhraseRequest(
        Long topicId,
        Long placeId,
        String text,
        List<TranslationItem> translations
) {}