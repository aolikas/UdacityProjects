package com.example.buran.myqapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
        int mScore;

        RadioGroup radioGroup2, radioGroup3,
                radioGroup4, radioGroup5;

        EditText questionEditText;

        CheckBox checkBox1Q1, checkBox2Q1, checkBox3Q1, checkBox4Q1;

        Button submitButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            radioGroup2 = (RadioGroup) findViewById(R.id.radio_group_question_2);
            radioGroup3 = (RadioGroup) findViewById(R.id.radio_group_question_3);
            radioGroup4 = (RadioGroup) findViewById(R.id.radio_group_question_4);
            radioGroup5 = (RadioGroup) findViewById(R.id.radio_group_question_5);

            checkBox1Q1 = (CheckBox) findViewById(R.id.check_box_1);
            checkBox2Q1 = (CheckBox) findViewById(R.id.check_box_2);
            checkBox3Q1 = (CheckBox) findViewById(R.id.check_box_3);
            checkBox4Q1 = (CheckBox) findViewById(R.id.check_box_4);

            questionEditText = (EditText) findViewById(R.id.edit_text_answer);
            submitButton = (Button) findViewById(R.id.submit_button);
            submitButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    RadioButton correctAnswerQ2 = (RadioButton) findViewById(R.id.rb_4_question_2);
                    if (correctAnswerQ2.isChecked()) {
                        mScore += 1;
                    }
                    RadioButton correctAnswerQ3 = (RadioButton) findViewById(R.id.rb_2_question_3);
                    if (correctAnswerQ3.isChecked()) {
                        mScore += 1;
                    }

                    RadioButton correctAnswerQ4 = (RadioButton) findViewById(R.id.rb_4_question_4);
                    if (correctAnswerQ4.isChecked()) {
                        mScore += 1;
                    }

                    RadioButton correctAnswerQ5 = (RadioButton) findViewById(R.id.rb_3_question_5);
                    if (correctAnswerQ5.isChecked()) {
                        mScore += 1;
                    }

                    if (questionEditText.getText().length() != 0) {
                        if (questionEditText.getText().toString().trim().equalsIgnoreCase("p")) {
                            mScore += 1;
                            Toast.makeText(getApplicationContext(), "Your total score is " + mScore, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Your total score is " + mScore, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please write an answer", Toast.LENGTH_SHORT).show();
                    }
                    mScore = 0;
                }
            });
        }

        public void answerQuestion1(View view) {
            boolean hasCheckBox1 = checkBox1Q1.isChecked();
            boolean hasCheckBox2 = checkBox2Q1.isChecked();
            boolean hasCheckBox3 = checkBox3Q1.isChecked();
            boolean hasCheckBox4 = checkBox4Q1.isChecked();

            if (hasCheckBox1 && hasCheckBox2 && !hasCheckBox3 && hasCheckBox4) {
                mScore += 1;
            } else {
                mScore += 0;
            }
        }

        /**
         * this method clears all radiobuttons group and score
         *
         * @param view
         */
        public void clearAll(View view) {
            radioGroup2.clearCheck();
            radioGroup3.clearCheck();
            radioGroup3.clearCheck();
            radioGroup4.clearCheck();
            radioGroup5.clearCheck();

            if (checkBox1Q1.isChecked()) {
                checkBox1Q1.setChecked(false);
            }
            if (checkBox2Q1.isChecked()) {
                checkBox2Q1.setChecked(false);
            }
            if (checkBox3Q1.isChecked()) {
                checkBox3Q1.setChecked(false);
            }
            if (checkBox4Q1.isChecked()) {
                checkBox4Q1.setChecked(false);
            }

            questionEditText.setText(null);

            mScore = 0;

            Toast.makeText(getApplicationContext(), "Now your score is " + mScore + " Let's start again", Toast.LENGTH_SHORT).show();
        }
    }

