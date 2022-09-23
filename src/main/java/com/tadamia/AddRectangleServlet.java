package com.tadamia;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.apache.logging.log4j.ThreadContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

@WebServlet(name = "AddRectangleServlet", urlPatterns = "/basicAuth")
public class AddRectangleServlet extends HttpServlet {
    Rectangles rectangles = new Rectangles();

    @Override
    public void init() {
        ThreadContext.put("methodName","AddRectangleServlet");
        ThreadContext.put("alertMessage","initializing");
        Main.lgg.info("doing get method");

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.properties");
        Properties props = new Properties();
        Rectangle rectangle = null;
        rectangles.setRectangleList(new ArrayList<>());

        try (in) {
            props.load(in);
            String database = props.getProperty("database_rectangles");

            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileReader(database));

            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();

                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();

                    switch (startElement.getName().getLocalPart()) {
                        case "rectangle":
                            rectangle = new Rectangle();
                            break;
                        case "a":
                            nextEvent = reader.nextEvent();
                            assert rectangle != null;
                            rectangle.setA(Double.parseDouble(nextEvent.asCharacters().getData()));
                            break;
                        case "b":
                            nextEvent = reader.nextEvent();
                            assert rectangle != null;
                            rectangle.setB(Double.parseDouble(nextEvent.asCharacters().getData()));
                            break;
                    }
                }

                if (nextEvent.isEndElement()) {
                    EndElement endElement = nextEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals("rectangle")) {
                        rectangles.getRectangleList().add(rectangle);
                    }
                }
            }

        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            JAXBContext context= JAXBContext.newInstance(Rectangle.class);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
            Rectangle rectangle = new Rectangle(); //(Rectangle) unmarshaller.unmarshal(req.getInputStream());
            rectangle.setA(10);
            rectangle.setB(20);
            rectangles.getRectangleList().add(rectangle);


            resp.setContentType("text/xml");
            resp.setCharacterEncoding("UTF-8");
            context = JAXBContext.newInstance(Rectangles.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(rectangles,resp.getOutputStream());

        } catch (JAXBException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
