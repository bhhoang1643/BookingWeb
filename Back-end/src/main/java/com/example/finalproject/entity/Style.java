package com.example.finalproject.entity;

import jakarta.persistence.*;

@Entity
public class Style {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "style_tag_id", nullable = false)
    private StyleTag styleTag;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    public Style() {}

    public Style(StyleTag styleTag, Agent agent) {
        this.styleTag = styleTag;
        this.agent = agent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StyleTag getStyleTag() {
        return styleTag;
    }

    public void setStyleTag(StyleTag styleTag) {
        this.styleTag = styleTag;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
