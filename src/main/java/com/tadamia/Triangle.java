package com.tadamia;

public class Triangle {
    double a,b,c;

    public Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getLength() {
        return this.a+this.b+this.c;
    }

    @Override
    public String toString() {
        return "a " + this.a +" b " + this.b + " c " + this.c;
    }
}
