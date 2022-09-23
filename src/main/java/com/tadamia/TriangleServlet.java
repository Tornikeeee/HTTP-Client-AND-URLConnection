package com.tadamia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Properties;

@WebServlet(name = "TriangleServlet", urlPatterns = "/triangles")
public class TriangleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) {

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.properties");
        Properties props = new Properties();

        try {
            //Main.getCredentials(req.getParameter("username"), req.getParameter("password"), response,this.getClass());

            final Double p = Double.parseDouble(req.getParameter("p"));
            if (p < 0)
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"11");

            props.load(in);
            String database = props.getProperty("database_triangles", null);

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(database));

            Triangle[] triangles = gson.fromJson(bufferedReader, Triangle[].class);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            int i = 0;
            for (Triangle triangle : triangles) {
                if (triangle.getLength() == p) {
                    out.println("<h2>[" + triangle + "]</h2><br>");
                    i++;
                }
            }

            if (i == 0)
                response.sendError(HttpServletResponse.SC_NOT_FOUND);

            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException | NullPointerException e) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"22");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
