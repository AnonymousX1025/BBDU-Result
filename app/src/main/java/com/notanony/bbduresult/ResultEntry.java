package com.notanony.bbduresult;

public class ResultEntry {

    private String session;
    private String year;
    private String rollNumber;
    private String semester;
    private String examType;
    private String status;

    public ResultEntry(
            String session,
            String year,
            String rollNumber,
            String semester,
            String examType,
            String status
    ) {
        this.session = session;
        this.year = year;
        this.rollNumber = rollNumber;
        this.semester = semester;
        this.examType = examType;
        this.status = status;
    }

    public String getSession() {
        return session;
    }

    public String getYear() {
        return year;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getSemester() {
        return semester;
    }

    public String getExamType() {
        return examType;
    }

    public String getStatus() {
        return status;
    }
}
