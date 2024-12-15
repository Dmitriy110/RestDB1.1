package org.example.model;


import java.util.Objects;
import java.util.UUID;

public class Post {
    private UUID id;
    private String title;
    private String content;
    private UUID userId; // Foreign key to User

    public Post() { }
    public Post(UUID id, String title, String content, UUID userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
    }



    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(title, post.title) && Objects.equals(content, post.content) && Objects.equals(userId, post.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, userId);
    }

    @Override
    public String toString() {
        return "Post{" +
                "userId=" + userId +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", id=" + id +
                '}';
    }
}
