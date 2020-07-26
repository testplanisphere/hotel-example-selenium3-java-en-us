package hotel;

import static hotel.Utils.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hotel.pages.SignupPage.Gender;
import hotel.pages.SignupPage.Rank;
import hotel.pages.TopPage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.Colors;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@TestMethodOrder(OrderAnnotation.class)
@DisplayName("MyPage")
class MyPageTest {

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
  @DisplayName("It should be display preset user [clark]")
  void testMyPageExistUserOne() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("clark@example.com", "password");

    assertAll("MyPage display",
        () -> assertEquals("clark@example.com", myPage.getEmail()),
        () -> assertEquals("Clark Evans", myPage.getUsername()),
        () -> assertEquals("Premium", myPage.getRank()),
        () -> assertEquals("Mountain View, California", myPage.getAddress()),
        () -> assertEquals("01234567891", myPage.getTel()),
        () -> assertEquals("male", myPage.getGender()),
        () -> assertEquals("not answered", myPage.getBirthday()),
        () -> assertEquals("received", myPage.getNotification())
    );
  }

  @Test
  @Order(2)
  @DisplayName("It should be display preset user [diana]")
  void testMyPageExistUserTwo() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("diana@example.com", "pass1234");

    assertAll("MyPage display",
        () -> assertEquals("diana@example.com", myPage.getEmail()),
        () -> assertEquals("Diana Johansson", myPage.getUsername()),
        () -> assertEquals("Normal", myPage.getRank()),
        () -> assertEquals("Redmond, Washington", myPage.getAddress()),
        () -> assertEquals("not answered", myPage.getTel()),
        () -> assertEquals("female", myPage.getGender()),
        () -> assertEquals("April 1, 2000", myPage.getBirthday()),
        () -> assertEquals("not received", myPage.getNotification())
    );
  }

  @Test
  @Order(3)
  @DisplayName("It should be display preset user [ororo]")
  void testMyPageExistUserThree() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("ororo@example.com", "pa55w0rd!");

    assertAll("MyPage display",
        () -> assertEquals("ororo@example.com", myPage.getEmail()),
        () -> assertEquals("Ororo Saldana", myPage.getUsername()),
        () -> assertEquals("Premium", myPage.getRank()),
        () -> assertEquals("Cupertino, California", myPage.getAddress()),
        () -> assertEquals("01212341234", myPage.getTel()),
        () -> assertEquals("other", myPage.getGender()),
        () -> assertEquals("December 17, 1988", myPage.getBirthday()),
        () -> assertEquals("not received", myPage.getNotification())
    );
  }

  @Test
  @Order(4)
  @DisplayName("It should be display preset user [miles]")
  void testMyPageExistUserFour() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("miles@example.com", "pass-pass");

    assertAll("MyPage display",
        () -> assertEquals("miles@example.com", myPage.getEmail()),
        () -> assertEquals("Miles Boseman", myPage.getUsername()),
        () -> assertEquals("Normal", myPage.getRank()),
        () -> assertEquals("not answered", myPage.getAddress()),
        () -> assertEquals("01298765432", myPage.getTel()),
        () -> assertEquals("not answered", myPage.getGender()),
        () -> assertEquals("August 31, 1992", myPage.getBirthday()),
        () -> assertEquals("received", myPage.getNotification())
    );
  }

  @Test
  @Order(5)
  @DisplayName("It should be display new user")
  void testMyPageNewUser() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var signupPage = topPage.goToSignupPage();
    signupPage.setEmail("new-user@example.com");
    signupPage.setPassword("11111111");
    signupPage.setPasswordConfirmation("11111111");
    signupPage.setUsername("Jane Doe");
    signupPage.setRank(Rank.NORMAL);
    signupPage.setAddress("Detroit, Michigan");
    signupPage.setTel("09876543211");
    signupPage.setGender(Gender.FEMALE);
    signupPage.setBirthday(LocalDate.parse("2000-01-01"));
    signupPage.setNotification(false);
    var myPage = signupPage.goToMyPage();

    assertAll("MyPage display",
        () -> assertEquals("new-user@example.com", myPage.getEmail()),
        () -> assertEquals("Jane Doe", myPage.getUsername()),
        () -> assertEquals("Normal", myPage.getRank()),
        () -> assertEquals("Detroit, Michigan", myPage.getAddress()),
        () -> assertEquals("09876543211", myPage.getTel()),
        () -> assertEquals("female", myPage.getGender()),
        () -> assertEquals("January 1, 2000", myPage.getBirthday()),
        () -> assertEquals("not received", myPage.getNotification())
    );
  }

  @Test
  @Order(6)
  @DisplayName("It should be an error selecting not image on icon setting")
  void testIconNotImage() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("new-user@example.com", "11111111");
    var iconPage = myPage.goToIconPage();

    Path file = Paths.get("src", "test", "resources", "dummy.txt");
    iconPage.setIcon(file);

    assertEquals("Please select an image file.", iconPage.getIconMessage());
  }

  @Test
  @Order(7)
  @DisplayName("It should be an error selecting over 10KB file on icon setting")
  void testIconOverSize() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("new-user@example.com", "11111111");
    var iconPage = myPage.goToIconPage();

    Path file = Paths.get("src", "test", "resources", "240x240_12.png");
    iconPage.setIcon(file);

    assertEquals("Please select a file with a size of 10 KB or less.", iconPage.getIconMessage());
  }

  @Test
  @Order(8)
  @DisplayName("It should be display icon image")
  void testIconSuccess() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("new-user@example.com", "11111111");
    var iconPage = myPage.goToIconPage();

    Path file = Paths.get("src", "test", "resources", "240x240_01.png");
    iconPage.setIcon(file);
    iconPage.setZoom(80);
    iconPage.setColor(Colors.BLACK.getColorValue());
    iconPage.goToMyPage();

    assertAll("Icon image",
        () -> assertTrue(myPage.existsIconImage()),
        () -> assertEquals(80 - 10, myPage.getIconImageWidth()), // -(padding + border)
        () -> assertEquals(Colors.BLACK.getColorValue(), myPage.getIconImageBorder())
    );
  }

  @Test
  @Order(9)
  @DisplayName("it should delete new user")
  void testDeleteUser() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);

    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("new-user@example.com", "11111111");
    myPage.deleteUser();

    Alert confirm = wait.until(ExpectedConditions.alertIsPresent());
    assertEquals("If you cancel your membership, all information will be deleted.\nDo you wish to proceed?", confirm.getText());
    confirm.accept();
    Alert alert = wait.until(ExpectedConditions.alertIsPresent());
    assertEquals("The process has been completed. Thank you for your service.", alert.getText());
    alert.accept();
    wait.until(ExpectedConditions.urlContains("index.html"));
    assertTrue(driver.getCurrentUrl().contains("index.html"));
  }

}
