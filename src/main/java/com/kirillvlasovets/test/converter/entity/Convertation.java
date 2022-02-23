package com.kirillvlasovets.test.converter.entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name = "convertations")
public class Convertation implements Comparable<Convertation> {
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

    @Column(name = "convertation_date")
    private Date convertationDate;

    @Column(name = "convertation_time")
    private Time convertationTime;

    public Convertation() {
    }

    public Convertation(String inCurrencyName, String outCurrencyName, double inCurrencyValue, double outCurrencyValue, Date convertationDate, Time convertationTime) {
        this.inCurrencyName = inCurrencyName;
        this.outCurrencyName = outCurrencyName;
        this.inCurrencyValue = inCurrencyValue;
        this.outCurrencyValue = outCurrencyValue;
        this.convertationDate = convertationDate;
        this.convertationTime = convertationTime;
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

    public double getInCurrencyValue() {
        return inCurrencyValue;
    }

    public void setInCurrencyValue(double inCurrencyValue) {
        this.inCurrencyValue = inCurrencyValue;
    }

    public double getOutCurrencyValue() {
        return outCurrencyValue;
    }

    public void setOutCurrencyValue(double outCurrencyValue) {
        this.outCurrencyValue = outCurrencyValue;
    }

    public Date getConvertationDate() {
        return convertationDate;
    }

    public void setConvertationDate(Date convertationDate) {
        this.convertationDate = convertationDate;
    }

    public Time getConvertationTime() {
        return convertationTime;
    }

    public void setConvertationTime(Time convertationTime) {
        this.convertationTime = convertationTime;
    }

    @Override
    public int compareTo(Convertation convertation) {
        if (getConvertationDate() == null || convertation.getConvertationDate() == null) {
            return 0;
        }
        if (Objects.equals(getConvertationDate(), convertation.getConvertationDate())) {
            return getConvertationTime().compareTo(convertation.convertationTime);
        }
        else {
            return getConvertationDate().compareTo(convertation.getConvertationDate());
        }
    }
}
