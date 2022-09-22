package com.tadamia;

//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
class Rectangles {
    private List<Rectangle> rectangleList;

    public List<Rectangle> getRectangleList() {
        return rectangleList;
    }

    @XmlElement(name = "rectangle")
    public void setRectangleList(List<Rectangle> rectangleList) {
        this.rectangleList = rectangleList;
    }

    @Override
    public String toString() {
        return "com.tadamia.Rectangles{" +
                "rectangleList=" + rectangleList +
                '}';
    }
}
