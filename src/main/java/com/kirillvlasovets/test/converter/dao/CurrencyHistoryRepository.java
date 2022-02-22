package com.kirillvlasovets.test.converter.dao;

import com.kirillvlasovets.test.converter.entity.CurrencyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyHistoryRepository extends JpaRepository<CurrencyHistory, Integer> {
    public CurrencyHistory getCurrencyHistoriesByConvertationDate(String convertationDate);
}
