package com.kirillvlasovets.test.converter.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

@Service
public class ConvertingLogicImpl implements ConvertingLogic {

    @Override
    public List<String> getInfoFromCBR(String attributeName) throws Exception {
        URL url = new URL("https://www.cbr.ru/scripts/XML_daily.asp");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(connection.getInputStream());

        NodeList nodeList = document.getChildNodes();
        Element lang;
        List<String> finalArray = new ArrayList<>();
        // Проходим по каждому элементу
        for(int i = 0; i < nodeList.getLength(); i++) {
            lang = (Element) nodeList.item(i);
            NodeList list = lang.getElementsByTagName("Name");
            for (int j = 0; j < list.getLength(); j++) {
                finalArray.add(lang.getElementsByTagName(attributeName)
                        .item(j)
                        .getFirstChild()
                        .getNodeValue()
                        .replace(',', '.'));
            }
        }
        connection.disconnect();
        return finalArray;
    }

    @Override
    public double getConvertedValue(double inCourse, double outCourse, String inCurrencyValue) {
        double scale = Math.pow(10, 2);
        return round((inCourse * Double.parseDouble(inCurrencyValue) / outCourse) * scale) / scale;
    }
}
