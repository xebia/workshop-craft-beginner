package com.xebia.workshops;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

/**
 * @author iwein
 */
public class JsonLibJsonToXmlConverter {
    private final String json;

    public JsonLibJsonToXmlConverter(String json) {
        this.json = json;
    }

    public String asXml() {
        XMLSerializer serializer = new XMLSerializer();
        JSON json = JSONSerializer.toJSON(this.json);
        return replaceElements(serializer.write(json));
    }

    private String replaceElements(String jsonLibStyleXml) {
        /*
        This is butt ugly, and if you want to do this properly you should be looking at something like xslt
        but hey, I got the general idea working in 10 minutes. Just proving a point here :)
         */
        return jsonLibStyleXml
                .replaceAll("<a/>", "<array/>")
                .replaceAll("<a>", "<array>")
                .replaceAll("</a>", "</array>")
                .replaceAll("<e [^>]*>", "<item>")
                .replaceAll("</e>", "</item>");
    }
}
