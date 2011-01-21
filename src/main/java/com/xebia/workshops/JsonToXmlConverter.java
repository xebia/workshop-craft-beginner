package com.xebia.workshops;


public class JsonToXmlConverter {
    public String convert(String json) {
        StringBuilder xml = new StringBuilder(); //new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        if (json.trim().startsWith("[")) {
            if (json.trim().length() == 2) {
                xml.append("<array/>");
            } else {
                xml.append("<array>");

                String[] items = json.trim().substring(1, json.length() - 1).split(",");

                for (String item : items) {
                    xml.append("<item>");
                    xml.append(item);
                    xml.append("</item>");
                }

                xml.append("</array>");
            }
        } else if (json.trim().startsWith("{")) {
            if (json.trim().length() == 2) {
                xml.append("<object/>");
            } else {
                xml.append("<object>");

                String[] nameValuePairs = json.trim().substring(1, json.length() - 1).split(",");

                for (String nameValuePair : nameValuePairs) {
                    String[] splitPair = nameValuePair.trim().split(":");

                    xml.append("<" + splitPair[0].replaceAll("\"", "") + ">");
                    xml.append(splitPair[1].replaceAll("\"", ""));
                    xml.append("</" + splitPair[0].replaceAll("\"", "") + ">");
                }

                xml.append("</object>");
            }
        }

        return xml.toString();
    }
}
