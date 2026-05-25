package ua.edu.ukma.web.lab1;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "surnameServlet", value = "/surname")
public class ServletSurname extends HttpServlet {
    private String message;

    public void init() {
        message = "Serheiev";
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>My Surname</title></head><body>");
        out.println("<h1>"+message+"</h1>");
        out.println("<a href=\"index.jsp\">Back</a>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}