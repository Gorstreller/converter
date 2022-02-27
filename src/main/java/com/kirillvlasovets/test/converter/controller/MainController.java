package com.kirillvlasovets.test.converter.controller;

import com.kirillvlasovets.test.converter.entity.Converting;
import com.kirillvlasovets.test.converter.service.ConvertingServiceImpl;
import com.kirillvlasovets.test.converter.service.ConvertingLogicImpl;
import com.kirillvlasovets.test.converter.entity.CurrencyHistory;
import com.kirillvlasovets.test.converter.models.ConvertingModel;
import com.kirillvlasovets.test.converter.service.CurrencyHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    private ConvertingServiceImpl convertingService;
    @Autowired
    private CurrencyHistoryService currencyHistoryService;
    @Autowired
    private ConvertingLogicImpl convertingLogic;

    static List<String> currencyNames = new ArrayList<>();

    @GetMapping ("/new_converting")
    public String getConvertingModel(Model model) throws Exception {
        Map<String, List<String>> mapCurrenciesInfo = convertingLogic.getInfoFromCBR();
        Map<String, String> currenciesNamesAndCharCodes = new HashMap<>();

        Set<String> keySet = mapCurrenciesInfo.keySet();
        for (String key : keySet) {
            currenciesNamesAndCharCodes.put(key, mapCurrenciesInfo.get(key).get(1));
            currencyNames.add(mapCurrenciesInfo.get(key).get(1));
        }

        model.addAttribute("convertingModel", new ConvertingModel());
        model.addAttribute("currencies", currenciesNamesAndCharCodes);
        return "new_converting";
    }

    @PostMapping("/converting_result")
    public String submitConvertingModel(@ModelAttribute ConvertingModel convertingModel, Model model) throws Exception {
        long now = System.currentTimeMillis();

        String dateToday = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        Time currentTime = new Time(now);

        Map<String, List<String>> mapCurrenciesInfo;
        if (currencyHistoryService.getCurrencyHistoriesByExchangeDate(dateToday) == null) {
            mapCurrenciesInfo = convertingLogic.getInfoFromCBR();
            currencyHistoryService.saveCurrencyHistory(
                    new CurrencyHistory(dateToday, convertingLogic.mapToStringSerialization(mapCurrenciesInfo)));
        }
        else {
            mapCurrenciesInfo = convertingLogic.stringToMapDeserialization(
                    currencyHistoryService.getCurrencyHistoriesByExchangeDate(dateToday).getCurrenciesInfo());
        }


        String inCurrencyCode = convertingModel.getInCurrencyName();
        String inCurrencyValue = convertingModel.getInCurrencyValue();
        String outCurrencyCode = convertingModel.getOutCurrencyName();

        String inCurrencyName = mapCurrenciesInfo.get(inCurrencyCode).get(1);
        String outCurrencyName = mapCurrenciesInfo.get(outCurrencyCode).get(1);

        double inCourse = Double.parseDouble(mapCurrenciesInfo.get(inCurrencyCode).get(2));
        double outCourse = Double.parseDouble(mapCurrenciesInfo.get(outCurrencyCode).get(2));
        double result = convertingLogic.getConvertedValue(inCourse, outCourse, inCurrencyValue);

        convertingService.saveConvertation(
                new Converting(inCurrencyName, outCurrencyName, Double.parseDouble(inCurrencyValue), result,
                        dateToday, currentTime));

        model.addAttribute("inCurrencyName", inCurrencyName);
        model.addAttribute("inCurrencyValue", inCurrencyValue);
        model.addAttribute("outCurrencyName", outCurrencyName);
        model.addAttribute("outCurrencyValue", result);
        return "converting_result";
    }

    @RequestMapping("/history")
    public String showConvertingHistory(Model model) {
        List<Converting> allConvertings = convertingService.getAllConvertings()
                .stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        model.addAttribute("convertings", allConvertings);
        return "history";
    }
}
