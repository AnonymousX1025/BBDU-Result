package com.notanony.bbduresult;

import java.util.List;

public class SemesterResult {
    private final String semester;
    private final List<SubjectResult> subjects;

    private final String totalObtained;
    private final String totalMax;

    private final String theoryCreditObtained;
    private final String theoryCreditMax;
    private final String practicalCreditObtained;
    private final String practicalCreditMax;

    private final String sgpa;

    public SemesterResult(
            String semester,
            List<SubjectResult> subjects,
            String totalObtained,
            String totalMax,
            String theoryCreditObtained,
            String theoryCreditMax,
            String practicalCreditObtained,
            String practicalCreditMax,
            String sgpa
    ) {
        this.semester = semester;
        this.subjects = subjects;
        this.totalObtained = totalObtained;
        this.totalMax = totalMax;
        this.theoryCreditObtained = theoryCreditObtained;
        this.theoryCreditMax = theoryCreditMax;
        this.practicalCreditObtained = practicalCreditObtained;
        this.practicalCreditMax = practicalCreditMax;
        this.sgpa = sgpa;
    }

    public String getSemester() {
        return semester;
    }

    public List<SubjectResult> getSubjects() {
        return subjects;
    }

    public String getTotalObtained() {
        return totalObtained;
    }
    public String getTotalMax() {
        return totalMax;
    }

    public String getTheoryCreditObtained() {
        return theoryCreditObtained;
    }
    public String getTheoryCreditMax() {
        return theoryCreditMax;
    }

    public String getPracticalCreditObtained() {
        return practicalCreditObtained;
    }
    public String getPracticalCreditMax() {
        return practicalCreditMax;
    }

    public String getSgpa() {
        return sgpa;
    }
}
