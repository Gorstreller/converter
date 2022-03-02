package com.kirillvlasovets.test.converter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ConvertingLogic {
    public Map<String, List<String>> getInfoFromCBR() throws Exception;

    public double getConvertedValue(double inCourse, double outCourse, double inCurrencyValue);

    public Map<String, List<String>> stringToMapDeserialization(String exchangeRates);

    public String mapToStringSerialization(Map<String, List<String>> mapExchangeRates);
}
