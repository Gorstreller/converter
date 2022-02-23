package com.kirillvlasovets.test.converter.service;

import java.util.List;

public interface ConvertingLogic {
    public List<String> getInfoFromCBR(String attributeName) throws Exception;

    public double getConvertedValue(double inCourse, double outCourse, String inCurrencyValue);
}
