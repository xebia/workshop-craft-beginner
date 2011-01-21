package com.xebia.workshops;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    public List<String> parseTitles(File input) {
        List<String> titles = new ArrayList<String>();
        String fileContents = null;

        try {
            fileContents = FileUtils.readFileToString(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fileContents != null) {
            while (true) {
                int nextTitlePos = fileContents.indexOf("\"title\"");

                if (nextTitlePos < 0) {
                    break;
                }

                fileContents = fileContents.substring(nextTitlePos + 7);

                Pattern regex = Pattern.compile(":\"([^\"]*)\"(.*)", Pattern.DOTALL);

                Matcher matcher = regex.matcher(fileContents);

                if (matcher.matches()) {
                    titles.add(matcher.group(1));

                    fileContents = matcher.group(2);
                }
            }
        }

        return titles;
    }

    public List<Long> parseIds(File input) {
        List<Long> ids = new ArrayList<Long>();
        String fileContents = null;

        try {
            fileContents = FileUtils.readFileToString(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fileContents != null) {
            while (true) {
                int nextTitlePos = fileContents.indexOf("\"id\"");

                if (nextTitlePos < 0) {
                    break;
                }

                fileContents = fileContents.substring(nextTitlePos + 4);

                Pattern regex = Pattern.compile(":(\\d*)(.*)", Pattern.DOTALL);

                Matcher matcher = regex.matcher(fileContents);

                if (matcher.matches()) {
                    ids.add(Long.parseLong(matcher.group(1)));

                    fileContents = matcher.group(2);
                }
            }
        }

        return ids;
    }
}
