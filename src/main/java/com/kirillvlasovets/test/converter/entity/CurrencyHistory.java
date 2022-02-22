package com.kirillvlasovets.test.converter.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "currency_history")
public class CurrencyHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "convertation_date")
    private Date convertationDate;

    @Column(name = "currency_names")
    private String currencyNames;

    @Column(name = "currency_values")
    private String currencyValues;

    public CurrencyHistory() {
    }

    public CurrencyHistory(Date convertationDate, String currencyNames, String currencyValues) {
        this.convertationDate = convertationDate;
        this.currencyNames = currencyNames;
        this.currencyValues = currencyValues;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getConvertationDate() {
        return convertationDate;
    }

    public void setConvertationDate(Date convertationDate) {
        this.convertationDate = convertationDate;
    }

    public String getCurrencyNames() {
        return currencyNames;
    }

    public void setCurrencyNames(String currencyName) {
        this.currencyNames = currencyName;
    }

    public String getCurrencyValues() {
        return currencyValues;
    }

    public void setCurrencyValues(String currencyValue) {
        this.currencyValues = currencyValue;
    }
}
