package com.kirillvlasovets.test.converter.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "currency_history")
@Getter
public class CurrencyHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "exchange_date")
    private String exchangeDate;

    @Column(name = "currencies_info")
    private String currenciesInfo;

    public CurrencyHistory() {
    }

    public CurrencyHistory(String exchangeDate, String currenciesInfo) {
        this.exchangeDate = exchangeDate;
        this.currenciesInfo = currenciesInfo;
    }
}
