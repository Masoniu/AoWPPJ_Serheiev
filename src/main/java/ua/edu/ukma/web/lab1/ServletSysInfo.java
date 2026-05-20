package ua.edu.ukma.web.lab1;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "sysInfoServlet", value = "/sysinfo")
public class ServletSysInfo extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        Runtime runtime = Runtime.getRuntime();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        long totalMemMB = runtime.totalMemory() / (1024 * 1024);
        long freeMemMB = runtime.freeMemory() / (1024 * 1024);
        int processors = runtime.availableProcessors();
        String osName = osBean.getName();
        String osArch = osBean.getArch();
        String osVersion = osBean.getVersion();
        out.println("<html><head><title>System Info</title></head><body>");
        out.println("<h2>Server Details:</h2>");
        out.println("<ul>");
        out.println("<li><b>OS:</b> " + osName + " " + osVersion + " (" + osArch + ")</li>");
        out.println("<li><b>CPU (cores):</b> " + processors + "</li>");
        out.println("<li><b>RAm given to JVM:</b> " + totalMemMB + " MB</li>");
        out.println("<li><b>Free RAM in JVM:</b> " + freeMemMB + " MB</li>");
        out.println("</ul>");
        out.println("<br/><a href=\"index.jsp\">Back</a>");
        out.println("</body></html>");
    }
}