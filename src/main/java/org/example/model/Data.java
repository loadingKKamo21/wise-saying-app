package org.example.model;

public class Data {
    
    final int    idx;
    final String author;
    final String content;
    
    private Data(int idx, String author, String content) {
        this.idx = idx;
        this.author = author;
        this.content = content;
    }
    
    public static Data of(final int idx, final String author, final String content) {
        return new Data(idx, author, content);
    }
    
    public int getIdx() {
        return idx;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getContent() {
        return content;
    }
    
}
