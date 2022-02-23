package com.kirillvlasovets.test.converter.controller;

import com.kirillvlasovets.test.converter.entity.Convertation;
import com.kirillvlasovets.test.converter.service.ConvertationServiceImpl;
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

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ConvertationServiceImpl convertationService;
    @Autowired
    private CurrencyHistoryService currencyHistoryService;
    @Autowired
    private ConvertingLogicImpl convertingLogic;

    static List<String> currencyNames = new ArrayList<>();
    static List<Double> currencyValues = new ArrayList<>();

    @GetMapping ("/another")
    public String getConvertingModel(Model model) throws Exception {
        currencyNames = convertingLogic.getInfoFromCBR("Name");

        model.addAttribute("convertingModel", new ConvertingModel());
        model.addAttribute("currencies", currencyNames);
        return "another";
    }

    @PostMapping("/another")
    public String submitConvertingModel(@ModelAttribute ConvertingModel convertingModel, Model model) throws Exception {
        long now = System.currentTimeMillis();
        Date dateToday = new Date(now);
        Time currentTime = new Time(now);

        CurrencyHistory todayCourses = currencyHistoryService
                .getCurrencyHistoriesByConvertationDate(dateToday);
        if (todayCourses == null) {
            List<String> list = convertingLogic.getInfoFromCBR("Value");
            for (String value: list) {
                currencyValues.add(Double.parseDouble(value));
            }
            currencyHistoryService.saveCurrencyHistory(
                    new CurrencyHistory(dateToday, currencyNames.toString(), currencyValues.toString()));
        }
        else {
            todayCourses = currencyHistoryService
                    .getCurrencyHistoriesByConvertationDate(dateToday);
            String[] todayCoursesMass = todayCourses.getCurrencyValues()
                    .replace("[", "")
                    .replace("]", "")
                    .split(",");
            for (String currency: todayCoursesMass) {
                currencyValues.add(Double.parseDouble(currency));
            }
        }

        String inCurrencyName = convertingModel.getInCurrencyName();
        String inCurrencyValue = convertingModel.getInCurrencyValue();
        String outCurrencyName = convertingModel.getOutCurrencyName();

        double inCourse = currencyValues.get(currencyNames.indexOf(inCurrencyName));
        double outCourse = currencyValues.get(currencyNames.indexOf(outCurrencyName));
        double result = convertingLogic.getConvertedValue(inCourse, outCourse, inCurrencyValue);

        convertationService.saveConvertation(
                new Convertation(inCurrencyName, outCurrencyName, Double.parseDouble(inCurrencyValue), result,
                        dateToday, currentTime));

        model.addAttribute("inCurrencyName", inCurrencyName);
        model.addAttribute("inCurrencyValue", inCurrencyValue);
        model.addAttribute("outCurrencyName", outCurrencyName);
        model.addAttribute("outCurrencyValue", result);
        return "converting";
    }

    @RequestMapping("/history")
    public String showConvertingHistory(Model model) {
        model.addAttribute("convertations", convertationService.getAllConvertations());
        return "history";
    }
}
