package com.tadamia;

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
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet(name = "RectangleServlet", urlPatterns = "/rectangles")
public class RectangleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Main.getCredentials(req.getParameter("username"), req.getParameter("password"), resp,this.getClass());


        ThreadContext.put("methodName","RectangleServlet");
        ThreadContext.put("alertMessage","alert message 2");
        Main.lgg.info("doing get method");



        InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.properties");
        Properties props = new Properties();
        Rectangle rectangle = null;
        int i = 0;

        try (in) {
            final double area = Double.parseDouble(req.getParameter("s"));

            if (area < 0)
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"query param is negative");

            props.load(in);
            String database = props.getProperty("database_rectangles");

            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();

            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileReader(database));

            while (reader.hasNext()){
                XMLEvent nextEvent = reader.nextEvent();

                if (nextEvent.isStartElement()){
                    StartElement startElement = nextEvent.asStartElement();

                    switch (startElement.getName().getLocalPart()){
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
                        assert rectangle != null;
                        if (rectangle.getArea() == area) {
                            out.println("<h2>[" + rectangle + "]</h2><br>");
                            i++;
                        }
                    }
                }
            }

            if (i == 0)
                resp.sendError(HttpServletResponse.SC_NOT_FOUND,"2");

        } catch (NumberFormatException | NullPointerException e) {
            try {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (XMLStreamException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
