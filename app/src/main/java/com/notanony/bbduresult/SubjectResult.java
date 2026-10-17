package com.notanony.bbduresult;

public class SubjectResult {
    private final String subjectCode;
    private final String eseObtained;
    private final String eseMax;
    private final String ciaObtained;
    private final String ciaMax;
    private final String totalObtained;
    private final String totalMax;
    private final String credit;
    private final String grade;

    public SubjectResult(
            String subjectCode,
            String eseObtained,
            String eseMax,
            String ciaObtained,
            String ciaMax,
            String totalObtained,
            String totalMax,
            String credit,
            String grade
    ) {
        this.subjectCode = subjectCode;
        this.eseObtained = eseObtained;
        this.eseMax = eseMax;
        this.ciaObtained = ciaObtained;
        this.ciaMax = ciaMax;
        this.totalObtained = totalObtained;
        this.totalMax = totalMax;
        this.credit = credit;
        this.grade = grade;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getEseObtained() {
        return eseObtained;
    }
    public String getEseMax() {
        return eseMax;
    }

    public String getCiaObtained() {
        return ciaObtained;
    }
    public String getCiaMax() {
        return ciaMax;
    }

    public String getTotalObtained() {
        return totalObtained;
    }
    public String getTotalMax() {
        return totalMax;
    }

    public String getCredit() {
        return credit;
    }

    public String getGrade() {
        return grade;
    }
}
