package com.example;

import Jama.Matrix;
import javax.swing.JOptionPane;

public class Matrices {

    private double[][] A;
    private double[][] At;
    private double[][] Al;
    private double[][] Bt;
    private double[][] Bl;
    private double[][] B;
    private double[][] Ct;
    private double[][] Cl;
    private double[][] C;
    private double[][] Zb;
    private double[][] Il;
    private double[][] Ib;
    private double[][] Jb;
    private double[][] Eb;
    private double[][] Vb;
    private final int row;
    private final int col;
    private final int getLinks;

    public Matrices(double[][] a,double[][] Zb,double[][] Ib,double[][] Eb) {
        this.A = a;
        this.Eb = Eb;
        this.Ib = Ib;
        this.Zb = Zb;
        this.row = A.length;
        this.col = A[0].length;
        this.getLinks = this.col - this.row + 1;
        splitA();
        initC();
        initB();
        initIl();
        initJb();
        initVb();
    }

    public double[][] getB() {
        return B;
    }

    public double[][] getA() {
        return A;
    }

    private void splitA() {
        double[][] at;
        try {
            at = new double[row][col - getLinks];

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "you should enter your matrix A");
            return;
        }

        for (int temp = 0; temp < row; temp++) {

            for (int temp1 = 0; temp1 < col - getLinks; temp1++) {
                if (this.A[temp][temp1] == -0) {
                    this.A[temp][temp1] = 0;
                }

                at[temp][temp1] = this.A[temp][temp1];
                //  System.out.println(at[temp-1][temp1]);
            }
        }

        double[][] al = new double[row][getLinks];
        for (int temp = 0; temp < row; temp++) {

            for (int temp1 = col - getLinks; temp1 < col; temp1++) {
                if (this.A[temp][temp1] == -0) {
                    this.A[temp][temp1] = 0;
                }

                al[temp][temp1 - col + getLinks] = this.A[temp][temp1];

            }
        }
//        showMatrix(al, "");
        this.At = reduse(at);
        this.Al = reduse(al);
    }

    private void initC() {
        Matrix At = new Matrix(this.At);
        Matrix Al = new Matrix(this.Al);
        Matrix cl = At.inverse().times(Al);
        this.Cl = cl.getArrayCopy();

        int nodes = cl.getArrayCopy().length;
        this.Ct = unitMatrix(nodes);

        this.C = concat(this.Ct, this.Cl);
    }

    private void initB() {
        Matrix cl;
        cl = new Matrix(Cl);

        Matrix bt = cl.transpose().uminus();
        this.Bt = bt.getArrayCopy();
        int nodes = bt.getArrayCopy().length;
        this.Bl = unitMatrix(nodes);
        this.B = concat(this.Bt, this.Bl);
    }

    private double[][] unitMatrix(int size) {
        double[][] m = new double[size][size];
        for (int i = 0; i < m.length; i++) {
            m[i][i] = 1;
        }
        return m;
    }

    private double[][] concat(double[][] a1, double[][] a2) {
        if (a1.length != a2.length) {
            System.err.println("The number of raw in matrix 1 , matrix 2 is not matched");
            return null;
        }
        double[][] a = new double[a1.length][a1[0].length + a2[0].length];
        for (int i = 0; i < a1.length; i++) {
            for (int j = 0; j < a1[i].length; j++) {
                a[i][j] = a1[i][j];
            }
        }
        for (int i = 0; i < a2.length; i++) {
            for (int j = 0; j < a2[i].length; j++) {
                a[i][j + a1[i].length] = a2[i][j];
            }
        }
        return a;
    }

    private double[][] reduse(double[][] m) {
        double[][] res = new double[m.length - 1][m[0].length];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length; j++) {
                res[i][j] = m[i][j];
            }
        }
        return res;
    }

    public double[][] getC() {
        return this.C;
    }

    private void initIl() {
//        B.Zb.BT.Il = B.Eb-B.Zb.Ib
//      B.Zb.BT
        Matrix m1 = new Matrix(this.B).times(
                new Matrix(this.Zb).times(new Matrix(this.B).transpose()));

//      B.Zb.Ib
        Matrix m2=new Matrix(this.B).times(new Matrix(this.Zb).times(new Matrix(this.Ib)));
//      B.Eb-B.Zb.Ib
        Matrix m3 = new Matrix(this.B).times(new Matrix(this.Eb)).minus(m2);
//        Il
        this.Il = m1.solve(m3).getArrayCopy();
    }

    private void initJb() {
//        Jb = BT.Il
        this.Jb = new Matrix(this.B).transpose().times(new Matrix(this.Il)).getArrayCopy();
    }

    private void initVb() {
//        Vb = Zb(Ib+Jb)-Eb
//        Zb(Ib+Jb)
        Matrix m1 = new Matrix(this.Zb).times(new Matrix(this.Ib).plus(new Matrix(this.Jb)));

//        Vb
        this.Vb = m1.minus(new Matrix(this.Eb)).getArrayCopy();

    }

    public double[][] getJb() {
        return Jb;
    }

    public double[][] getVb() {
        return Vb;
    }
    
}
