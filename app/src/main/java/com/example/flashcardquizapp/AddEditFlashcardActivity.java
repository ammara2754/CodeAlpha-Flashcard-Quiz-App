package com.example.flashcardquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditFlashcardActivity extends AppCompatActivity {

    private EditText etQuestion;
    private EditText etAnswer;

    private TextView tvScreenTitle;

    private Button btnSave;
    private Button btnCancel;

    private String mode;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_flashcard);

        // Connect views
        tvScreenTitle = findViewById(R.id.tvScreenTitle);

        etQuestion = findViewById(R.id.etQuestion);
        etAnswer = findViewById(R.id.etAnswer);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Get mode
        mode = getIntent().getStringExtra("mode");

        // Get position of the card being edited
        position = getIntent().getIntExtra("position", -1);

        // Check whether we are adding or editing
        if ("edit".equals(mode)) {

            tvScreenTitle.setText("Edit Flashcard");
            btnSave.setText("Update Flashcard");

            String question =
                    getIntent().getStringExtra("question");

            String answer =
                    getIntent().getStringExtra("answer");

            etQuestion.setText(question);
            etAnswer.setText(answer);

        } else {

            tvScreenTitle.setText("Add Flashcard");
            btnSave.setText("Save Flashcard");
        }

        // Save / Update button
        btnSave.setOnClickListener(v -> {

            String question =
                    etQuestion.getText().toString().trim();

            String answer =
                    etAnswer.getText().toString().trim();

            // Validate question
            if (question.isEmpty()) {

                etQuestion.setError(
                        "Please enter a question"
                );

                etQuestion.requestFocus();

                return;
            }

            // Validate answer
            if (answer.isEmpty()) {

                etAnswer.setError(
                        "Please enter an answer"
                );

                etAnswer.requestFocus();

                return;
            }

            // Create result
            Intent resultIntent = new Intent();

            resultIntent.putExtra(
                    "question",
                    question
            );

            resultIntent.putExtra(
                    "answer",
                    answer
            );

            resultIntent.putExtra(
                    "position",
                    position
            );

            // Tell MainActivity whether this is Add or Edit
            if ("edit".equals(mode)) {

                resultIntent.putExtra(
                        "mode",
                        "edit"
                );

            } else {

                resultIntent.putExtra(
                        "mode",
                        "add"
                );
            }

            // Send result back
            setResult(
                    RESULT_OK,
                    resultIntent
            );

            Toast.makeText(
                    AddEditFlashcardActivity.this,
                    "Flashcard saved successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });

        // Cancel button
        btnCancel.setOnClickListener(v -> finish());
    }
}