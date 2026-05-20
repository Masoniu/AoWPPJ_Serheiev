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
    private String osName;
    private String osArch;
    private String osVersion;

    public void init() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        osName = osBean.getName();
        osArch = osBean.getArch();
        osVersion = osBean.getVersion();
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        Runtime runtime = Runtime.getRuntime();
        long totalMemMB = runtime.totalMemory() / (1024 * 1024);
        long freeMemMB = runtime.freeMemory() / (1024 * 1024);
        int processors = runtime.availableProcessors();
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

    public void destroy() {
    }
}