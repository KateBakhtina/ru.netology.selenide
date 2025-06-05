package ru.netology.selenide;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.Keys.chord;

import org.apache.commons.lang3.StringUtils;

public class formCardTest {

    private String getDate(int days, String formatOfDate) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(formatOfDate));
    }

    @Test
    void sendFormCardTest() {
        Selenide.open("http://0.0.0.0:9999");
        SelenideElement form = $("#root form");
        form.$("[data-test-id='city'] input").setValue("Орёл");
        form.$("[data-test-id='date'] input")
                .press(chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE)
                .setValue(getDate(3, "dd.MM.yyyy"));
        form.$("[data-test-id='name'] input").setValue("Катя Бахтина");
        form.$("[data-test-id='phone'] input").setValue("+79208123162");
        form.$("[data-test-id='agreement']").click();
        form.$(withText("Забронировать")).click();
        $("#root [data-test-id='notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void selectCityTest() {
        String startCityName = "Ор";

        Selenide.open("http://0.0.0.0:9999");
        SelenideElement form = $("#root form");
        form.$("[data-test-id='city'] input").setValue(startCityName);
        $$(".menu .menu-item").findBy(matchText("^" + startCityName)).click();
        form.$("[data-test-id='date'] button").click();
        form.$("[data-test-id='date'] input").press(chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    }

    @Test
    void selectDateTest() {
        Selenide.open("http://0.0.0.0:9999");

        $(".calendar-input button").click();
        SelenideElement calendarTitle = $(".calendar .calendar__title");
        SelenideElement calendarLayout = $(".calendar .calendar__layout");
        String monthYear = StringUtils.capitalize(getDate(7, "LLLL YYYY"));
        String day = getDate(7, "d");
        Boolean flag = true;
        while (flag) {
            String title = calendarTitle.$(".calendar__name").getText();
            if (monthYear.equals(title)) {
                calendarLayout.$$("td.calendar__day[data-day]").findBy(text(day)).click();
                flag = false;
            } else {
                calendarTitle.$(".calendar__arrow.calendar__arrow_direction_right[data-step='1']").click();
            }
        }
    }
}

