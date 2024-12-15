package org.example.servlet.dto;

import java.util.UUID;

public class OutGoingDtoPost {
    private UUID id;
    private String title;
    private String content;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

}
