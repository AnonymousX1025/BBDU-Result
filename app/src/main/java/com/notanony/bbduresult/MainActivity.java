package com.notanony.bbduresult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.textfield.TextInputEditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText rollNumber;
    private MaterialButton getResult;
    private ResultApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rollNumber = findViewById(R.id.urollno);
        getResult = findViewById(R.id.getResult);
        api = RetrofitClient.getApi();

        getResult.setOnClickListener(v -> {
            String roll_no = "";

            if (rollNumber.getText() != null) {
                roll_no = rollNumber.getText()
                        .toString()
                        .trim();
            }

            if (roll_no.isEmpty()) {
                rollNumber.setError("Enter your roll number");
                rollNumber.requestFocus();
                return;
            }

            if (roll_no.length() != 10) {
                rollNumber.setError("Enter a valid 10-digit roll number");
                rollNumber.requestFocus();
                return;
            }

            getResultList(roll_no);
        });
    }

    private void getResultList(String roll) {
        setLoading(true);

        api.getResultList(roll).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<String> call,
                    @NonNull Response<String> response
            ) {
                setLoading(false);

                if (!response.isSuccessful()) {
                    showToast("Server error: " + response.code());
                    return;
                }

                String html = response.body();
                if (html == null || html.trim().isEmpty()) {
                    showToast("No result found");
                    return;
                }

                List<ResultEntry> results = parseResultList(html);
                if (results.isEmpty()) {
                    showToast("Result not found!");
                    return;
                }

                showResultSelection(results);
            }

            @Override
            public void onFailure(
                    @NonNull Call<String> call,
                    @NonNull Throwable t
            ) {
                setLoading(false);

                Log.e(
                        "BBDU_RESULT",
                        "Request failed",
                        t
                );
                showToast(
                        "Network error: " + t.getMessage()
                );
            }
        });
    }

    private List<ResultEntry> parseResultList(String html) {

        List<ResultEntry> results = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements rows = document.select("table.table tbody tr");

        for (Element row : rows) {
            String session = getInputValue(row, "session");
            String year = getInputValue(row, "year");
            String roll = getInputValue(row, "urollno");
            String semester = getInputValue(row, "semester");
            String examType = getInputValue(row, "examtype");
            String status = getInputValue(row, "status");

            if (session.isEmpty() || semester.isEmpty()) {
                continue;
            }

            ResultEntry entry = new ResultEntry(
                    session,
                    year,
                    roll,
                    semester,
                    examType,
                    status
            );
            results.add(entry);
        }
        return results;
    }

    private String getInputValue(
            Element element,
            String name
    ) {
        Element input = element.selectFirst(
                "input[name=" + name + "]"
        );

        if (input == null) {
            return "";
        }
        return input.attr("value").trim();
    }

    private int dpToPx() {
        return (int) (
                4 * getResources()
                        .getDisplayMetrics()
                        .density
        );
    }

    private void showResultSelection(List<ResultEntry> results) {
        results.sort(
                Comparator.comparingInt(
                        (ResultEntry result) ->
                                Integer.parseInt(result.getSemester())
                ).thenComparing(
                        ResultEntry::getSession
                )
        );

        View dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_result_selection, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        LinearLayout semesterContainer =
                dialogView.findViewById(R.id.semesterContainer);

        MaterialButton cancelButton =
                dialogView.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        Map<String, List<ResultEntry>> groupedResults =
                new LinkedHashMap<>();

        for (ResultEntry result : results) {
            String key = result.getSemester()
                    + "_"
                    + result.getSession();

            groupedResults.
                    computeIfAbsent(
                            key,
                            k -> new ArrayList<>()
                    )
                    .add(result);
        }

        for (List<ResultEntry> semesterResults : groupedResults.values()) {
            android.view.View semesterView =
                    getLayoutInflater()
                            .inflate(
                                    R.layout.item_semester_result,
                                    semesterContainer,
                                    false
                            );

            MaterialTextView semesterText =
                    semesterView.findViewById(
                            R.id.semesterText
                    );
            MaterialTextView sessionText =
                    semesterView.findViewById(
                            R.id.sessionText
                    );
            LinearLayout optionsContainer =
                    semesterView.findViewById(
                            R.id.optionsContainer
                    );

            ResultEntry first = semesterResults.get(0);
            semesterText.setText(
                    getString(
                            R.string.semester,
                            first.getSemester()
                    )
            );

            sessionText.setText(
                    String.format(
                            "(%s)",
                            first.getSession()
                    )
            );

            for (ResultEntry result : semesterResults) {
                MaterialButton button = getMaterialButton(result, dialog);

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1
                        );

                params.setMargins(
                        dpToPx(),
                        0,
                        dpToPx(),
                        0
                );

                optionsContainer.addView(
                        button,
                        params
                );
            }

            semesterContainer.addView(semesterView);
        }

        dialog.show();
        Window window = dialog.getWindow();

        if (window != null) {
            window.setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.90),
                    (int) (getResources().getDisplayMetrics().heightPixels * 0.80)
            );
        }
    }

    @NonNull
    private MaterialButton getMaterialButton(ResultEntry result, AlertDialog dialog) {
        MaterialButton button =
                new MaterialButton(
                        this,
                        null,
                        com.google.android.material.R.attr
                                .materialButtonOutlinedStyle
                );

        String buttonText = result.getExamType()
                + " • "
                + result.getStatus();

        button.setText(buttonText);
        button.setTextSize(12);
        button.setAllCaps(false);

        button.setOnClickListener(v -> {
            dialog.dismiss();
            getResultDetails(result);
        });

        return button;
    }

    private void openResultScreen(String html) {
        Intent intent = new Intent(
                MainActivity.this,
                ResultActivity.class
        );
        intent.putExtra("result_html", html);
        startActivity(intent);
    }

    private void getResultDetails(
            ResultEntry result
    ) {
        setLoading(true);

        api.getResultDetails(
                result.getSession(),
                result.getYear(),
                result.getRollNumber(),
                result.getSemester(),
                result.getExamType(),
                result.getStatus()

        ).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<String> call,
                    @NonNull Response<String> response
            ) {
                setLoading(false);

                if (!response.isSuccessful()) {
                    showToast(
                            "Server error: " + response.code()
                    );
                    return;
                }

                String html = response.body();
                if (html == null || html.trim().isEmpty()) {
                    showToast("Result data is empty");
                    return;
                }
                openResultScreen(response.body());
            }

            @Override
            public void onFailure(
                    @NonNull Call<String> call,
                    @NonNull Throwable t
            ) {
                setLoading(false);

                Log.e(
                        "BBDU_RESULT_DETAILS",
                        "Request failed",
                        t
                );
                showToast(
                        "Network error: " + t.getMessage()
                );
            }
        });
    }

    private void setLoading(boolean loading) {
        if (loading) {
            getResult.setEnabled(false);
            getResult.setText(R.string.loading);
        } else {
            getResult.setEnabled(true);
            getResult.setText(R.string.get_result);
        }
    }

    private void showToast(String message) {
        Toast.makeText(
                MainActivity.this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }
}
