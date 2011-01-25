package com.xebia.workshops;


import java.io.*;

public class JsonToXmlConverter {
    public String convert(String json) {
        Reader reader = new BufferedReader(new StringReader(json));
        Writer stringWriter = new StringWriter();
        Writer writer = new BufferedWriter(stringWriter);

        try {
            convert(reader, writer);

            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringWriter.toString();
    }

    private void convert(Reader reader, Writer writer) throws IOException {
        int c = reader.read();

        convert(c, reader, writer, -1);
    }

    private int convert(int c, Reader reader, Writer writer, int... terminators) throws IOException {
        while (isNotIn(c, terminators)) {
            if (c == '[') {
                c = convertArray(reader, writer);
            } else if (c == '{') {
                c = convertObject(reader, writer);
            } else if (c == '"') {
                c = convertString(reader, writer);
            } else if (c == 'f') {
                c = convertFalse(reader, writer);
            } else if (c == 't') {
                c = convertTrue(reader, writer);
            } else if (Character.isDigit(c)) {
                c = convertNumber((char) c, reader, writer);
            } else if (Character.isWhitespace(c) || c == ']') {
                c = reader.read();
            } else {
                throw new IllegalStateException("Unexpected character '" + (char) c + "' found.");
            }
        }

        return c;
    }

    private int convertArray(Reader reader, Writer writer) throws IOException {
        writer.append("<array>");

        convertArrayItems(reader, writer);

        writer.append("</array>");

        return reader.read(); // eat the last closing bracket
    }

    private int convertArrayItems(Reader reader, Writer writer) throws IOException {
        int c = reader.read();
        while (c != ']') {
            writer.append("<item>");

            c = convert(c, reader, writer, ']', ',');

            writer.append("</item>");

            if (c == ',') {
                c = reader.read();
            }
        }

        return c;
    }

    private int convertNumber(int c, Reader reader, Writer writer) throws IOException {
        while (c != ',' && c != ']') {
            if (!Character.isWhitespace(c)) {
                writer.write(c);
            }

            c = reader.read();
        }

        return c;
    }

    private int convertTrue(Reader reader, Writer writer) throws IOException {
        int r = reader.read();
        int u = reader.read();
        int e = reader.read();

        if (r != 'r' || u != 'u' || e != 'e') {
            throw new IllegalStateException("Unexpected character. Expected 'true' but found 't" + r + u + e + "'");
        }

        writer.append("true");

        return reader.read();
    }

    private int convertFalse(Reader reader, Writer writer) throws IOException {
        int a = reader.read();
        int l = reader.read();
        int s = reader.read();
        int e = reader.read();

        if (a != 'a' || l != 'l' || s != 's' || e != 'e') {
            throw new IllegalStateException("Unexpected character. Expected 'false' but found 'f" + a + l + s + e + "'");
        }

        writer.append("false");

        return reader.read();
    }

    private char convertString(Reader reader, Writer writer) {
        throw new UnsupportedOperationException("convertString");
    }

    private char convertObject(Reader reader, Writer writer) {
        throw new UnsupportedOperationException("convertObject");
    }

    private boolean isNotIn(int c, int[] terminators) {
        for (int terminator : terminators) {
            if (terminator == c) {
                return false;
            }
        }

         return true;
    }
}
