package org.example.model;

public class Data {
    
    final int    id;
    final String author;
    final String content;
    
    private Data(int id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }
    
    public static Data of(final int id, final String author, final String content) {
        return new Data(id, author, content);
    }
    
    public int getId() {
        return id;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getContent() {
        return content;
    }
    
}
