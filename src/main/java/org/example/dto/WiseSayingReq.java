package org.example.dto;

public class WiseSayingReq {

    private final String author;
    private final String content;

    private WiseSayingReq(final String author, final String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public static WiseSayingReq of(final String author, final String content) {
        return new WiseSayingReq(author, content);
    }

}
