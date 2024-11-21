package org.example.dto;

import java.util.List;
import org.example.entities.WiseSaying;

public class WiseSayingPaging {

    private final int currentPage;
    private final int totalPages;
    private final List<WiseSaying> content;

    private WiseSayingPaging(final int currentPage, final int totalPages, final List<WiseSaying> content) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.content = content;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<WiseSaying> getContent() {
        return content;
    }

    public static WiseSayingPaging of(final int currentPage, final int totalPages, final List<WiseSaying> content) {
        return new WiseSayingPaging(currentPage, totalPages, content);
    }

}
