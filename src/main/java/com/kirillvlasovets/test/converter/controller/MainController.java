package com.kirillvlasovets.test.converter.controller;

import com.kirillvlasovets.test.converter.entity.CurrencyHistory;
import com.kirillvlasovets.test.converter.models.ConvertingModel;
import com.kirillvlasovets.test.converter.service.CurrencyHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.lang.Math.round;

@Controller
public class MainController {
    @Autowired
    private CurrencyHistoryService currencyHistoryService;

    static List<String> currencyNames = null;
    static List<Double> currencyValues = null;

    @GetMapping ("/another")
    public String getConvertingModel(Model model) throws Exception {
        URL url = new URL("https://www.cbr.ru/scripts/XML_daily.asp");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(connection.getInputStream());

        NodeList nodeList = document.getChildNodes();
        Element lang = null;
        currencyNames = new ArrayList<>();
        currencyValues = new ArrayList<>();
        //проходим по каждому элементу
        for(int i = 0; i < nodeList.getLength(); i++) {
            lang = (Element) nodeList.item(i);
            NodeList list = lang.getElementsByTagName("Name");
            for (int j = 0; j < list.getLength(); j++) {
                currencyNames.add(
                        lang.getElementsByTagName("Name").item(j).getFirstChild().getNodeValue());
                currencyValues.add(
                        Double.parseDouble(
                                lang.getElementsByTagName("Value").item(j).getFirstChild().getNodeValue()
                                        .replace(',', '.')));
            }
            System.out.println(currencyNames);
            System.out.println(currencyValues);
        }

        model.addAttribute("convertingModel", new ConvertingModel());

        model.addAttribute("currencies", currencyNames);
        model.addAttribute("currencyValues", currencyValues);
        connection.disconnect();
        return "another";
    }

    @PostMapping("/another")
    public String submitConvertingModel(@ModelAttribute ConvertingModel convertingModel, Model model) {
        String inCurrencyName = convertingModel.getInCurrencyName();
        String inCurrencyValue = convertingModel.getInCurrencyValue();
        String outCurrencyName = convertingModel.getOutCurrencyName();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateToday = dateFormat.format(new Date());
        CurrencyHistory todayCourses = currencyHistoryService
                .getCurrencyHistoriesByConvertationDate(dateToday);
        if (todayCourses == null) {
            currencyHistoryService.saveCurrencyHistory(
                    new CurrencyHistory(dateToday, currencyNames.toString(), currencyValues.toString()));
            todayCourses = currencyHistoryService
                    .getCurrencyHistoriesByConvertationDate(dateToday);
        }
        String[] todayCoursesMass = todayCourses.getCurrencyValues().split(",");
        currencyValues = new ArrayList<>();
        for (String currency: todayCoursesMass) {
            currencyValues.add(Double.parseDouble(currency));
        }

        double firstCourse = currencyValues.get(currencyNames.indexOf(inCurrencyName));
        double secondCourse = currencyValues.get(currencyNames.indexOf(outCurrencyName));
        double scale = Math.pow(10, 2);
        double result = round((firstCourse * Double.parseDouble(inCurrencyValue) / secondCourse) * scale) / scale;

        model.addAttribute("inCurrencyName", inCurrencyName);
        model.addAttribute("inCurrencyValue", inCurrencyValue);
        model.addAttribute("outCurrencyName", outCurrencyName);
        model.addAttribute("outCurrencyValue", result);
        return "converting";
    }
}
