package com.example.android.archerypoints;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int point = 0;
    int arrow = 0;
    Button b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayForPoint(point);

        b0 = (Button) findViewById(R.id.b_add_zero);
        b0.setOnClickListener(this);

        b1 = (Button) findViewById(R.id.b_add_one);
        b1.setOnClickListener(this);

        b2 = (Button) findViewById(R.id.b_add_two);
        b2.setOnClickListener(this);

        b3 = (Button) findViewById(R.id.b_add_three);
        b3.setOnClickListener(this);

        b4 = (Button) findViewById(R.id.b_add_four);
        b4.setOnClickListener(this);

        b5 = (Button) findViewById(R.id.b_add_five);
        b5.setOnClickListener(this);

        b6 = (Button) findViewById(R.id.b_add_six);
        b6.setOnClickListener(this);

        b7 = (Button) findViewById(R.id.b_add_seven);
        b7.setOnClickListener(this);

        b8 = (Button) findViewById(R.id.b_add_eight);
        b8.setOnClickListener(this);

        b9 = (Button) findViewById(R.id.b_add_nine);
        b9.setOnClickListener(this);

        b10 = (Button) findViewById(R.id.b_add_ten);
        b10.setOnClickListener(this);
    }

    /**
     * this method performs an action on click and adds
     * and displays points from 1 to 10 according to what kind of button is clicked.
     * Adds and displays param. an arrow in every click on the button.
     *
     * @param v
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_add_zero:
                arrow += 1;
                displayForArrows(arrow);
                break;
            case R.id.b_add_one:
                point += 1;
                displayForPoint(point);
                arrow += 1;
                displayForArrows(arrow);
                break;
            case R.id.b_add_two:
                point += 2;
                displayForPoint(point);
                arrow += 1;
                displayForArrows(arrow);
                break;
            case R.id.b_add_three:
                point += 3;
                displayForPoint(point);
                arrow += 1;
                displayForArrows(arrow);
                break;
            case R.id.b_add_four:
                point += 4;
                displayForPoint(point);
                arrow += 1;
                displayForArrows(arrow);
                break;
            case R.id.b_add_five:
                point += 5;
                displayForPoint(point);
                arrow += 1;
                displayForArrows(arrow);
                break;
            case R.id.b_add_six:
                point += 6;
                displayForPoint(point);
                arrow += 1;
                displayForArrows(arrow);
                break;
            case R.id.b_add_seven:
                point += 7;
                displayForPoint(point);
                arrow += 1;
                displayForArrows(arrow);
                break;
            case R.id.b_add_eight:
                point += 8;
                displayForPoint(point);
                arrow += 1;
                displayForArrows(arrow);
                break;
            case R.id.b_add_nine:
                point += 9;
                displayForPoint(point);
                arrow += 1;
                displayForArrows(arrow);
                break;
            case R.id.b_add_ten:
                point += 10;
                displayForPoint(point);
                arrow += 1;
                displayForArrows(arrow);
                break;
        }
    }

    /**
     * this method displays on the screen points.
     *
     * @param score
     */
    public void displayForPoint(int score) {
        TextView pointView = (TextView) (findViewById(R.id.text_points));
        pointView.setText(String.valueOf(score));
    }

    /**
     * this method displays on the screen amount of arrows.
     *
     * @param score
     */
    public void displayForArrows(int score) {
        TextView arrowView = (TextView) (findViewById(R.id.text_arrows));
        arrowView.setText(String.valueOf(score));
    }

    /**
     * this method resets all to zero and displays zero
     *
     * @param v
     */
    public void resetAll(View v) {
        arrow = 0;
        point = 0;
        displayForArrows(arrow);
        displayForPoint(point);
    }
}
