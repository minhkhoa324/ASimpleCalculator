package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String prevNumber = null;
    private String prevOperator = null;
    private int stage = 0; // Our current stage
    private TextView display, displayOP;
    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnCom;
    private Button btnAC, btnPlus, btnMinus, btnMul, btnDiv, btnEqual, btnPercent;
    private Button btnSign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectToID();
        normalButtonAssign();
        operatorButtonAssign();
    }

    private void normalButtonAssign() {
        Button[] myButtons = {btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnCom};
        for (Button myButton : myButtons) {
            myButton.setOnClickListener(view -> update(myButton.getText().toString()));
        }
    }

    private void operatorButtonAssign() {
        // Operator Process
        Button[] myButtons = {btnPlus, btnMinus, btnMul, btnDiv};
        for (Button myButton : myButtons) {
            myButton.setOnClickListener(view -> updateOperator(myButton.getText().toString()));
        }
        btnAC.setOnClickListener(view -> resetApp());
        btnEqual.setOnClickListener(view -> process(btnEqual.getText().toString()));
        btnSign.setOnClickListener(view -> changeSign());
        btnPercent.setOnClickListener(view -> toPercent());
    }

    private void connectToID() {
        // Textview
        display = findViewById(R.id.displayMain);
        displayOP = findViewById(R.id.currentOP);

        // Normal buttons
        btn0 = findViewById(R.id.number0);
        btn1 = findViewById(R.id.number1);
        btn2 = findViewById(R.id.number2);
        btn3 = findViewById(R.id.number3);
        btn4 = findViewById(R.id.number4);
        btn5 = findViewById(R.id.number5);
        btn6 = findViewById(R.id.number6);
        btn7 = findViewById(R.id.number7);
        btn8 = findViewById(R.id.number8);
        btn9 = findViewById(R.id.number9);
        btnCom = findViewById(R.id.decimal);

        // Operator buttons;
        btnAC = findViewById(R.id.C);
        btnPlus = findViewById(R.id.plus);
        btnMinus = findViewById(R.id.minus);
        btnDiv = findViewById(R.id.div);
        btnMul = findViewById(R.id.multi);
        btnEqual = findViewById(R.id.equal);
        btnSign = findViewById(R.id.changeSign);
        btnPercent = findViewById(R.id.percent);
    }

    // Process strings like this "00003"
    private String validate_input(String result) {
        int n = result.length();
        for (int i = 0; i < n; ++i) {
            if (result.charAt(i) != '0') {
                return result.charAt(i) == '.' ? result.substring(i - 1, n) : result.substring(i, n);
            }
        }
        return "0";
    }

    // Update our current expression
    private void update(String exp) {
        if (stage == -1) {
            resetApp();
        }

        if (stage == 1) {
            display.setText("0");
            ++stage;
        }
        String text = display.getText().toString();
        if (exp.equals(".")) {
            if (text.indexOf('.') != -1) {
                return;
            }
        }
        text += exp;
        text = validate_input(text);
        display.setText(text);
    }

    // Process when our result is a integer
    private String validate_process(String result) {
        double temp = Double.parseDouble(result);
        if (temp == (int) temp) {
            return String.valueOf((int) temp);
        } else {
            return String.valueOf(temp);
        }
    }

    // Change a positive to negative and otherwise
    private void changeSign() {
        String temp = display.getText().toString();
        double ourCurrNum = (-1) * Double.parseDouble(temp);
        display.setText(validate_process(String.valueOf(ourCurrNum)));
    }

    // Divide our number by 100
    private void toPercent() {
        String temp = display.getText().toString();
        double ourCurrNum = Double.parseDouble(temp) / 100.0;
        display.setText(validate_process(String.valueOf(ourCurrNum)));
    }

    // Update our current operator and select when to process
    private void updateOperator(String op) {
        if (stage == -1) {
            stage = 1;
            prevOperator = op;
            prevNumber = display.getText().toString();
        }
        if (prevNumber == null) {
            ++stage;
            prevNumber = display.getText().toString();
        }
        if (stage == 2) {
            process(prevOperator);
            stage = 1;
        }
        prevOperator = op;
        if (prevOperator != null) {
            displayOP.setText(prevOperator);
        } else {
            displayOP.setText("");
        }
    }

    // Process our current Math expression
    public void process(String operator) {
        if (stage != 2) {
            return;
        }
        if (operator.equals("=")) {
            stage = -1;
            displayOP.setText("");
            operator = prevOperator;
        }
        String curr = display.getText().toString();
        double prevAns = Double.parseDouble(prevNumber);
        double currNumber = Double.parseDouble(curr);
        double finalAns = 0;
        if (operator.equals("+")) {
            finalAns = prevAns + currNumber;
        }
        if (operator.equals("-")) {
            finalAns = prevAns - currNumber;
        }
        if (operator.equals("X")) {
            finalAns = prevAns * currNumber;
        }
        if (operator.equals("/")) {
            try  {
                finalAns = prevAns / currNumber;
            } catch (ArithmeticException e) {
                resetApp();
                return;
            }
        }
        prevNumber = validate_process(String.valueOf(finalAns));
        display.setText(validate_process(String.valueOf(finalAns)));
    }

    // Reset our app to starting state
    private void resetApp() {
        prevNumber = null;
        prevOperator = null;
        stage = 0;
        display.setText("0");
        displayOP.setText("");
    }
}