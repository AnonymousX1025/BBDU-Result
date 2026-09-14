package com.notanony.bbduresult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ResultParser {

    public static List<ResultEntry> parseResultList(String html) {

        List<ResultEntry> results = new ArrayList<>();

        Document document = Jsoup.parse(html);

        Elements forms = document.select(
                "form[action='result-display.php']"
        );

        for (Element form : forms) {

            String session = getValue(form, "session");
            String year = getValue(form, "year");
            String rollNumber = getValue(form, "urollno");
            String semester = getValue(form, "semester");
            String examType = getValue(form, "examtype");
            String status = getValue(form, "status");

            results.add(
                    new ResultEntry(
                            session,
                            year,
                            rollNumber,
                            semester,
                            examType,
                            status
                    )
            );
        }

        return results;
    }

    private static String getValue(Element form, String name) {

        Element input = form.selectFirst(
                "input[name=" + name + "]"
        );

        if (input == null) {
            return "";
        }

        return input.attr("value");
    }
}
