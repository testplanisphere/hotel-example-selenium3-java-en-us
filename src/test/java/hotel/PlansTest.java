package hotel;

import static hotel.Utils.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hotel.pages.TopPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;

@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Plans")
class PlansTest {

  private static WebDriver driver;

  @BeforeAll
  static void initAll() {
    driver = Utils.createWebDriver();
  }

  @AfterEach
  void tearDown() {
    driver.manage().deleteAllCookies();
  }

  @AfterAll
  static void tearDownAll() {
    driver.quit();
  }

  @Test
  @Order(1)
  @DisplayName("It should be display plans when not logged in")
  void testPlanListNotLogin() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var plansPage = topPage.goToPlansPage();
    var planTitles = plansPage.getPlanTitles();

    assertAll("Plans",
        () -> assertEquals(7, planTitles.size()),
        () -> assertEquals("Plan with special offers", planTitles.get(0)),
        () -> assertEquals("Staying without meals", planTitles.get(1)),
        () -> assertEquals("Business trip", planTitles.get(2)),
        () -> assertEquals("With beauty salon", planTitles.get(3)),
        () -> assertEquals("With private onsen", planTitles.get(4)),
        () -> assertEquals("For honeymoon", planTitles.get(5)),
        () -> assertEquals("With complimentary ticket", planTitles.get(6))
    );
  }

  @Test
  @Order(2)
  @DisplayName("It should be display plans when logged in member")
  void testPlanListLoginNormal() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);
    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("diana@example.com", "pass1234");

    var plansPage = myPage.goToPlansPage();
    var planTitles = plansPage.getPlanTitles();

    assertAll("Plans",
        () -> assertEquals(9, planTitles.size()),
        () -> assertEquals("Plan with special offers", planTitles.get(0)),
        () -> assertEquals("With dinner", planTitles.get(1)),
        () -> assertEquals("Economical", planTitles.get(2)),
        () -> assertEquals("Staying without meals", planTitles.get(3)),
        () -> assertEquals("Business trip", planTitles.get(4)),
        () -> assertEquals("With beauty salon", planTitles.get(5)),
        () -> assertEquals("With private onsen", planTitles.get(6)),
        () -> assertEquals("For honeymoon", planTitles.get(7)),
        () -> assertEquals("With complimentary ticket", planTitles.get(8))
    );
  }

  @Test
  @Order(3)
  @DisplayName("It should be display plans when logged in premium member")
  void testPlanListLoginPremium() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);
    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("clark@example.com", "password");

    var plansPage = myPage.goToPlansPage();
    var planTitles = plansPage.getPlanTitles();

    assertAll("Plans",
        () -> assertEquals(10, planTitles.size()),
        () -> assertEquals("Plan with special offers", planTitles.get(0)),
        () -> assertEquals("Premium plan", planTitles.get(1)),
        () -> assertEquals("With dinner", planTitles.get(2)),
        () -> assertEquals("Economical", planTitles.get(3)),
        () -> assertEquals("Staying without meals", planTitles.get(4)),
        () -> assertEquals("Business trip", planTitles.get(5)),
        () -> assertEquals("With beauty salon", planTitles.get(6)),
        () -> assertEquals("With private onsen", planTitles.get(7)),
        () -> assertEquals("For honeymoon", planTitles.get(8)),
        () -> assertEquals("With complimentary ticket", planTitles.get(9))
    );
  }

}
