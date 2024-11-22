package org.example.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.example.dto.WiseSayingPaging;
import org.example.dto.WiseSayingReq;
import org.example.entities.WiseSaying;
import org.example.repositories.WiseSayingRepository;
import org.example.utils.WiseSayingHandler;

public class WiseSayingServiceImpl implements WiseSayingService {

    private static final int PAGE_SIZE = 5;

    private final WiseSayingRepository repository;

    public WiseSayingServiceImpl(final WiseSayingRepository repository) {
        this.repository = repository;
    }

    @Override
    public void makeDirectory() {
        repository.makeDirectory();
    }

    @Override
    public int saveWiseSaying(final WiseSayingReq dto) throws IOException {
        return repository.saveToJson(dto).getId();
    }

    @Override
    public int updateWiseSaying(final int id, final WiseSayingReq dto) throws IOException {
        return repository.update(id, dto).getId();
    }

    @Override
    public void buildData() throws IOException {
        String buildData = WiseSayingHandler.buildData(repository.loadAll());
        repository.buildData(buildData);
    }

    @Override
    public Map<Integer, WiseSaying> loadAll() throws IOException {
        return repository.loadAll();
    }

    @Override
    public void saveLastId() throws IOException {
        repository.saveLastIdTxt();
    }

    @Override
    public int loadLastId() {
        return repository.loadLastId();
    }

    @Override
    public void deleteWiseSaying(final int id) throws IOException {
        repository.deleteJson(id);
    }

    @Override
    public WiseSayingPaging getPaging(final String keywordType, final String keyword, int page) throws IOException {
        List<Entry<Integer, WiseSaying>> entries = new ArrayList<>(repository.loadAll().entrySet());
        List<Entry<Integer, WiseSaying>> filteredEntries = entries.stream().filter(e -> {
            if (keyword == null) {
                return true;
            }
            if (!keyword.trim().isEmpty()) {
                if ("content".equals(keywordType)) {
                    return e.getValue().getContent().contains(keyword);
                } else if ("author".equals(keywordType)) {
                    return e.getValue().getAuthor().contains(keyword);
                } else {
                    return e.getValue().getAuthor().contains(keyword) || e.getValue().getContent()
                            .contains(keyword);
                }
            } else {
                return true;
            }
        }).collect(Collectors.toList());
        filteredEntries.sort((e1, e2) -> e2.getKey().compareTo(e1.getKey()));

        int totalItems = filteredEntries.size();
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);

        if (page > totalPages) {
            page = 1;
        }

        int startIdx = (page - 1) * PAGE_SIZE;
        int endIdx = Math.min(startIdx + PAGE_SIZE, totalItems);

        List<WiseSaying> pagedList = new ArrayList<>();
        if (startIdx < totalItems) {
            for (int i = startIdx; i < endIdx; i++) {
                pagedList.add(filteredEntries.get(i).getValue());
            }
        }

        return WiseSayingPaging.of(page, totalPages, pagedList);
    }

}
