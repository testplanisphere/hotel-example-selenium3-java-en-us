package hotel;

import static hotel.Utils.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hotel.pages.SignupPage.Gender;
import hotel.pages.SignupPage.Rank;
import hotel.pages.TopPage;
import java.time.LocalDate;
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
@DisplayName("Sign up")
class SignupTest {

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
  @DisplayName("It should be successful sign up")
  void testSignupSuccess() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var signupPage = topPage.goToSignupPage();
    signupPage.setEmail("new-user@example.com");
    signupPage.setPassword("password");
    signupPage.setPasswordConfirmation("password");
    signupPage.setUsername("new user 1");
    signupPage.setRank(Rank.NORMAL);
    signupPage.setAddress("New York City");
    signupPage.setTel("01234567891");
    signupPage.setGender(Gender.FEMALE);
    signupPage.setBirthday(LocalDate.parse("2000-01-01"));
    signupPage.setNotification(true);
    var myPage = signupPage.goToMyPage();

    assertEquals("MyPage", myPage.getHeaderText());
  }

  @Test
  @Order(2)
  @DisplayName("It should be an error when blank")
  void testSignupErrorBlank() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var signupPage = topPage.goToSignupPage();
    signupPage.setEmail("");
    signupPage.setPassword("");
    signupPage.setPasswordConfirmation("");
    signupPage.setUsername("");
    signupPage.setRank(Rank.PREMIUM);
    signupPage.setAddress("");
    signupPage.setTel("");
    signupPage.setGender(Gender.NOT_ANSWER);
    signupPage.setBirthday(null);
    signupPage.setNotification(false);
    signupPage.goToMyPageExpectingFailure();

    assertAll("Error messages",
        () -> assertEquals("Please fill out this field.", signupPage.getEmailMessage()),
        () -> assertEquals("Please fill out this field.", signupPage.getPasswordMessage()),
        () -> assertEquals("Please fill out this field.", signupPage.getPasswordConfirmationMessage()),
        () -> assertEquals("Please fill out this field.", signupPage.getUsernameMessage()),
        () -> assertEquals("", signupPage.getAddressMessage()),
        () -> assertEquals("", signupPage.getTelMessage()),
        () -> assertEquals("", signupPage.getGenderMessage()),
        () -> assertEquals("", signupPage.getBirthdayMessage())
    );
  }

  @Test
  @Order(3)
  @DisplayName("It should be an error when invalid value")
  void testSignupErrorInvalid() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var signupPage = topPage.goToSignupPage();
    signupPage.setEmail("a");
    signupPage.setPassword("1234567");
    signupPage.setPasswordConfirmation("1");
    signupPage.setUsername("tester tester");
    signupPage.setRank(Rank.NORMAL);
    signupPage.setAddress("Chicago, Illinois");
    signupPage.setTel("1234567890");
    signupPage.setGender(Gender.OTHER);
    signupPage.setBirthday(LocalDate.parse("2000-01-01"));
    signupPage.setNotification(true);
    signupPage.goToMyPageExpectingFailure();

    assertAll("Error messages",
        () -> assertEquals("Please enter a non-empty email address.", signupPage.getEmailMessage()),
        () -> assertEquals("Please lengthen this text to 8 characters or more.", signupPage.getPasswordMessage()),
        () -> assertEquals("Please lengthen this text to 8 characters or more.", signupPage.getPasswordConfirmationMessage()),
        () -> assertEquals("", signupPage.getUsernameMessage()),
        () -> assertEquals("", signupPage.getAddressMessage()),
        () -> assertEquals("Please match the requested format.", signupPage.getTelMessage()),
        () -> assertEquals("", signupPage.getGenderMessage()),
        () -> assertEquals("", signupPage.getBirthdayMessage())
    );
  }

  @Test
  @Order(4)
  @DisplayName("It should be an error when email has already been taken")
  void testSignupErrorDouble() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var signupPage = topPage.goToSignupPage();
    signupPage.setEmail("new-user@example.com");
    signupPage.setPassword("password");
    signupPage.setPasswordConfirmation("password");
    signupPage.setUsername("new user 1");
    signupPage.setRank(Rank.NORMAL);
    signupPage.setAddress("Las Vegas, Nevada");
    signupPage.setTel("01234567891");
    signupPage.setGender(Gender.FEMALE);
    signupPage.setBirthday(LocalDate.parse("2000-01-01"));
    signupPage.setNotification(true);
    signupPage.goToMyPageExpectingFailure();

    assertEquals("Email has already been taken.", signupPage.getEmailMessage());
  }

  @Test
  @Order(5)
  @DisplayName("It should be an error when password doesn't match")
  void testSignupErrorUnMatchPassword() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var signupPage = topPage.goToSignupPage();
    signupPage.setEmail("new-user@example.com");
    signupPage.setPassword("password");
    signupPage.setPasswordConfirmation("123456789");
    signupPage.setUsername("new user 1");
    signupPage.setRank(Rank.NORMAL);
    signupPage.setAddress("Kansas City, Missouri");
    signupPage.setTel("01234567891");
    signupPage.setGender(Gender.MALE);
    signupPage.setBirthday(LocalDate.parse("2000-01-01"));
    signupPage.setNotification(true);
    signupPage.goToMyPageExpectingFailure();

    assertEquals("Password doesn't match.", signupPage.getPasswordConfirmationMessage());
  }

}
