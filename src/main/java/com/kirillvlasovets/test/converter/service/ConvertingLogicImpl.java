package com.kirillvlasovets.test.converter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static java.lang.Math.round;

@Service
public class ConvertingLogicImpl implements ConvertingLogic {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, List<String>> getInfoFromCBR() throws Exception {
//        URL url = new URL("https://www.cbr.ru/scripts/XML_daily.asp");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document documentExchangeRates = builder.parse(connection.getInputStream());
        Document documentExchangeRates = builder.parse(new File("C:\\Users\\Vlaso\\IdeaProjects\\XML\\Today Rates.xml"));
        Document documentInfoForNames = builder.parse(new File("C:\\Users\\Vlaso\\IdeaProjects\\XML\\Info For Names.xml"));

        NodeList nodeListExchangeRates = documentExchangeRates.getChildNodes();
        Element elementExchangeRates;

        Map<String, List<String>> mapExchangeRates = new HashMap<>();
        String valuteId;
        String numCode;
        String name;
        double value;
        // Проходим по каждому элементу
        for(int i = 0; i < nodeListExchangeRates.getLength(); i++) {
            elementExchangeRates = (Element) nodeListExchangeRates.item(i);
            NodeList list = elementExchangeRates.getElementsByTagName("Valute");
            for (int j = 0; j < list.getLength(); j++) {
                List<String> currenciesInfo = new ArrayList<>();
                valuteId = elementExchangeRates.getElementsByTagName("Valute")
                        .item(j)
                        .getAttributes()
                        .getNamedItem("ID")
                        .getNodeValue();
                numCode = elementExchangeRates.getElementsByTagName("NumCode")
                        .item(j)
                        .getFirstChild()
                        .getNodeValue();
                name = evaluateXPath(documentInfoForNames, "/Valuta/Item[@ID='" + valuteId + "']/Name/text()").get(0);
//                name = elementExchangeRates.getElementsByTagName("Name")
//                        .item(j)
//                        .getFirstChild()
//                        .getNodeValue();
                value = Double.parseDouble(elementExchangeRates.getElementsByTagName("Value")
                        .item(j)
                        .getFirstChild()
                        .getNodeValue()
                        .replace(',', '.')) /
                        Integer.parseInt(elementExchangeRates.getElementsByTagName("Nominal")
                                .item(j)
                                .getFirstChild()
                                .getNodeValue());
                currenciesInfo.add(numCode);
                currenciesInfo.add(name);
                currenciesInfo.add(String.valueOf(value));
                mapExchangeRates.put(valuteId, currenciesInfo);
            }
        }

//        connection.disconnect();
        return mapExchangeRates;
    }

    @Override
    public double getConvertedValue(double inCourse, double outCourse, String inCurrencyValue) {
        double scale = Math.pow(10, 2);
        return round((inCourse * Double.parseDouble(inCurrencyValue) / outCourse) * scale) / scale;
    }

    @Override
    public Map<String, List<String>> stringToMapDeserialization(String exchangeRates) {
        TypeReference<Map<String, List<String>>> typeRef
                = new TypeReference<>() {};
        Map<String, List<String>> mapExchangeRates = null;
        try {
            mapExchangeRates = mapper.readValue(exchangeRates, typeRef);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        Map<String, Double> mapExchangeRates = Arrays.stream(exchangeRates.split(","))
//                .map(entry -> entry.split("="))
//                .collect(Collectors.toMap(entry -> entry[0].trim(), entry -> Double.parseDouble(entry[1])));
        return mapExchangeRates;
    }

    @Override
    public String mapToStringSerialization(Map<String, List<String>> mapExchangeRates) {

        String jsonResult = null;
        try {
            jsonResult = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(mapExchangeRates);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        System.out.println(mapExchangeRates.keySet());
//        String mapAsString = mapExchangeRates.keySet().stream()
//                .map(key -> key + "=" + mapExchangeRates.get(key))
//                .collect(Collectors.joining(","));
        return jsonResult;
    }


    private List<String> evaluateXPath(Document document, String xpathExpression)
    {
        // Create XPathFactory object
        XPathFactory xpathFactory = XPathFactory.newInstance();

        // Create XPath object
        XPath xpath = xpathFactory.newXPath();

        List<String> values = new ArrayList<>();
        try
        {
            // Create XPathExpression object
            XPathExpression expr = xpath.compile(xpathExpression);

            // Evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                values.add(nodes.item(i).getNodeValue());
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return values;
    }
}
