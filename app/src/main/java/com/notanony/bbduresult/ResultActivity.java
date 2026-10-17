package com.notanony.bbduresult;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private LinearLayout semesterContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        semesterContainer = findViewById(R.id.semesterContainer);

        String html = getIntent().getStringExtra("result_html");
        if (html == null || html.isEmpty()) {
            finish();
            return;
        }

        ResultData result = ResultParser.parse(html);
        displayResult(result);
    }

    private void displayResult(ResultData result) {

        TextView universityText = findViewById(R.id.universityText);
        TextView schoolText = findViewById(R.id.schoolText);
        TextView courseText = findViewById(R.id.courseText);
        TextView sessionText = findViewById(R.id.sessionText);
        TextView nameText = findViewById(R.id.nameText);
        TextView rollText = findViewById(R.id.rollText);
        TextView fatherText = findViewById(R.id.fatherText);
        TextView enrollmentText = findViewById(R.id.enrollmentText);
        TextView motherText = findViewById(R.id.motherText);
        TextView branchText = findViewById(R.id.branchText);
        TextView examTypeText = findViewById(R.id.examTypeText);
        TextView statusText = findViewById(R.id.statusText);

        universityText.setText(result.getUniversity());
        schoolText.setText(result.getSchool());
        courseText.setText(result.getCourse());
        sessionText.setText(result.getSession());

        nameText.setText(getString(
                R.string.student_name, result.getName()
        ));
        rollText.setText(getString(
                R.string.roll_no, result.getRollNumber()
        ));
        fatherText.setText(getString(
                R.string.father_name, result.getFatherName()
        ));
        enrollmentText.setText(getString(
                R.string.enroll_no, result.getEnrollmentNumber()
        ));
        motherText.setText(getString(
                R.string.mother_name, result.getMotherName()
        ));
        branchText.setText(getString(
                R.string.branch, result.getBranch()
        ));
        examTypeText.setText(getString(
                R.string.exam_type, result.getExamType()
        ));
        statusText.setText(getString(
                R.string.status, result.getStatus()
        ));

        displaySemesters(result.getSemesters());

        String carryOvers = result.getCarryOverPapers();
        View carryoverCard = findViewById(R.id.carryOverCard);
        TextView carryOverText = findViewById(R.id.carryOverText);

        if (carryOvers == null
                || carryOvers.replace(",", "").isBlank()) {
            carryoverCard.setVisibility(View.GONE);
        } else {
            carryOverText.setText(
                    result.getCarryOverPapers()
            );
        }

        View overallCard = findViewById(R.id.overallCard);
        String overallRes = result.getOverallResult();
        if (overallRes == null || overallRes.isBlank()) {
            overallCard.setVisibility(View.GONE);
        } else {
            TextView overallMarksText = findViewById(R.id.overallMarksText);
            overallMarksText.setText(
                    getString(
                            R.string.overall_marks,
                            result.getOverallResult(),
                            result.getOverallObtained(),
                            result.getOverallMax()
                    )
            );

            TextView overallTheoryText = findViewById(R.id.overallTheoryText);
            overallTheoryText.setText(
                    getString(
                            R.string.overall_theory,
                            result.getOverallTheoryObtained(),
                            result.getOverallTheoryMax()
                    )
            );

            TextView overallPracticalText = findViewById(R.id.overallPracticalText);
            overallPracticalText.setText(
                    getString(
                            R.string.overall_practical,
                            result.getOverallPracticalObtained(),
                            result.getOverallPracticalMax()
                    )
            );
        }

        TextView declaration = findViewById(R.id.declarationText);
        declaration.setText(getString(
                R.string.declaration_date,
                result.getDeclarationDate()
        ));
    }

    private void displaySemesters(
            List<SemesterResult> semesters
    ) {

        for (SemesterResult semester : semesters) {
            android.view.View semesterView =
                    getLayoutInflater()
                            .inflate(
                                    R.layout.item_result_semester,
                                    semesterContainer,
                                    false
                            );

            TextView semesterTitle = semesterView.findViewById(R.id.semesterTitle);
            TableLayout subjectTable = semesterView.findViewById(R.id.subjectTable);
            TextView totalText = semesterView.findViewById(R.id.totalText);
            TextView theoryCredit = semesterView.findViewById(R.id.theoryCredit);
            TextView practicalCredit = semesterView.findViewById(R.id.practicalCredit);
            TextView sgpaText = semesterView.findViewById(R.id.sgpaText);

            semesterTitle.setText(semester.getSemester());

            addHeaderRow(subjectTable);

            for (SubjectResult subject : semester.getSubjects()) {
                addSubjectRow(
                        subjectTable,
                        subject
                );
            }

            theoryCredit.setText(
                    Html.fromHtml(
                            getString(
                                    R.string.theory_credit,
                                    semester.getTheoryCreditObtained(),
                                    semester.getTheoryCreditMax()
                            ),
                            Html.FROM_HTML_MODE_COMPACT
                    )
            );
            practicalCredit.setText(
                    Html.fromHtml(
                            getString(
                                    R.string.practical_credit,
                                    semester.getPracticalCreditObtained(),
                                    semester.getPracticalCreditMax()
                            ),
                            Html.FROM_HTML_MODE_COMPACT
                    )
            );

            totalText.setText(
                    Html.fromHtml(
                            getString(
                                    R.string.semester_total,
                                    semester.getTotalObtained(),
                                    semester.getTotalMax()
                            ),
                            Html.FROM_HTML_MODE_COMPACT
                    )
            );
            sgpaText.setText(
                    Html.fromHtml(
                            getString(
                                    R.string.semester_sgpa,
                                    semester.getSgpa()
                            ),
                            Html.FROM_HTML_MODE_COMPACT
                    )
            );

            semesterContainer.addView(semesterView);
        }
    }

    private void addHeaderRow(
            TableLayout table
    ) {
        TableRow row = new TableRow(this);
        String[] headers = {
                "Subject",
                "ESE",
                "CIA",
                "Total",
                "Credit",
                "Grade"
        };

        for (String header : headers) {
            TextView text = createCell(header);
            text.setTypeface(
                    null,
                    Typeface.BOLD
            );
            row.addView(text);
        }
        table.addView(row);
    }

    private void addSubjectRow(
            TableLayout table,
            SubjectResult subject
    ) {
        TableRow row = new TableRow(this);

        String ese = subject.getEseObtained()
                + "/"
                + subject.getEseMax();
        String cia = subject.getCiaObtained()
                + "/"
                + subject.getCiaMax();
        String total = subject.getTotalObtained()
                + "/"
                + subject.getTotalMax();

        if (ese.strip().equals("/")) {
            ese = "-";
        }

        String[] values = {
                subject.getSubjectCode(),
                ese,
                cia,
                total,
                subject.getCredit(),
                subject.getGrade()
        };

        for (String value : values) {
            row.addView(
                    createCell(value)
            );
        }
        table.addView(row);
    }

    private TextView createCell(
            String text
    ) {
        TextView cell = new TextView(this);

        cell.setText(text);
        cell.setTextSize(13);
        cell.setGravity(Gravity.CENTER);
        cell.setPadding(
                dp(12),
                dp(10),
                dp(12),
                dp(10)
        );

        return cell;
    }

    private int dp(int value) {
        return (int) (
                value
                        * getResources()
                        .getDisplayMetrics()
                        .density
        );
    }
}
