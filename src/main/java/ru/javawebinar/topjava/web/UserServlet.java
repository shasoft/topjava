package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
        final String action = request.getParameter("action");
        log.info("doGet action {}", action);
        switch (action == null ? "all" : action) {
            case "auth":
                String authUserIdStr = request.getParameter("authUserId");
                log.info("change user to {}", authUserIdStr);
                if (authUserIdStr != null) {
                    Integer authUserId = Integer.parseInt(authUserIdStr);
                    log.info("authUserId {}", authUserId);
                    SecurityUtil.setAuthUserId(authUserId);
                } else {
                    SecurityUtil.setAuthUserId(null);
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
            case "all":
            default:
                log.info("getAll");
                request.getRequestDispatcher("/users.jsp").forward(request, response);
                break;
        }
    }
}
