package org.phrasebook.phrasebook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String text;

    @ManyToOne(optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    // EAGER — чтобы переводы отдавались в JSON без LazyInitializationException
    // (open-in-view=false). cascade=ALL — новые переводы сохраняются вместе с фразой.
    @OneToMany(mappedBy = "phrase", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Translation> translations = new ArrayList<>();
}
