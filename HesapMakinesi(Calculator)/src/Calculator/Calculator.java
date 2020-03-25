package Calculator;

import java.awt.*;
import java.awt.event.*;

public class Calculator extends Frame {

    boolean setClear = true;
    double number, memoryValue;
    char operator;

    String digitButtonText[] = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "+/-", "."};
    String operatorButtonText[] = {"/", "sqrt", "*", "%", "-", "1/X", "+", "="};
    String memoryButtonText[] = {"MC", "MR", "MS", "M+"};
    String specialButtonText[] = {"Bckspc", "C", "CE"};

    MyDigitButton digitButton[] = new MyDigitButton[digitButtonText.length];
    MyOperatorButton operatorButton[] = new MyOperatorButton[operatorButtonText.length];
    MyMemoryButton memoryButton[] = new MyMemoryButton[memoryButtonText.length];
    MySpecialButton specialButton[] = new MySpecialButton[specialButtonText.length];

    Label displayLabel = new Label("0", Label.RIGHT);
    Label memoryLabel = new Label(" ", Label.RIGHT);

    int FRAME_WIDTH = 300, FRAME_HEIGHT = 310;
    int HEIGHT = 30, WIDTH = 30, H_SPACE = 10, V_SPACE = 10;
    int TOPX = 30, TOPY = 50;

    Calculator(String s) {
        super(s);

        int tempX = TOPX, tempY = TOPY;
        displayLabel.setBounds(tempX, tempY, 240, HEIGHT);
        displayLabel.setBackground(Color.BLACK);
        displayLabel.setForeground(Color.WHITE);
        add(displayLabel);

        memoryLabel.setBounds(TOPX, TOPY + HEIGHT + V_SPACE, WIDTH, HEIGHT);
        add(memoryLabel);

        // Digit Buttonların yerleştirilmesi(Set coordinates for Digit Buttons)
        int digitX = TOPX + WIDTH + H_SPACE;
        int digitY = TOPY + 2 * (HEIGHT + V_SPACE);
        tempX = digitX;
        tempY = digitY;
        for (int i = 0; i < digitButton.length; i++) {
            digitButton[i] = new MyDigitButton(tempX, tempY, WIDTH, HEIGHT, digitButtonText[i], this);
            digitButton[i].setForeground(Color.BLACK);
            tempX += WIDTH + H_SPACE;
            if ((i + 1) % 3 == 0) {
                tempX = digitX;
                tempY += HEIGHT + V_SPACE;
            }
        }

        // Operator Buttonların yerleştirilmesi(Set coordinates for Operator Buttons)
        int operatorX = digitX + 2 * (WIDTH + H_SPACE) + H_SPACE;
        int operatorY = digitY;
        tempX = operatorX;
        tempY = operatorY;
        for (int i = 0; i < operatorButton.length; i++) {
            tempX += WIDTH + H_SPACE;
            operatorButton[i] = new MyOperatorButton(tempX, tempY, WIDTH, HEIGHT, operatorButtonText[i], this);
            operatorButton[i].setForeground(Color.RED);
            if ((i + 1) % 2 == 0) {
                tempX = operatorX;
                tempY += HEIGHT + V_SPACE;
            }
        }

        // Memory Buttonların yerleştirilmesi(Set coordinates for Memory Buttons)
        memoryLabel.setBounds(TOPX, TOPY + HEIGHT + V_SPACE, WIDTH, HEIGHT);
        add(memoryLabel);
        tempX = TOPX;
        tempY = TOPY + 2 * (HEIGHT + V_SPACE);
        for (int i = 0; i < memoryButton.length; i++) {
            memoryButton[i] = new MyMemoryButton(tempX, tempY, WIDTH, HEIGHT, memoryButtonText[i], this);
            memoryButton[i].setForeground(Color.GREEN);
            tempY += HEIGHT + V_SPACE;
        }

        // Special Buttonların yerleştirilmesi(Set coordinates for Special Buttons)
        tempX = TOPX + 1 * (WIDTH + H_SPACE);
        tempY = TOPY + 1 * (HEIGHT + V_SPACE);
        for (int i = 0; i < specialButton.length; i++) {
            specialButton[i] = new MySpecialButton(tempX, tempY, WIDTH * 2, HEIGHT, specialButtonText[i], this);
            specialButton[i].setForeground(Color.BLUE);
            tempX = tempX + 2 * WIDTH + H_SPACE;
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });

        setLayout(null);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setVisible(true);
    }

    static String getFormattedText(double temp) {
        String resetText = "" + temp;
        if (resetText.lastIndexOf(".0") > 0) {
            resetText = resetText.substring(0, resetText.length() - 2);
        }
        return resetText;
    }

    public static void main(String[] args) {
        new Calculator("Hesap Makinesi");
    }
}

class MyDigitButton extends Button implements ActionListener {

    Calculator c;

