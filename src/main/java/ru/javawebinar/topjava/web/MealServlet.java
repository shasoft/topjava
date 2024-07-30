package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController mealRestController;

    @Override
    public void init() {
        ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        final String id = request.getParameter("id");
        log.info("doPost for id {}", id);
        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (meal.isNew()) {
            log.info("Create {}", meal);
            mealRestController.create(meal);
        } else {
            log.info("Update {}", meal);
            mealRestController.update(meal, meal.getId());
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String action = request.getParameter("action");
        log.info("doGet action {}", action);
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute(
                        "meals",
                        mealRestController.select(
                                getDateFromRequest(request, "startDate"),
                                getTimeFromRequest(request, "startTime"),
                                getDateFromRequest(request, "endDate"),
                                getTimeFromRequest(request, "endTime")
                        )
                );
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private LocalDate getDateFromRequest(HttpServletRequest request, String name) {
        final String str = request.getParameter(name);
        if (str == null || str.isEmpty()) {
            request.setAttribute(name, "");
            return null;
        }
        request.setAttribute(name, str);
        return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private LocalTime getTimeFromRequest(HttpServletRequest request, String name) {
        final String str = request.getParameter(name);
        if (str == null || str.isEmpty()) {
            request.setAttribute(name, "");
            return null;
        }
        request.setAttribute(name, str);
        return LocalTime.parse(str, DateTimeFormatter.ofPattern("HH:mm"));
    }
}
