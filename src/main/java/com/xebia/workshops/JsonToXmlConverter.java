package com.xebia.workshops;


import java.io.*;
import java.util.Arrays;

public class JsonToXmlConverter {
    public String convert(String json) {
        Reader reader = new BufferedReader(new StringReader(json));
        final StringWriter stringWriter = new StringWriter();
        Writer writer = new BufferedWriter(stringWriter) {
            public String toString() {
                try {flush();} catch (IOException e) {}
                return stringWriter.toString();
            }
        };

        try {
            parseJson(reader, writer);

            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringWriter.toString();
    }

    private void parseJson(Reader reader, Writer writer) throws IOException {
        int c = reader.read();

        while (c != -1) {
            if (c == '[') {
                c = parseJsonArray(reader, writer);
            } else if (c == '{') {
                c = parseJsonObject(reader, writer);
            } else if (c == '"') {
                c = parseJsonString(reader, writer);
            } else if (c == 'f') {
                c = parseJsonFalse(reader, writer);
            } else if (c == 't') {
                c = parseJsonTrue(reader, writer);
            } else if (Character.isDigit(c)) {
                c = parseJsonNumber((char) c, reader, writer);
            } else if (Character.isWhitespace(c) || c == ']') {
                c = reader.read();
            } else {
                throw new IllegalStateException("Incorrect char -->" + (char) c + "<-- Written so far: " + writer.toString());
            }
        }
    }

    private char parseJsonNumber(char c, Reader reader, Writer writer) throws IOException {
        while (c != ',' && c != ']') {
            if (!Character.isWhitespace(c)) {
                writer.write(c);
            }

            c = (char) reader.read();
        }

        return (char) c;
    }

    private char parseJsonTrue(Reader reader, Writer writer) {
        throw new UnsupportedOperationException("parseJsonTrue");
    }

    private char parseJsonFalse(Reader reader, Writer writer) {
        throw new UnsupportedOperationException("parseJsonFalse");
    }

    private char parseJsonString(Reader reader, Writer writer) {
        throw new UnsupportedOperationException("parseJsonString");
    }

    private char parseJsonObject(Reader reader, Writer writer) {
        throw new UnsupportedOperationException("parseJsonObject");
    }

    private int parseJsonArray(Reader reader, Writer writer) throws IOException {
        writer.append("<array>");

        int c = parseJsonArrayItems(reader, writer);

        writer.append("</array>");

        c = reader.read(); // eat the last closing bracket

        return c;
    }

    private int parseJsonArrayItems(Reader reader, Writer writer) throws IOException {
        int c = reader.read();
        while (c != ']') {
            writer.append("<item>");

            c = parseJson(c, reader, writer, ']', ',');

            writer.append("</item>");

            if (c == ',') {
                c = reader.read();
            }
        }

        return c;
    }

    private int parseJson(int c, Reader reader, Writer writer, char... terminators) throws IOException {
        while (isNotIn((char) c, terminators)) {
            if (c == '[') {
                c = parseJsonArray(reader, writer);
            } else if (c == '{') {
                c = parseJsonObject(reader, writer);
            } else if (c == '"') {
                c = parseJsonString(reader, writer);
            } else if (c == 'f') {
                c = parseJsonFalse(reader, writer);
            } else if (c == 't') {
                c = parseJsonTrue(reader, writer);
            } else if (Character.isDigit(c)) {
                c = parseJsonNumber((char) c, reader, writer);
            } else if (Character.isWhitespace(c) || c == ']') {
                c = reader.read();
            } else {
                throw new IllegalStateException("Incorrect char -->" + (char) c + "<--");
            }
        }

        return c;
    }

    private boolean isNotIn(char c, char[] terminators) {
        for (char terminator : terminators) {
            if (terminator == c) {
                return false;
            }
        }

         return true;
    }


    public String oldConvert(String json) {
        StringBuilder xml = new StringBuilder(); //new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        if (json.trim().startsWith("[")) {
            if (json.trim().length() == 2) {
                xml.append("<array/>");
            } else {
                xml.append("<array>");

                String[] items = json.trim().substring(1, json.length() - 1).split(",");

                for (String item : items) {
                    xml.append("<item>");
                    xml.append(item.trim());
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
                    String[] splitPair = nameValuePair.trim().split("\"\\s*:");

                    xml.append("<" + splitPair[0].replaceAll("\"", "") + ">");
                    xml.append(splitPair[1].trim().replaceAll("\"", ""));
                    xml.append("</" + splitPair[0].replaceAll("\"", "") + ">");
                }

                xml.append("</object>");
            }
        }

        return xml.toString();
    }
}
