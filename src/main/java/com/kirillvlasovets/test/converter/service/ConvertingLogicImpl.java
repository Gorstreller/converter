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
import javax.xml.xpath.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.round;

@Service
public class ConvertingLogicImpl implements ConvertingLogic {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, List<String>> getInfoFromCBR() throws Exception {
        URL urlNames = new URL("https://cbr.ru/scripts/XML_val.asp?d=0");
        HttpURLConnection connectionNames = (HttpURLConnection) urlNames.openConnection();
        connectionNames.setRequestMethod("GET");

        URL urlRates = new URL("https://www.cbr.ru/scripts/XML_daily.asp");
        HttpURLConnection connectionRates = (HttpURLConnection) urlRates.openConnection();
        connectionRates.setRequestMethod("GET");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documentExchangeRates = builder.parse(connectionRates.getInputStream());
        Document documentInfoForNames = builder.parse(connectionNames.getInputStream());

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
                name = evaluateXPath(documentInfoForNames,
                        "/Valuta/Item[@ID='" + valuteId + "']/Name/text()").get(0);
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

        connectionNames.disconnect();
        connectionRates.disconnect();
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
        return jsonResult;
    }

    private List<String> evaluateXPath(Document document, String xpathExpression) {
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
