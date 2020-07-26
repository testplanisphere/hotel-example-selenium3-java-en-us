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
@DisplayName("Login")
class LoginTest {

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
  @DisplayName("It should be successful logged in preset user")
  void testLoginSuccess() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("clark@example.com", "password");

    assertEquals("MyPage", myPage.getHeaderText());
  }

  @Test
  @Order(2)
  @DisplayName("It should be an error when empty input")
  void testLoginFailBlank() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    loginPage.doLoginExpectingFailure("", "");

    assertAll("Error messages",
        () -> assertEquals("Please fill out this field.", loginPage.getEmailMessage()),
        () -> assertEquals("Please fill out this field.", loginPage.getPasswordMessage())
    );
  }

  @Test
  @Order(3)
  @DisplayName("It should be an error when invalid user")
  void testLoginFailUnregister() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    loginPage.doLoginExpectingFailure("error@example.com", "error");

    assertAll("Error messages",
        () -> assertEquals("Email or password is invalid.", loginPage.getEmailMessage()),
        () -> assertEquals("Email or password is invalid.", loginPage.getPasswordMessage())
    );
  }

}
