package org.example.services;

import org.example.error.ex.DataException;
import org.example.model.Data;
import org.example.repositories.DataRepository;

import java.io.IOException;
import java.util.Map;

import static org.example.error.ex.ErrorCode.*;

public class DataService {
    
    private static final String STORE_DIR = "./db/wiseSaying";
    
    private static final DataRepository REPOSITORY = new DataRepository();
    
    public void makeDirectory() {
        REPOSITORY.makeDirectory(STORE_DIR);
    }
    
    public void saveDataToJson(final Data data) {
        try {
            REPOSITORY.saveDataToJson(data, STORE_DIR);
        } catch (IOException e) {
            throw new DataException(FILE_SAVE_FAILED);
        }
    }
    
    public void saveStringToJson(final String str) {
        try {
            REPOSITORY.saveStringToJson(str, STORE_DIR);
        } catch (IOException e) {
            throw new DataException(FILE_SAVE_FAILED);
        }
    }
    
    public void saveIdToTxt(final int id) {
        try {
            REPOSITORY.saveIdToTxt(id, STORE_DIR);
        } catch (IOException e) {
            throw new DataException(FILE_SAVE_FAILED);
        }
    }
    
    public Data loadDataByJsonFile(final String fileName) {
        try {
            return REPOSITORY.loadDataByJsonFile(STORE_DIR + "/" + fileName);
        } catch (IOException e) {
            throw new DataException(FILE_LOAD_FAILED);
        }
    }
    
    public Map<Integer, Data> loadAllDataByStoreDir(final Map<Integer, Data> map) {
        try {
            return REPOSITORY.loadAllDataByStoreDir(map, STORE_DIR);
        } catch (IOException e) {
            throw new DataException(FILE_LOAD_FAILED);
        }
    }
    
    public int loadIdByTxtFile() {
        return REPOSITORY.loadIdByTxtFile(STORE_DIR);
    }
    
    public void deleteFile(final String fileName) {
        try {
            REPOSITORY.deleteFile(STORE_DIR + "/" + fileName);
        } catch (IOException e) {
            throw new DataException(FILE_DELETE_FAILED);
        }
    }
    
}