    MyDigitButton(int x, int y, int width, int height, String s, Calculator calculator) {
        super(s);
        setBounds(x, y, width, height);
        this.c = calculator;
        this.c.add(this);
        addActionListener(this);
    }

    static boolean isInString(String s, char c) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String digitText = ((MyDigitButton) ae.getSource()).getLabel();

        if (digitText.equals(".")) {
            if (c.setClear) {
                c.displayLabel.setText("0");
                c.setClear = false;
            } else if (!isInString(c.displayLabel.getText(), '.')) {
                c.displayLabel.setText(c.displayLabel.getText() + ".");
            }
            return;
        }

        int index = 0;
        try {
            index = Integer.parseInt(digitText);
        } catch (NumberFormatException e) {
            return;
        }

        if (index == 0 && c.displayLabel.getText().equals("0")) {
            return;
        }

        if (c.setClear) {
            c.displayLabel.setText("" + index);
            c.setClear = false;
        } else {
            c.displayLabel.setText(c.displayLabel.getText() + index);
        }
    }
}

class MyOperatorButton extends Button implements ActionListener {

    Calculator c;

    MyOperatorButton(int x, int y, int width, int height, String s, Calculator calculator) {
        super(s);
        setBounds(x, y, width, height);
        this.c = calculator;
        this.c.add(this);
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String operatorText = ((MyOperatorButton) ae.getSource()).getLabel();

        c.setClear = true;
        double temp = Double.parseDouble(c.displayLabel.getText());

        if (operatorText.equals("1/X")) {
            try {
                double temp2 = 1 / (double) temp;
                c.displayLabel.setText(Calculator.getFormattedText(temp2));
            } catch (ArithmeticException e) {
                c.displayLabel.setText("0'a böl.");
            }
            return;
        }
        if (operatorText.equals("sqrt")) {
            try {
                double temp2 = Math.sqrt(temp);
                c.displayLabel.setText(Calculator.getFormattedText(temp2));
            } catch (ArithmeticException e) {
                c.displayLabel.setText("0'a böl.");
            }
            return;
        }
        if (!operatorText.equals("=")) {
            c.number = temp;
            c.operator = operatorText.charAt(0);
            return;
        }

        switch (c.operator) {
            case '+':
                temp += c.number;
                break;
            case '-':
                temp = c.number - temp;
                break;
            case '*':
                temp *= c.number;
                break;
            case '%':
                try {
                    temp = c.number % temp;
                } catch (ArithmeticException e) {
                    c.displayLabel.setText("0'a böl.");
                    return;
                }
                break;
            case '/':
                try {
                    temp = c.number / temp;
                } catch (ArithmeticException e) {
                    c.displayLabel.setText("0'a böl.");
                    return;
                }
                break;
        }
        c.displayLabel.setText(Calculator.getFormattedText(temp));
    }
}

class MyMemoryButton extends Button implements ActionListener {

    Calculator c;

    MyMemoryButton(int x, int y, int width, int height, String s, Calculator calculator) {
        super(s);
        setBounds(x, y, width, height);
        this.c = calculator;
        this.c.add(this);
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        char memoryText = ((MyMemoryButton) ae.getSource()).getLabel().charAt(1);

        c.setClear = true;
        double temp = Double.parseDouble(c.displayLabel.getText());

        switch (memoryText) {
            case 'C':
                c.memoryLabel.setText(" ");
                c.memoryValue = 0.0;
                break;
            case 'R':
                c.displayLabel.setText(Calculator.getFormattedText(c.memoryValue));
                break;
            case 'S':
                c.memoryValue = 0.0;
                break;
            case '+':
                c.memoryValue += Double.parseDouble(c.displayLabel.getText());
                if (c.displayLabel.getText().equals("0") || c.displayLabel.getText().equals("0.0")) {
                    c.memoryLabel.setText(" ");
                } else {
                    c.memoryLabel.setText("M");
                }
                break;
        }
    }
}

class MySpecialButton extends Button implements ActionListener {

    Calculator c;

    MySpecialButton(int x, int y, int width, int height, String s, Calculator calculator) {
        super(s);
        setBounds(x, y, width, height);
        this.c = calculator;
        this.c.add(this);
        addActionListener(this);
    }

    static String backSpace(String s) {
        String sil = "";
        for (int i = 0; i < s.length() - 1; i++) {
            sil += s.charAt(i);
        }
        return sil;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String specialText = ((MySpecialButton) ae.getSource()).getLabel();

        if (specialText.equals("Bckspc")) {
            String temp = backSpace(c.displayLabel.getText());
            if (temp.equals("")) {
                c.displayLabel.setText("0");
            } else {
                c.displayLabel.setText(temp);
            }
            return;
        }

        if (specialText.equals("C")) {
            c.number = 0.0;
            c.operator = ' ';
            c.memoryValue = 0.0;
            c.memoryLabel.setText(" ");
        }

        c.displayLabel.setText("0");
        c.setClear = true;
    }
}
