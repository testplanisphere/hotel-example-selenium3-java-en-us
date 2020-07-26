package hotel;

import static hotel.Utils.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Redirection")
class RedirectionTest {

  private static WebDriver driver;

  private static WebDriverWait wait;

  @BeforeAll
  static void initAll() {
    driver = Utils.createWebDriver();
    wait = new WebDriverWait(driver, 10);
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
  @DisplayName("It should redirect MyPage to Top when not logged in")
  void testMyPageToTop() {
    driver.get(BASE_URL + "/mypage.html");
    wait.until(ExpectedConditions.urlContains("index.html"));
    assertTrue(driver.getCurrentUrl().endsWith("index.html"));
  }

  @Test
  @Order(2)
  @DisplayName("It should redirect Login to Top when logged in")
  void testLoginPageToTop() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    loginPage.doLogin("clark@example.com", "password");

    driver.get(BASE_URL + "/login.html");
    wait.until(ExpectedConditions.urlContains("index.html"));
    assertTrue(driver.getCurrentUrl().endsWith("index.html"));
  }

  @Test
  @Order(3)
  @DisplayName("It should redirect Sign Up to Top when logged in")
  void testSignupPageToTop() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    loginPage.doLogin("clark@example.com", "password");

    driver.get(BASE_URL + "/signup.html");
    wait.until(ExpectedConditions.urlContains("index.html"));
    assertTrue(driver.getCurrentUrl().endsWith("index.html"));
  }

  @Test
  @Order(4)
  @DisplayName("It should redirect Reserve to Top when invalid plan id [1]")
  void testNoPlanPageToTop() {
    driver.get(BASE_URL + "/reserve.html?plan-id=100");
    wait.until(ExpectedConditions.urlContains("index.html"));
    assertTrue(driver.getCurrentUrl().endsWith("index.html"));
  }

  @Test
  @Order(5)
  @DisplayName("It should redirect Reserve to Top when invalid plan id [2]")
  void testInvalidPlanPageToTop() {
    driver.get(BASE_URL + "/reserve.html?plan-id=abc");
    wait.until(ExpectedConditions.urlContains("index.html"));
    assertTrue(driver.getCurrentUrl().endsWith("index.html"));
  }

  @Test
  @Order(6)
  @DisplayName("It should redirect Reserve to Top when invalid plan id [3]")
  void testInvalidParamPlanPageToTop() {
    driver.get(BASE_URL + "/reserve.html");
    wait.until(ExpectedConditions.urlContains("index.html"));
    assertTrue(driver.getCurrentUrl().endsWith("index.html"));
  }

  @Test
  @Order(7)
  @DisplayName("It should redirect Reserve to Top when not logged in user access member's plan")
  void testMemberOnlyPlanPageToTop() {
    driver.get(BASE_URL + "/reserve.html?plan-id=3");
    wait.until(ExpectedConditions.urlContains("index.html"));
    assertTrue(driver.getCurrentUrl().endsWith("index.html"));
  }

  @Test
  @Order(8)
  @DisplayName("It should redirect Reserve to Top when not logged in user access premium member's plan")
  void testPremiumOnlyPlanPageToTop() {
    driver.get(BASE_URL + "/reserve.html?plan-id=1");
    wait.until(ExpectedConditions.urlContains("index.html"));
    assertTrue(driver.getCurrentUrl().endsWith("index.html"));
  }

  @Test
  @Order(9)
  @DisplayName("It should redirect Reserve to Top when normal user access premium member's plan")
  void testPremiumOnlyPlanNormalMemberPageToTop() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    loginPage.doLogin("diana@example.com", "pass1234");

    driver.get(BASE_URL + "/reserve.html?plan-id=1");
    wait.until(ExpectedConditions.urlContains("index.html"));
    assertTrue(driver.getCurrentUrl().endsWith("index.html"));
  }

  @Test
  @Order(10)
  @DisplayName("It should redirect Confirm to Top when direct access")
  void testInvalidParamConfirmPageToTop() {
    driver.get(BASE_URL + "/confirm.html");
    wait.until(ExpectedConditions.urlContains("index.html"));
    assertTrue(driver.getCurrentUrl().endsWith("index.html"));
  }
}
