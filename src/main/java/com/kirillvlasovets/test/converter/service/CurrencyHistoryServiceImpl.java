package com.kirillvlasovets.test.converter.service;

import com.kirillvlasovets.test.converter.dao.CurrencyHistoryRepository;
import com.kirillvlasovets.test.converter.entity.CurrencyHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyHistoryServiceImpl implements CurrencyHistoryService {

    @Autowired
    private CurrencyHistoryRepository currencyHistoryRepository;


    @Override
    public List<CurrencyHistory> getAllCurrencyHistory() {
        return currencyHistoryRepository.findAll();
    }

    @Override
    public void saveCurrencyHistory(CurrencyHistory currencyHistory) {
        currencyHistoryRepository.save(currencyHistory);
    }

    @Override
    public CurrencyHistory getCurrencyHistory(int id) {
        CurrencyHistory currencyHistory = null;
        Optional<CurrencyHistory> optional = currencyHistoryRepository.findById(id);
        if (optional.isPresent()) {
            currencyHistory = optional.get();
        }
        return currencyHistory;
    }

    @Override
    public void deleteCurrencyHistory(int id) {
        currencyHistoryRepository.deleteById(id);
    }

    @Override
    public CurrencyHistory getCurrencyHistoriesByConvertationDate(String convertationDate) {
        return currencyHistoryRepository.getCurrencyHistoriesByConvertationDate(convertationDate);
    }
}
