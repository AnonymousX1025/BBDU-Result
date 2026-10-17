package com.notanony.bbduresult;

import java.util.List;

public class ResultData {

    private final String university;
    private final String school;
    private final String course;
    private final String session;

    private final String name;
    private final String rollNumber;
    private final String examType;
    private final String fatherName;
    private final String enrollmentNumber;
    private final String status;
    private final String motherName;
    private final String branch;

    private final List<SemesterResult> semesters;

    private final String carryOverPapers;

    private final String overallResult;
    private final String overallObtained;
    private final String overallMax;

    private final String overallTheoryObtained;
    private final String overallTheoryMax;
    private final String overallPracticalObtained;
    private final String overallPracticalMax;

    private final String declarationDate;

    public ResultData(
            String university,
            String school,
            String course,
            String session,
            String name,
            String rollNumber,
            String examType,
            String fatherName,
            String enrollmentNumber,
            String status,
            String motherName,
            String branch,
            List<SemesterResult> semesters,
            String carryOverPapers,
            String overallResult,
            String overallObtained,
            String overallMax,
            String overallTheoryObtained,
            String overallTheoryMax,
            String overallPracticalObtained,
            String overallPracticalMax,
            String declarationDate
    ) {
        this.university = university;
        this.school = school;
        this.course = course;
        this.session = session;
        this.name = name;
        this.rollNumber = rollNumber;
        this.examType = examType;
        this.fatherName = fatherName;
        this.enrollmentNumber = enrollmentNumber;
        this.status = status;
        this.motherName = motherName;
        this.branch = branch;
        this.semesters = semesters;
        this.carryOverPapers = carryOverPapers;
        this.overallResult = overallResult;
        this.overallObtained = overallObtained;
        this.overallMax = overallMax;
        this.overallTheoryObtained = overallTheoryObtained;
        this.overallTheoryMax = overallTheoryMax;
        this.overallPracticalObtained = overallPracticalObtained;
        this.overallPracticalMax = overallPracticalMax;
        this.declarationDate = declarationDate;
    }

    public String getUniversity() {
        return university;
    }
    public String getSchool() {
        return school;
    }
    public String getCourse() {
        return course;
    }
    public String getSession() {
        return session;
    }

    public String getName() {
        return name;
    }
    public String getRollNumber() {
        return rollNumber;
    }
    public String getExamType() {
        return examType;
    }
    public String getFatherName() {
        return fatherName;
    }
    public String getEnrollmentNumber() {
        return enrollmentNumber;
    }
    public String getStatus() {
        return status;
    }
    public String getMotherName() {
        return motherName;
    }
    public String getBranch() {
        return branch;
    }

    public List<SemesterResult> getSemesters() {
        return semesters;
    }

    public String getCarryOverPapers() {
        return carryOverPapers;
    }

    public String getOverallResult() {
        return overallResult;
    }
    public String getOverallObtained() {
        return overallObtained;
    }
    public String getOverallMax() {
        return overallMax;
    }

    public String getOverallTheoryObtained() {
        return overallTheoryObtained;
    }
    public String getOverallTheoryMax() {
        return overallTheoryMax;
    }

    public String getOverallPracticalObtained() {
        return overallPracticalObtained;
    }
    public String getOverallPracticalMax() {
        return overallPracticalMax;
    }

    public String getDeclarationDate() {
        return declarationDate;
    }
}
