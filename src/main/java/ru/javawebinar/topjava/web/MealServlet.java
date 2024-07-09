package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.storage.MemoryMealStorage;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealStorage storage;

    @Override
    public void init() throws ServletException {
        storage = new MemoryMealStorage();
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        storage.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = this.getRoutePath(request);

        log.debug("doGet {}", path);

        switch (path) {
            case "/": {
                this.doPageList(request, response);
            }
            break;
            case "/edit": {
                this.gotoEditPage(request, response);
            }
            break;
            case "/delete": {
                this.doPageDelete(request, response);
            }
            break;
            default:
                response.setStatus(404);
        }
    }

    private void doPageList(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        log.debug("doPageList");

        final List<MealTo> mealTos = MealsUtil.filteredByStreams(
                storage.getAll(),
                LocalTime.MIN, LocalTime.MAX,
                MealsUtil.getMaxCaloriesPerDay()
        );
        request.setAttribute("meals", mealTos);
        request.setAttribute("dateTimeFormatter", TimeUtil.getDatetimeFormat());
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private void gotoEditPage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        log.debug("gotoEditPage");
        Meal meal = this.getMeal(request);
        log.debug("meal {}", meal);
        if (meal == null) {
            meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0);
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
    }

    private void doPageDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        log.debug("doPageDelete");
        final String id = request.getParameter("id");
        log.debug("meal id {}", id);
        if (id != null) {
            storage.delete(Integer.parseInt(id));
            response.sendRedirect(request.getContextPath() + "/meals");
        } else {
            response.setStatus(404);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        final String path = this.getRoutePath(request);

        log.debug("doPost {}", path);

        switch (path) {
            case "/edit": {
                this.saveMeal(request, response);
            }
            default:
                response.setStatus(404);
        }
    }

    private Meal getMeal(HttpServletRequest request) {
        final String id = request.getParameter("id");
        if (id == null) {
            return null;
        }
        return storage.get(Integer.parseInt(id));
    }

    private void saveMeal(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        log.debug("saveMeal");
        final String idParameter = request.getParameter("id");
        final Integer id = idParameter == null ? null : Integer.parseInt(idParameter);
        Meal meal = new Meal(
                id,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );
        log.debug("meal {}", meal);
        if (meal.getId() == null) {
            storage.create(meal);
        } else {
            storage.update(meal);
        }
        response.sendRedirect(request.getContextPath() + "/meals");
    }


    private String getRoutePath(HttpServletRequest request) {
        return (request.getPathInfo() != null ? request.getPathInfo() : "/").toLowerCase();
    }
}
