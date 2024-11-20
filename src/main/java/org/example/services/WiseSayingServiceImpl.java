package org.example.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.example.dto.WiseSayingPaging;
import org.example.dto.WiseSayingReq;
import org.example.entities.WiseSaying;
import org.example.proxy.LoggingProxy;
import org.example.repositories.WiseSayingRepository;
import org.example.repositories.WiseSayingRepositoryImpl;
import org.example.utils.WiseSayingHandler;

public class WiseSayingServiceImpl implements WiseSayingService {

    private static final int PAGE_SIZE = 5;

    private static Map<Integer, WiseSaying> map = new LinkedHashMap<>();

    private int currentId = 1;

    private WiseSayingRepository repository = LoggingProxy.createProxy(new WiseSayingRepositoryImpl(),
            WiseSayingRepository.class);

    @Override
    public void makeDirectory() {
        repository.makeDirectory();
    }

    @Override
    public int saveWiseSaying(final WiseSayingReq dto) throws IOException {
        WiseSaying wiseSaying = WiseSaying.of(currentId++, dto.getAuthor(), dto.getContent());
        map.put(wiseSaying.getId(), wiseSaying);
        repository.saveToJson(wiseSaying);
        return wiseSaying.getId();
    }

    @Override
    public int updateWiseSaying(final int id, final WiseSayingReq dto) throws IOException {
        WiseSaying wiseSaying = WiseSaying.of(id, dto.getAuthor(), dto.getContent());
        map.put(wiseSaying.getId(), wiseSaying);
        repository.saveToJson(wiseSaying);
        return wiseSaying.getId();
    }

    @Override
    public void buildData() throws IOException {
        String buildData = WiseSayingHandler.buildData(map);
        repository.buildData(buildData);
    }

    @Override
    public Map<Integer, WiseSaying> loadAll() throws IOException {
        return repository.loadAll(map);
    }

    @Override
    public void saveLastId() throws IOException {
        repository.saveLastIdTxt(currentId);
    }

    @Override
    public int loadLastId() throws IOException {
        if (repository.hasLastId()) {
            currentId = Math.max(map.size(), repository.loadLastId());
        }
        return currentId;
    }

    @Override
    public void deleteWiseSaying(final int id) throws IOException {
        map.remove(id);
        repository.deleteJson(id);
    }

    @Override
    public WiseSayingPaging getPaging(final String keywordType, final String keyword, int page) {
        List<Entry<Integer, WiseSaying>> entries = new ArrayList<>(map.entrySet());
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
