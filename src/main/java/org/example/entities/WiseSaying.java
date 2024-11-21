package org.example.entities;

public class WiseSaying extends BaseEntity {

    private final String author;
    private final String content;

    private WiseSaying(final int id, final String author, final String content) {
        super(id);
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public static WiseSaying of(final int id, final String author, final String content) {
        return new WiseSaying(id, author, content);
    }

}
