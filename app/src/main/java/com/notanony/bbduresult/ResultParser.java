package com.notanony.bbduresult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public final class ResultParser {
    private ResultParser() {
    }

    public static ResultData parse(String html) {
        Document document = Jsoup.parse(html);

        String university = "";
        String school = "";
        String course = "";
        String session = "";

        String name = "";
        String rollNumber = "";
        String examType = "";
        String fatherName = "";
        String enrollmentNumber = "";
        String status = "";
        String motherName = "";
        String branch = "";

        Element header = document.selectFirst(".hdr");

        if (header != null) {
            Elements spans = header.select("span");

            if (!spans.isEmpty()) {
                university = spans.get(0).ownText().trim();
            }

            if (spans.size() > 1) {
                school = spans.get(1).text().trim();
            }

            if (spans.size() > 2) {
                course = spans.get(2).text().trim();
            }

            if (spans.size() > 3) {
                session = spans.get(3).text().trim();
            }
        }

        Elements paragraphs = document.select(".bd p");
        for (Element p : paragraphs) {
            String text = p.text().trim();

            Element bold = p.selectFirst("b");
            if (bold == null) {
                continue;
            }

            String value = bold.text().trim();
            String lower = text.toLowerCase();

            if (lower.startsWith("name:")) {
                name = value;
            } else if (lower.startsWith("roll no:")) {
                rollNumber = value;
            } else if (lower.startsWith("exam type:")) {
                examType = value;
            } else if (lower.startsWith("father name:")) {
                fatherName = value;
            } else if (lower.startsWith("enrollment no:")) {
                enrollmentNumber = value;
            } else if (lower.startsWith("status:")) {
                status = value;
            } else if (lower.startsWith("mother name:")) {
                motherName = value;
            } else if (lower.startsWith("branch:")) {
                branch = value;
            }
        }

        List<SemesterResult> semesters = new ArrayList<>();

        Elements semesterTables = document.select(
                ".tb-mrsheet > tbody > tr > td > table"
        );

        if (semesterTables.isEmpty()) {
            semesterTables = document.select(
                    "table.tb-mrsheet table.table-bordered"
            );
        }

        for (Element table : semesterTables) {
            Element semesterHeader = table.selectFirst(
                    "thead tr:first-child th"
            );

            if (semesterHeader == null) {
                continue;
            }

            String semester = semesterHeader.text().trim();
            if (!semester
                    .toLowerCase()
                    .contains("semester")) {
                continue;
            }

            List<SubjectResult> subjects = new ArrayList<>();

            String totalObtained = "";
            String totalMax = "";

            String theoryCreditObtained = "";
            String theoryCreditMax = "";
            String practicalCreditObtained = "";
            String practicalCreditMax = "";

            String sgpa = "";

            Elements rows = table.select("tbody tr");

            for (Element row : rows) {
                Elements cells = row.select("> td");
                String rowText = row.text().trim().toLowerCase();

                if (cells.size() == 9) {
                    String first = cell(cells, 0);

                    if (first.equalsIgnoreCase("Total")) {
                        totalObtained = cell(cells, 5);
                        totalMax = cell(cells, 6);
                        continue;
                    }

                    if (first.equals("-") || first.isEmpty()) {
                        continue;
                    }

                    subjects.add(
                            new SubjectResult(
                                    cell(cells, 0),
                                    cell(cells, 1),
                                    cell(cells, 2),
                                    cell(cells, 3),
                                    cell(cells, 4),
                                    cell(cells, 5),
                                    cell(cells, 6),
                                    cell(cells, 7),
                                    cell(cells, 8)
                            )
                    );
                    continue;
                }

                if (cells.size() >= 5) {
                    String lastCell = cell(
                            cells,
                            cells.size() - 1
                    );

                    if (lastCell.matches("\\d+(\\.\\d+)?")) {
                        String rowLower = rowText.toLowerCase();

                        if (rowLower.matches(
                                ".*\\d+.*\\d+.*\\d+.*\\d+.*"
                        )) {
                            theoryCreditObtained = cell(cells, 0);
                            theoryCreditMax = cell(cells, 1);
                            practicalCreditObtained = cell(cells, 2);
                            practicalCreditMax = cell(cells, 3);
                            sgpa = lastCell;
                        }
                    }
                }
            }

            semesters.add(
                    new SemesterResult(
                            semester,
                            subjects,
                            totalObtained,
                            totalMax,
                            theoryCreditObtained,
                            theoryCreditMax,
                            practicalCreditObtained,
                            practicalCreditMax,
                            sgpa
                    )
            );
        }

        String carryOverPapers = "";
        Elements carryOverLabels = document.select("span");

        for (Element span : carryOverLabels) {
            String label = span.text().trim();

            if (label.equalsIgnoreCase(
                    "Carry Over Paper(s):"
            )) {
                Element parent = span.parent();

                if (parent != null) {
                    carryOverPapers = parent.text().trim().replace(
                            "Carry Over Paper(s):", ""
                    ).trim();
                }
                break;
            }
        }

        String overallResult = "";
        String overallObtained = "";
        String overallMax = "";

        String overallTheoryObtained = "";
        String overallTheoryMax = "";

        String overallPracticalObtained = "";
        String overallPracticalMax = "";

        Elements overallTables = document.select(
                "table.tb-mrsheet table.table-bordered"
        );

        for (Element table : overallTables) {
            Element firstHeader = table.selectFirst("thead th");
            if (firstHeader == null ||
                    !firstHeader.text().trim()
                            .equalsIgnoreCase("Result")
            ) {
                continue;
            }

            Element resultRow = table.selectFirst("tbody tr");
            if (resultRow != null) {
                Elements cells = resultRow.children();

                if (cells.size() >= 3) {
                    overallResult = cells.get(0).text().trim();
                    overallObtained = cells.get(1).text().trim();
                    overallMax = cells.get(2).text().trim();
                }
            }

            Element outerRow = table.parent();
            if (outerRow != null) {
                Element outerTable = outerRow.parent();

                if (outerTable != null) {
                    Elements creditTables = outerTable.select(
                            "table.table-bordered"
                    );

                    for (Element creditTable : creditTables) {
                        Element creditHeader = creditTable.selectFirst("thead th");

                        if (creditHeader == null ||
                                !creditHeader.text().trim()
                                        .equalsIgnoreCase("Credit")) {
                            continue;
                        }

                        Elements creditRows = creditTable.select("tbody tr");
                        for (Element creditRow : creditRows) {
                            Elements creditCells = creditRow.children();

                            if (creditCells.size() < 3) {
                                continue;
                            }

                            String creditType = creditCells.get(0).text().trim();

                            if (creditType.equalsIgnoreCase("Theory")) {
                                overallTheoryObtained = creditCells.get(1).text().trim();
                                overallTheoryMax = creditCells.get(2).text().trim();

                            } else if (creditType.equalsIgnoreCase("Practical")) {
                                overallPracticalObtained = creditCells.get(1).text().trim();
                                overallPracticalMax = creditCells.get(2).text().trim();
                            }
                        }

                        break;
                    }
                }
            }
            break;
        }

        String declarationDate = "";
        Elements divs = document.select("div");

        for (Element div : divs) {
            String ownText = div.ownText().trim();
            String prefix = "Date of Declaration of Result:";

            if (ownText.startsWith(prefix)) {
                declarationDate = ownText.substring(prefix.length()).trim();
                break;
            }
        }

        return new ResultData(
                university,
                school,
                course,
                session,
                name,
                rollNumber,
                examType,
                fatherName,
                enrollmentNumber,
                status,
                motherName,
                branch,
                semesters,
                carryOverPapers,
                overallResult,
                overallObtained,
                overallMax,
                overallTheoryObtained,
                overallTheoryMax,
                overallPracticalObtained,
                overallPracticalMax,
                declarationDate
        );
    }

    private static String cell(
            Elements cells,
            int index
    ) {
        if (index < 0 || index >= cells.size()) {
            return "";
        }
        return cells.get(index).text().trim();
    }
}
