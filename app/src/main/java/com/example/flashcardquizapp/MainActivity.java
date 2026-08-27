package com.example.flashcardquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private TextView tvAnswer;
    private TextView tvAnswerLabel;
    private TextView tvCounter;

    private Button btnShowAnswer;
    private Button btnPrevious;
    private Button btnNext;
    private Button btnAdd;
    private Button btnEdit;
    private Button btnDelete;

    private ArrayList<Flashcard> flashcards;

    private int currentCard = 0;

    private boolean answerVisible = false;

    private static final int ADD_CARD_REQUEST = 100;
    private static final int EDIT_CARD_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Connect XML views
        tvQuestion = findViewById(R.id.tvQuestion);
        tvAnswer = findViewById(R.id.tvAnswer);
        tvAnswerLabel = findViewById(R.id.tvAnswerLabel);
        tvCounter = findViewById(R.id.tvCounter);

        btnShowAnswer = findViewById(R.id.btnShowAnswer);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        // Create flashcards
        flashcards = new ArrayList<>();

        flashcards.add(new Flashcard(
                "What is Java?",
                "Java is a high-level, object-oriented programming language."
        ));

        flashcards.add(new Flashcard(
                "What is Android?",
                "Android is an operating system mainly used for mobile devices."
        ));

        flashcards.add(new Flashcard(
                "What is OOP?",
                "OOP stands for Object-Oriented Programming."
        ));

        displayCard();

        // Show / Hide Answer
        btnShowAnswer.setOnClickListener(v -> {

            if (answerVisible) {
                hideAnswer();
            } else {
                showAnswer();
            }

        });

        // Next
        btnNext.setOnClickListener(v -> {

            if (currentCard < flashcards.size() - 1) {

                currentCard++;
                displayCard();

            } else {

                Toast.makeText(
                        MainActivity.this,
                        "You are already on the last card",
                        Toast.LENGTH_SHORT
                ).show();

            }

        });

        // Previous
        btnPrevious.setOnClickListener(v -> {

            if (currentCard > 0) {

                currentCard--;
                displayCard();

            } else {

                Toast.makeText(
                        MainActivity.this,
                        "You are already on the first card",
                        Toast.LENGTH_SHORT
                ).show();

            }

        });

        // Add
        btnAdd.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditFlashcardActivity.class
            );

            intent.putExtra("mode", "add");

            startActivityForResult(
                    intent,
                    ADD_CARD_REQUEST
            );

        });

        // Edit
        btnEdit.setOnClickListener(v -> {

            if (flashcards.isEmpty()) {
                return;
            }

            Flashcard selectedCard =
                    flashcards.get(currentCard);

            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditFlashcardActivity.class
            );

            intent.putExtra("mode", "edit");

            intent.putExtra(
                    "question",
                    selectedCard.getQuestion()
            );

            intent.putExtra(
                    "answer",
                    selectedCard.getAnswer()
            );

            intent.putExtra(
                    "position",
                    currentCard
            );

            startActivityForResult(
                    intent,
                    EDIT_CARD_REQUEST
            );

        });

        // Delete
        btnDelete.setOnClickListener(v -> {

            if (flashcards.isEmpty()) {
                return;
            }

            showDeleteDialog();

        });
    }

    private void displayCard() {

        if (flashcards.isEmpty()) {

            tvQuestion.setText("No flashcards available");

            tvAnswer.setText("");

            tvCounter.setText("0 cards");

            btnShowAnswer.setVisibility(View.GONE);

            btnEdit.setEnabled(false);

            btnDelete.setEnabled(false);

            return;
        }

        btnShowAnswer.setVisibility(View.VISIBLE);

        btnEdit.setEnabled(true);

        btnDelete.setEnabled(true);

        Flashcard card =
                flashcards.get(currentCard);

        tvQuestion.setText(
                card.getQuestion()
        );

        tvCounter.setText(
                "Card " +
                        (currentCard + 1) +
                        " of " +
                        flashcards.size()
        );

        hideAnswer();
    }

    private void showAnswer() {

        Flashcard card =
                flashcards.get(currentCard);

        tvAnswer.setText(
                card.getAnswer()
        );

        tvAnswer.setVisibility(View.VISIBLE);

        tvAnswerLabel.setVisibility(View.VISIBLE);

        btnShowAnswer.setText(
                "Hide Answer"
        );

        answerVisible = true;
    }

    private void hideAnswer() {

        tvAnswer.setVisibility(View.GONE);

        tvAnswerLabel.setVisibility(View.GONE);

        btnShowAnswer.setText(
                "Show Answer"
        );

        answerVisible = false;
    }

    private void showDeleteDialog() {

        new AlertDialog.Builder(this)

                .setTitle("Delete Flashcard")

                .setMessage(
                        "Are you sure you want to delete this flashcard?"
                )

                .setNegativeButton(
                        "Cancel",
                        null
                )

                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            flashcards.remove(
                                    currentCard
                            );

                            if (currentCard >= flashcards.size()) {

                                currentCard =
                                        flashcards.size() - 1;

                            }

                            if (currentCard < 0) {
                                currentCard = 0;
                            }

                            displayCard();

                            Toast.makeText(
                                    MainActivity.this,
                                    "Flashcard deleted",
                                    Toast.LENGTH_SHORT
                            ).show();

                        })

                .show();
    }

    // Receive Add / Edit result
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (resultCode != RESULT_OK ||
                data == null) {

            return;
        }

        String question =
                data.getStringExtra("question");

        String answer =
                data.getStringExtra("answer");

        String mode =
                data.getStringExtra("mode");

        // ADD
        if (requestCode == ADD_CARD_REQUEST &&
                "add".equals(mode)) {

            flashcards.add(
                    new Flashcard(
                            question,
                            answer
                    )
            );

            currentCard =
                    flashcards.size() - 1;

            displayCard();

            Toast.makeText(
                    this,
                    "Flashcard added successfully",
                    Toast.LENGTH_SHORT
            ).show();
        }

        // EDIT
        else if (requestCode == EDIT_CARD_REQUEST &&
                "edit".equals(mode)) {

            int position =
                    data.getIntExtra(
                            "position",
                            currentCard
                    );

            if (position >= 0 &&
                    position < flashcards.size()) {

                Flashcard card =
                        flashcards.get(position);

                card.setQuestion(question);

                card.setAnswer(answer);

                currentCard = position;

                displayCard();

                Toast.makeText(
                        this,
                        "Flashcard updated successfully",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}