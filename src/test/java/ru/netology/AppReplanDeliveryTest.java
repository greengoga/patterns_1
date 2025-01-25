package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import com.github.javafaker.Faker;
import java.util.Locale;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AppReplanDeliveryTest {

    private String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    String planningDate = generateDate(3, "dd.MM.yyyy");
    static Faker faker = new Faker(new Locale("ru"));
    static String randomCity = faker.address().city();
    static String randomName = faker.name().fullName();
    static String randomPhone = faker.phoneNumber().phoneNumber();

    @Test
    void shouldSubmitSuccessfully() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue(randomCity);
        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue(randomName);
        $("[data-test-id='phone'] input").setValue(randomPhone);
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='success-notification'] .notification__title").shouldBe(visible).shouldHave(text("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + planningDate));
    }

    @Test
    void shouldSubmitSuccessfullyAfterRescheduling() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue(randomCity);
        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        if(!generateDate(3,"MM").equals((generateDate(7, "MM")))) $(" .calendar__arrow_direction_right[data-step='1']").click();
        $$(".calendar__day").findBy(Condition.text(generateDate(7, "d"))).click();
        $("[data-test-id='name'] input").setValue(randomName);
        $("[data-test-id='phone'] input").setValue(randomPhone);
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='replan-notification'] .button").click();
        $("[data-test-id='success-notification'] .notification__title").shouldBe(visible).shouldHave(text("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + generateDate(7, "dd.MM.yyyy")));
    }
}

