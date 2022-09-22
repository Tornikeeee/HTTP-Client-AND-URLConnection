package com.tadamia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Rectangle")
@XmlType(propOrder = {"a", "b"})
public class Rectangle {
    double a, b;

    public double getA() {
        return a;
    }

    @XmlElement(name = "a")
    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    @XmlElement(name = "b")
    public void setB(double b) {
        this.b = b;
    }

    public double getLength() {
        return 2 * (this.a + this.b);
    }

    public double getArea() {
        return this.a * this.b;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
