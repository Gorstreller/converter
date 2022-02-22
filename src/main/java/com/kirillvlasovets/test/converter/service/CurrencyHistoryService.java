package com.kirillvlasovets.test.converter.service;

import com.kirillvlasovets.test.converter.entity.CurrencyHistory;

import java.util.List;

public interface CurrencyHistoryService {
    public List<CurrencyHistory> getAllCurrencyHistory();

    public void saveCurrencyHistory(CurrencyHistory currencyHistory);

    public CurrencyHistory getCurrencyHistory(int id);

    public void deleteCurrencyHistory(int id);

    public CurrencyHistory getCurrencyHistoriesByConvertationDate(String convertationDate);
}
