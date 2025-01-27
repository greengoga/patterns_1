package ru.netology.test;

import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AppReplanDeliveryTest {

    @BeforeEach
    void setup() {open("http://localhost:9999"); }

    @Test
    @DisplayName("Should successfully schedule a meeting")
    void shouldSubmitSuccessfully() {
        var ValidUser = DataGenerator.Registration.generateUser("ru");

        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id='city'] input").setValue(ValidUser.getCity());
        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(ValidUser.getName());
        $("[data-test-id='phone'] input").setValue(ValidUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(Selectors.byText("Запланировать")).click();
        $(Selectors.withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(visible);
        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $(Selectors.byText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate))
                .shouldBe(visible);
    }
}

/*    private String generateDate(int days, String pattern) {
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
} */