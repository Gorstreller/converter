package com.kirillvlasovets.test.converter.models;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

//@Getter
public class ConvertingModel {

    private String inCurrencyName;
    @NotEmpty(message = "Ошибка формата")
    private String inCurrencyValue;
    private String outCurrencyName;
    private String outCurrencyValue;

    public ConvertingModel(String inCurrencyName, String inCurrencyValue, String outCurrencyName, String outCurrencyValue) {
        this.inCurrencyName = inCurrencyName;
        this.inCurrencyValue = inCurrencyValue;
        this.outCurrencyName = outCurrencyName;
        this.outCurrencyValue = outCurrencyValue;
    }

    public ConvertingModel() {
        super();
    }

    public String getInCurrencyName() {
        return inCurrencyName;
    }

    public void setInCurrencyName(String inCurrencyName) {
        this.inCurrencyName = inCurrencyName;
    }

    public String getInCurrencyValue() {
        return inCurrencyValue;
    }

    public void setInCurrencyValue(String inCurrencyValue) {
        this.inCurrencyValue = inCurrencyValue;
    }

    public String getOutCurrencyName() {
        return outCurrencyName;
    }

    public void setOutCurrencyName(String outCurrencyName) {
        this.outCurrencyName = outCurrencyName;
    }

    public String getOutCurrencyValue() {
        return outCurrencyValue;
    }

    public void setOutCurrencyValue(String outCurrencyValue) {
        this.outCurrencyValue = outCurrencyValue;
    }
}
