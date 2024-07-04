package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.Config;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class MealEditServlet extends HttpServlet {
    private static final Logger log = getLogger(MealEditServlet.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("edit meal");
        int id = Integer.parseInt(request.getParameter("id"));
        final Meal meal;
        if(id==0) {
            meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),"",0);
        } else {
            meal = Config.get().getStorage().get(id);
        }
        if( request.getParameter("delete") ==null){
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
        } else {
            Config.get().getStorage().delete(meal);
            response.sendRedirect("/topjava/meals");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(request.getParameter("id"));
        final String dateTime = request.getParameter("dateTime");
        final String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        final Meal meal = new Meal(id, LocalDateTime.parse(dateTime), description, calories);
        final Storage storage = Config.get().getStorage();
        if (id == 0) {
            storage.add(meal);
        } else {
            storage.set(meal);
        }

        response.sendRedirect("/topjava/meals");
    }
}
