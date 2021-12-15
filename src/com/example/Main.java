package com.example;

import java.awt.Dimension;
import Jama.Matrix;
import java.awt.Label;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main implements ActionListener {

    private static Matrices matrices;
    private static int col, row;  //dimentions
    private static double myMatrix[][];
    private static double Zb[][];
    private static double Ib[][];
    private static double Eb[][];
    private static double Cl[][];
    private static double B[][];
    private static double[][] tempMatrix1,tempMatrix2,tempMatrix3,tempMatrix4;
    private static JTextField inputField[][];
    private static int result;
    private static JButton cutSetMatrix, tieSetMatrix, showMatrix,
            newMatrix, currentSetMatrix, voltSetMatrix;
    private static JPanel choosePanel[] = new JPanel[7];
    private static int lastCol, lastRow;
    private JTextField nField = new JTextField(5), bField = new JTextField(5);

    public Main() {
        ChooseOperation();
    }

//    private int getLinks() {
//        int n = row;
//        int b = col;
//        return b - n + 1;
//    }

    private void ChooseOperation() {
        int temp;

        for (temp = 0; temp < choosePanel.length; temp++) {
            choosePanel[temp] = new JPanel();
        }
        

        newMatrix = new JButton("New Matrix");
        newMatrix.setPreferredSize(new Dimension(175, 35));
        newMatrix.addActionListener(this);
        choosePanel[1].add(newMatrix);
        
        showMatrix = new JButton("Show Matrix");
        showMatrix.setPreferredSize(new Dimension(175, 35));
        showMatrix.addActionListener(this);
        showMatrix.setEnabled(false);
        choosePanel[2].add(showMatrix);

        cutSetMatrix = new JButton("Cut-set Matrix");
        cutSetMatrix.setPreferredSize(new Dimension(175, 35));
        cutSetMatrix.addActionListener(this);
        cutSetMatrix.setEnabled(false);
        choosePanel[3].add(cutSetMatrix);

        tieSetMatrix = new JButton("Tie-set Matrix");
        tieSetMatrix.setPreferredSize(new Dimension(175, 35));
        tieSetMatrix.addActionListener(this);
        tieSetMatrix.setEnabled(false);
        choosePanel[4].add(tieSetMatrix);

        currentSetMatrix = new JButton("current Matrix");
        currentSetMatrix.setPreferredSize(new Dimension(175, 35));
        currentSetMatrix.addActionListener(this);
        currentSetMatrix.setEnabled(false);
        choosePanel[5].add(currentSetMatrix);

        voltSetMatrix = new JButton("volt Matrix");
        voltSetMatrix.setPreferredSize(new Dimension(175, 35));
        voltSetMatrix.addActionListener(this);
        voltSetMatrix.setEnabled(false);
        choosePanel[6].add(voltSetMatrix);

//        choosePanel[2].add(new JLabel("Node:"));
//        choosePanel[2].add(nField);
//        choosePanel[1].add(Box.createHorizontalStrut(15)); // a spacer
//        choosePanel[4].add(new JLabel("Branch:"));
//        choosePanel[4].add(bField);
        JOptionPane.showConfirmDialog(null, choosePanel, null,
                JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);

    }

    private static void getDimension() {
        JTextField lField = new JTextField(5); //lenght field 
        JTextField wField = new JTextField(5); //col field

        //design input line
        JPanel choosePanel[] = new JPanel[2];
        choosePanel[0] = new JPanel();
        choosePanel[1] = new JPanel();
        choosePanel[0].add(new JLabel("Enter Dimensitions"));
        choosePanel[1].add(new JLabel("Rows:"));
        choosePanel[1].add(lField);
        choosePanel[1].add(Box.createHorizontalStrut(15)); // a spacer
        choosePanel[1].add(new JLabel("Cols:"));
        choosePanel[1].add(wField);

        result = JOptionPane.showConfirmDialog(null, choosePanel,
                null, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        //save last dimensions
        lastCol = col;
        lastRow = row;

        //ok option
        if (result == JOptionPane.OK_OPTION) {

            if (wField.getText().equals("")) {
                col = 0;
            } else {
                if (isInt(wField.getText())) {
                    col = Integer.parseInt(wField.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong Dimensions");
                    col = lastCol;
                    row = lastRow;
                    return;
                }

                if (isInt(lField.getText())) {
                    row = Integer.parseInt(lField.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong Dimensions");
                    col = lastCol;
                    row = lastRow;
                    return;
                }

            }
            if (col < 1 || row < 1) {
                JOptionPane.showConfirmDialog(null, "You entered wrong dimensions",
                        "Error", JOptionPane.PLAIN_MESSAGE);
                col = lastCol;
                row = lastRow;

            } else {
                tempMatrix1 = myMatrix;
                tempMatrix2 = Zb;
                tempMatrix3 = Ib;
                tempMatrix4 = Eb;
                myMatrix = new double[row][col];
                Zb=new double[col][col];
                Ib= new double[col][1];
                Eb= new double[col][1];
                if (!setElements(myMatrix, "Fill your new matrix A")) //filling the new matrix
                {
                    //backup
                    myMatrix = tempMatrix1;
                }
                if (!setElements(Zb, "Fill your new matrix ZB")) //filling the new matrix
                {
                    //backup
                    Zb = tempMatrix2;
                }
                if (!setElements(Ib, "Fill your new matrix IB")) //filling the new matrix
                {
                    //backup

                    Ib = tempMatrix3;
                }
                if (!setElements(Eb, "Fill your new matrix EB")) //filling the new matrix
                {
                    //backup

                    Eb = tempMatrix4;
                }
                {
                    showMatrix.setEnabled(true);
                    cutSetMatrix.setEnabled(true);
                    tieSetMatrix.setEnabled(true);
                    currentSetMatrix.setEnabled(true);
                    voltSetMatrix.setEnabled(true);
                    matrices = new Matrices(myMatrix,Zb,Ib,Eb);

                }

            }
        } else if (result == 1) {
            col = lastCol;
            row = lastRow;
        }
    }//end get Dimension

    //setting a matrix's elementis

    private static boolean setElements(double matrix[][], String title) {
        int temp, temp1;             //temprature variable
        String tempString;
//{ {} , 2 , 3}
        JPanel choosePanel[] = new JPanel[matrix.length + 2];
        choosePanel[0] = new JPanel();
        choosePanel[0].add(new Label(title));
        choosePanel[choosePanel.length - 1] = new JPanel();
        choosePanel[choosePanel.length - 1].add(new Label("consider space field as zeros"));
        inputField = new JTextField[matrix.length][matrix[0].length];

        //lenght loop
        for (temp = 1; temp <= matrix.length; temp++) {
            choosePanel[temp] = new JPanel();

            for (temp1 = 0; temp1 < matrix[0].length; temp1++) {
                inputField[temp - 1][temp1] = new JTextField(3);
                choosePanel[temp].add(inputField[temp - 1][temp1]);

                if (temp1 < matrix[0].length - 1) {
                    choosePanel[temp].add(Box.createHorizontalStrut(15)); // a spacer
                }

            }//end col loop

        }//end row loop

        result = JOptionPane.showConfirmDialog(null, choosePanel,
                null, JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == 0) {
            checkTextField(inputField);
            for (temp = 0; temp < matrix.length; temp++) {
                for (temp1 = 0; temp1 < matrix[0].length; temp1++) {
                    tempString = inputField[temp][temp1].getText();

                    if (isDouble(tempString)) {
                        matrix[temp][temp1] = Double.parseDouble(inputField[temp][temp1].getText());
                    } else {
                        JOptionPane.showMessageDialog(null, "You entered wrong elements");
                        //backup
                        col = lastCol;
                        row = lastRow;

                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }

    }//end get Inputs
    private static void showMatrix(double[][] matrix, String title) {
        int temp, temp1;             //temprature variable

        if (col == 0 || row == 0) {
            JOptionPane.showMessageDialog(null, "You haven't entered any matrix");
        } else {

            JPanel choosePanel[] = new JPanel[matrix.length + 1];
            choosePanel[0] = new JPanel();
            choosePanel[0].add(new JLabel(title));

            for (temp = 1; temp <= matrix.length; temp++) {
                choosePanel[temp] = new JPanel();

                for (temp1 = 0; temp1 < matrix[0].length; temp1++) {
                    if (matrix[temp - 1][temp1] == -0) {
                        matrix[temp - 1][temp1] = 0;
                    }
                    choosePanel[temp].add(new JLabel("" + matrix[temp - 1][temp1]));

                    if (temp1 < matrix[0].length - 1) {
                        choosePanel[temp].add(Box.createHorizontalStrut(15)); // a spacer
                    }
                }
            }//end col loop

            JOptionPane.showMessageDialog(null, choosePanel, null,
                    JOptionPane.PLAIN_MESSAGE, null);
        }//end row loop

    }//end show Matrix

    public static void main(String[] args) {
        Main m = new Main();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == showMatrix) {
            showMatrix(myMatrix, "Your Matrix");
        } else if (e.getSource() == newMatrix) {
            getDimension();
        } else if (e.getSource() == cutSetMatrix) {
            cutSet();
        } else if (e.getSource() == tieSetMatrix) {
            tieSet();
        } else if (e.getSource() == currentSetMatrix) {
            currentSet();
        } else if (e.getSource() == voltSetMatrix) {
            voltSet();
        }

    }

    private void cutSet() {
        showMatrix(matrices.getC(), "Cut-set");
    }

    private void tieSet() {
        showMatrix(matrices.getB(), "Tie-set Matrex");
    }

    private void currentSet() {
       showMatrix(matrices.getJb(), "currents of branchs");
    }

    private void voltSet() {
       showMatrix(matrices.getVb(), "voltgs of branchs"); 
    }

    private static boolean isDouble(String str) {
        int temp;
        if (str.length() == '0') {
            return false;
        }

        for (temp = 0; temp < str.length(); temp++) {
            if (str.charAt(temp) != '+' && str.charAt(temp) != '-'
                    && str.charAt(temp) != '.'
                    && !Character.isDigit(str.charAt(temp))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isInt(String str) {
        int temp;
        if (str.length() == '0') {
            return false;
        }

        for (temp = 0; temp < str.length(); temp++) {
            if (str.charAt(temp) != '+' && str.charAt(temp) != '-'
                    && !Character.isDigit(str.charAt(temp))) {
                return false;
            }
        }
        return true;
    }

    //for setting spaced fields as zeros
    private static void checkTextField(JTextField field[][]) {
        for (int temp = 0; temp < field.length; temp++) {
            for (int temp1 = 0; temp1 < field[0].length; temp1++) {
                if (field[temp][temp1].getText().equals("")) {
                    field[temp][temp1].setText("0");
                }
            }
        }
    }//end reset


}
