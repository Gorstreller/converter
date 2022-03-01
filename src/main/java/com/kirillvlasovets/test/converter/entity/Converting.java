package com.kirillvlasovets.test.converter.entity;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Entity
@Table(name = "convertations")
@Getter
@Data
public class Converting implements Comparable<Converting> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "in_currency_name")
    private String inCurrencyName;

    @Column(name = "out_currency_name")
    private String outCurrencyName;

    @Column(name = "in_currency_value")
    private double inCurrencyValue;

    @Column(name = "out_currency_value")
    private double outCurrencyValue;

    @Column(name = "exchange_date")
    private String exchangeDate;

    @Column(name = "exchange_time")
    private Time exchangeTime;

    public Converting() {
    }

    public Converting(String inCurrencyName, String outCurrencyName, double inCurrencyValue, double outCurrencyValue, String convertationDate, Time convertationTime) {
        this.inCurrencyName = inCurrencyName;
        this.outCurrencyName = outCurrencyName;
        this.inCurrencyValue = inCurrencyValue;
        this.outCurrencyValue = outCurrencyValue;
        this.exchangeDate = convertationDate;
        this.exchangeTime = convertationTime;
    }

    @SneakyThrows
    @Override
    public int compareTo(Converting converting) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        if (this.exchangeDate == null || converting.exchangeDate == null) {
            return 0;
        }
        if (Objects.equals(exchangeDate, converting.exchangeDate)) {
            return this.exchangeTime.compareTo(converting.exchangeTime);
        }
        else {
            return formatter.parse(this.exchangeDate).compareTo(formatter.parse(converting.exchangeDate));
        }
    }
}
