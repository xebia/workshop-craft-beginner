package com.xebia.workshops;


import java.io.*;

/**
 * Your mission, should you choose to accept it, is to finish and improve this code
 * until it is something to be proud of. Of course all the tests should stay green.
 *
 * Hints:
 *  - remove duplication
 *  - look at the separation of concerns
 *
 * If that is not a mission impossible, feel free to choose one or more of the
 * following extra tasks:
 *
 * - find corner cases that break this code and fix them
 * - add support for self-closing tags (<array/> for [], <object/> for {}},
 *   <item/> and <field_x/> for null values)
 * - refactor whitespace handling into a special Reader implementation (separation of concerns, remember)
 * - add some error reporting meta data to the above reader, like the current position,
 *   the last 10 chars read (and the next 10 chars ?) so exceptions can be more helpful.
 * - add support for escaping XML entities within strings using unicode sequences
 * - or add support for adding CDATA around strings containing characters not allowed in XML
 *
 * Or, if you think all of the above is peanuts:
 *
 * - extract a real JSON parser from this code that produces an AST hierarchy something like this:
 *
 *   JValue (abstract)
 *       JNull
 *       JBoolean
 *       JLong
 *       JDouble
 *       JString
 *       JArray
 *       JObject
 *       JField
 *
 *  ...and then rewrite the XML converter to use that AST and produce XML that shows the types as element attributes.
 *
 */
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
            } else if (c == 'n') {
                c = convertNull(reader, writer);
            } else if (Character.isDigit(c)) {
                c = convertNumber((char) c, reader, writer);
            } else if (Character.isWhitespace(c)) {
                c = reader.read();
            } else {
                throw new IllegalStateException("Unexpected character '" + (char) c + "' (" + c + ") found.");
            }
        }

        return c;
    }

    private int convertArray(Reader reader, Writer writer) throws IOException {
        writer.append("<array>");

        int c = reader.read();
        while (c != ']') {
            if (Character.isWhitespace(c)) {
                c = reader.read();
                continue;
            }

            writer.append("<item>");

            c = convert(c, reader, writer, ',', ']');

            writer.append("</item>");

            if (c == ',') {
                c = reader.read();
            }
        }

        writer.append("</array>");

        return reader.read(); // read pas the end of the array
    }

    private int convertObject(Reader reader, Writer writer) throws IOException {
        writer.append("<object>");

        int c = reader.read();

        while (c != '}') {
            if (Character.isWhitespace(c)) {
                c = reader.read();
                continue;
            }

            if (c != '"') {
                throw new IllegalStateException("An object field name should start with a '\"' not with a '" + (char) c + "'");
            }

            StringWriter fieldNameWriter = new StringWriter();

            c = convertString(reader, fieldNameWriter);

            writer.append("<").append(fieldNameWriter.toString()).append(">");

            while (Character.isWhitespace(c)) {
                c = reader.read();
            }

            if (c != ':') {
                throw new IllegalStateException("An object field should be separated by a ':', not by a '" + (char) c + "'");
            }

            c = reader.read(); // move past the :
            c = convert(c, reader, writer, ',', '}');

            writer.append("</").append(fieldNameWriter.toString()).append(">");

            if (c == ',') {
                c = reader.read();
            }
        }

        writer.append("</object>");

        return reader.read(); // read pas the end of the object
    }

    private int convertNumber(int c, Reader reader, Writer writer) throws IOException {
        while (c != ',' && c != ']' && c != '}') {
            if (!Character.isWhitespace(c)) {
                writer.write(c);
            }

            c = reader.read();
        }

        return c;
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

    private int convertNull(Reader reader, Writer writer) throws IOException {
        char u = (char) reader.read();
        char l1 = (char) reader.read();
        char l2 = (char) reader.read();

        if (u != 'u' || l1 != 'l' || l2 != 'l') {
            throw new IllegalStateException("Unexpected character. Expected 'null' but found 'n" + u + l1 + l2 + "'");
        }

        return reader.read(); // read past the end of the boolean
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
