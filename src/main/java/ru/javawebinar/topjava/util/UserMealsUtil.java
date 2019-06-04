package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000));

    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        mealList.sort(Comparator.comparing(UserMeal::getDateTime));

        List<LocalDate> dateWhenCaloriesExceed = new ArrayList<>();

        LocalDate localDate = mealList.get(0).getDateTime().toLocalDate();
        int restCalories = caloriesPerDay;
        for (UserMeal um : mealList) {
            if (localDate.equals(um.getDateTime().toLocalDate())) {
                restCalories -= um.getCalories();
            }
            else {
                if (restCalories < 0)
                    dateWhenCaloriesExceed.add(localDate);
                localDate = um.getDateTime().toLocalDate();
                restCalories = caloriesPerDay - um.getCalories() ;
            }
        }
        if (restCalories < 0)
            dateWhenCaloriesExceed.add(localDate);

        return mealList.stream()
              .filter(x -> x.getDateTime().toLocalTime().compareTo(startTime) >= 0)
                .filter(x -> x.getDateTime().toLocalTime().compareTo(endTime) <= 0)
                .map(x -> new UserMealWithExceed(x.getDateTime(), x.getDescription(),
                        x.getCalories(), dateWhenCaloriesExceed.contains(x.getDateTime().toLocalDate())))
                .collect(Collectors.toList());

    }
}
