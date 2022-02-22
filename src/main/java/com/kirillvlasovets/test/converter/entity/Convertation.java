package com.kirillvlasovets.test.converter.entity;

import javax.persistence.*;

@Entity
@Table(name = "convertations")
public class Convertation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "in_currency_name")
    private String inCurrencyName;

    @Column(name = "out_currency_name")
    private String outCurrencyName;

    @Column(name = "in_currency_value")
    private int inCurrencyValue;

    @Column(name = "out_currency_value")
    private int outCurrencyValue;

    @Column(name = "convertation_date", insertable = false, updatable = false)
    private String convertationDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "convertation_date")
    private CurrencyHistory currencyHistory;

    public Convertation() {
    }

    public Convertation(String inCurrencyName, String outCurrencyName, Integer inCurrencyValue, Integer outCurrencyValue, String convertationDate) {
        this.inCurrencyName = inCurrencyName;
        this.outCurrencyName = outCurrencyName;
        this.inCurrencyValue = inCurrencyValue;
        this.outCurrencyValue = outCurrencyValue;
        this.convertationDate = convertationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInCurrencyName() {
        return inCurrencyName;
    }

    public void setInCurrencyName(String inCurrencyName) {
        this.inCurrencyName = inCurrencyName;
    }

    public String getOutCurrencyName() {
        return outCurrencyName;
    }

    public void setOutCurrencyName(String outCurrencyName) {
        this.outCurrencyName = outCurrencyName;
    }

    public int getInCurrencyValue() {
        return inCurrencyValue;
    }

    public void setInCurrencyValue(int inCurrencyValue) {
        this.inCurrencyValue = inCurrencyValue;
    }

    public int getOutCurrencyValue() {
        return outCurrencyValue;
    }

    public void setOutCurrencyValue(int outCurrencyValue) {
        this.outCurrencyValue = outCurrencyValue;
    }

    public String getConvertationDate() {
        return convertationDate;
    }

    public void setConvertationDate(String convertationDate) {
        this.convertationDate = convertationDate;
    }

    public CurrencyHistory getCurrencyHistory() {
        return currencyHistory;
    }

    public void setCurrencyHistory(CurrencyHistory currencyHistory) {
        this.currencyHistory = currencyHistory;
    }
}
