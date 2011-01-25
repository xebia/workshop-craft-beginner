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

        return reader.read(); // read pas the end of the array
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
        char r = (char) reader.read();
        char u = (char) reader.read();
        char e = (char) reader.read();

        if (r != 'r' || u != 'u' || e != 'e') {
            throw new IllegalStateException("Unexpected character. Expected 'true' but found 't" + r + u + e + "'");
        }

        writer.append("true");

        return reader.read(); // read past the end of the boolean
    }

    private int convertFalse(Reader reader, Writer writer) throws IOException {
        char a = (char) reader.read();
        char l = (char) reader.read();
        char s = (char) reader.read();
        char e = (char) reader.read();

        if (a != 'a' || l != 'l' || s != 's' || e != 'e') {
            throw new IllegalStateException("Unexpected character. Expected 'false' but found 'f" + a + l + s + e + "'");
        }

        writer.append("false");

        return reader.read(); // read past the end of the boolean
    }

    private int convertString(Reader reader, Writer writer) throws IOException {
        int c = reader.read();

        while (c != '"') {
            writer.write(c);

            if (c == '\\') {
                // escape sequence detected. next character should not be interpreted.
                // WARNING: this code will go crazy for input containing illegal escape sequences, like a lone \ at the end of the string
                writer.write(reader.read());
            }

            c = reader.read();
        }

        return reader.read(); // read past the end of the string
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
