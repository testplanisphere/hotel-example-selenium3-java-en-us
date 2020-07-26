package hotel;

import static hotel.Utils.BASE_URL;
import static hotel.Utils.getNewWindowHandle;
import static hotel.Utils.sleep;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hotel.pages.ReservePage;
import hotel.pages.ReservePage.Contact;
import hotel.pages.RoomPage;
import hotel.pages.TopPage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Reservation")
class ReserveTest {

  private static DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

  private static DateTimeFormatter longFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US);

  private static WebDriver driver;

  private static WebDriverWait wait;

  private String originalHandle;

  @BeforeAll
  static void initAll() {
    driver = Utils.createWebDriver();
    wait = new WebDriverWait(driver, 10);
  }

  @BeforeEach
  void init() {
    originalHandle = driver.getWindowHandle();
  }

  @AfterEach
  void tearDown() {
    if (driver.getWindowHandles().size() > 1) {
      driver.close();
    }
    driver.switchTo().window(originalHandle);
    driver.manage().deleteAllCookies();
  }

  @AfterAll
  static void tearDownAll() {
    driver.quit();
  }

  @Test
  @Order(1)
  @DisplayName("It should be display initial values [not logged in]")
  void testPageInitValue() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);
    var originalHandles = driver.getWindowHandles();

    var plansPage = topPage.goToPlansPage();
    plansPage.openPlanByTitle("Plan with special offers");
    sleep(500);
    var newHandles = driver.getWindowHandles();
    var newHandle = getNewWindowHandle(originalHandles, newHandles);
    driver.switchTo().window(newHandle);
    var reservePage = new ReservePage(driver);

    var tomorrow = shortFormatter.format(LocalDate.now().plusDays(1));

    assertAll("Initial values",
        () -> assertEquals("Plan with special offers", reservePage.getPlanName()),
        () -> assertEquals(tomorrow, reservePage.getReserveDate()),
        () -> assertEquals("1", reservePage.getReserveTerm()),
        () -> assertEquals("1", reservePage.getHeadCount()),
        () -> assertFalse(reservePage.isEmailDisplayed()),
        () -> assertFalse(reservePage.isTelDisplayed())
    );
    reservePage.setContact(Contact.EMAIL);
    assertAll("By email",
        () -> assertTrue(reservePage.isEmailDisplayed()),
        () -> assertFalse(reservePage.isTelDisplayed()),
        () -> assertTrue(reservePage.getEmail().isEmpty())
    );
    reservePage.setContact(Contact.TELEPHONE);
    assertAll("By telephone",
        () -> assertFalse(reservePage.isEmailDisplayed()),
        () -> assertTrue(reservePage.isTelDisplayed()),
        () -> assertTrue(reservePage.getTel().isEmpty())
    );

    driver.switchTo().frame("room");
    var roomPage = new RoomPage(driver);
    assertEquals("Standard Twin", roomPage.getHeader());
    driver.switchTo().defaultContent();
  }

  @Test
  @Order(2)
  @DisplayName("It should be display initial values [logged in]")
  void testPageInitValueLogin() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);
    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("clark@example.com", "password");
    var originalHandles = driver.getWindowHandles();

    var plansPage = myPage.goToPlansPage();
    plansPage.openPlanByTitle("Premium plan");
    sleep(500);
    var newHandles = driver.getWindowHandles();
    var newHandle = getNewWindowHandle(originalHandles, newHandles);
    driver.switchTo().window(newHandle);
    var reservePage = new ReservePage(driver);

    var tomorrow = shortFormatter.format(LocalDate.now().plusDays(1));

    assertAll("Initial values",
        () -> assertEquals("Premium plan", reservePage.getPlanName()),
        () -> assertEquals(tomorrow, reservePage.getReserveDate()),
        () -> assertEquals("1", reservePage.getReserveTerm()),
        () -> assertEquals("2", reservePage.getHeadCount()),
        () -> assertEquals("Clark Evans", reservePage.getUsername()),
        () -> assertFalse(reservePage.isEmailDisplayed()),
        () -> assertFalse(reservePage.isTelDisplayed())
    );
    reservePage.setContact(Contact.EMAIL);
    assertAll("By email",
        () -> assertTrue(reservePage.isEmailDisplayed()),
        () -> assertFalse(reservePage.isTelDisplayed()),
        () -> assertEquals("clark@example.com", reservePage.getEmail())
    );
    reservePage.setContact(Contact.TELEPHONE);
    assertAll("By telephone",
        () -> assertFalse(reservePage.isEmailDisplayed()),
        () -> assertTrue(reservePage.isTelDisplayed()),
        () -> assertEquals("01234567891", reservePage.getTel())
    );


    driver.switchTo().frame("room");
    var roomPage = new RoomPage(driver);
    assertEquals("Premium Twin", roomPage.getHeader());
    driver.switchTo().defaultContent();
  }

  @Test
  @Order(3)
  @DisplayName("It should be an error when blank values")
  void testBlankInputOne() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);
    var originalHandles = driver.getWindowHandles();

    var plansPage = topPage.goToPlansPage();
    plansPage.openPlanByTitle("Plan with special offers");
    sleep(500);
    var newHandles = driver.getWindowHandles();
    var newHandle = getNewWindowHandle(originalHandles, newHandles);
    driver.switchTo().window(newHandle);
    var reservePage = new ReservePage(driver);

    reservePage.setReserveDate("");
    reservePage.setReserveTerm("");
    reservePage.setHeadCount("");
    reservePage.setUsername("");  // move focus

    assertAll("Error messages",
        () -> assertEquals("Please fill out this field.", reservePage.getReserveDateMessage()),
        () -> assertEquals("Please fill out this field.", reservePage.getReserveTermMessage()),
        () -> assertEquals("Please fill out this field.", reservePage.getHeadCountMessage())
    );
  }

  @Test
  @Order(4)
  @DisplayName("It should be an error when invalid values [under]")
  void testInvalidInputSmall() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);
    var originalHandles = driver.getWindowHandles();

    var plansPage = topPage.goToPlansPage();
    plansPage.openPlanByTitle("Plan with special offers");
    sleep(500);
    var newHandles = driver.getWindowHandles();
    var newHandle = getNewWindowHandle(originalHandles, newHandles);
    driver.switchTo().window(newHandle);
    var reservePage = new ReservePage(driver);

    var today = shortFormatter.format(LocalDate.now());

    reservePage.setReserveDate(today);
    reservePage.setReserveTerm("0");
    reservePage.setHeadCount("0");
    reservePage.setUsername("the tester"); // move focus

    assertAll("Error messages",
        () -> assertEquals("Please enter a date after tomorrow.", reservePage.getReserveDateMessage()),
        () -> assertEquals("Value must be greater than or equal to 1.", reservePage.getReserveTermMessage()),
        () -> assertEquals("Value must be greater than or equal to 1.", reservePage.getHeadCountMessage())
    );
  }

  @Test
  @Order(5)
  @DisplayName("It should be an error when invalid values [over]")
  void testInvalidInputBig() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);
    var originalHandles = driver.getWindowHandles();

    var plansPage = topPage.goToPlansPage();
    plansPage.openPlanByTitle("Plan with special offers");
    sleep(500);
    var newHandles = driver.getWindowHandles();
    var newHandle = getNewWindowHandle(originalHandles, newHandles);
    driver.switchTo().window(newHandle);
    var reservePage = new ReservePage(driver);

    var after90 = shortFormatter.format(LocalDate.now().plusDays(91));

    reservePage.setReserveDate(after90);
    reservePage.setReserveTerm("10");
    reservePage.setHeadCount("10");
    reservePage.setUsername("the tester"); // move focus

    assertAll("Error messages",
        () -> assertEquals("Please enter a date within 3 months.", reservePage.getReserveDateMessage()),
        () -> assertEquals("Value must be less than or equal to 9.", reservePage.getReserveTermMessage()),
        () -> assertEquals("Value must be less than or equal to 9.", reservePage.getHeadCountMessage())
    );
  }

  @Test
  @Order(6)
  @DisplayName("It should be an error when invalid values [string]")
  void testInvalidInputOther() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);
    var originalHandles = driver.getWindowHandles();

    var plansPage = topPage.goToPlansPage();
    plansPage.openPlanByTitle("Plan with special offers");
    sleep(500);
    var newHandles = driver.getWindowHandles();
    var newHandle = getNewWindowHandle(originalHandles, newHandles);
    driver.switchTo().window(newHandle);
    var reservePage = new ReservePage(driver);

    reservePage.setReserveDate("12/3//345");
    reservePage.setReserveTerm("a");
    reservePage.setHeadCount("a");
    reservePage.setUsername("the tester"); // move focus

    assertAll("Error messages",
        () -> assertEquals("Please enter a valid value.", reservePage.getReserveDateMessage()),
        () -> assertFalse(reservePage.getReserveTermMessage().isEmpty()),
        () -> assertFalse(reservePage.getHeadCountMessage().isEmpty())
    );
  }

  @Test
  @Order(7)
  @DisplayName("It should be an error when submitting [mail]")
  void testInvalidInputOnSubmitEmail() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);
    var originalHandles = driver.getWindowHandles();

    var plansPage = topPage.goToPlansPage();
    plansPage.openPlanByTitle("Plan with special offers");
    sleep(500);
    var newHandles = driver.getWindowHandles();
    var newHandle = getNewWindowHandle(originalHandles, newHandles);
    driver.switchTo().window(newHandle);
    var reservePage = new ReservePage(driver);

    reservePage.setUsername("");
    reservePage.setContact(Contact.EMAIL);
    reservePage.setEmail("");
    reservePage.goToConfirmPageExpectingFailure();

    assertAll("Error messages",
        () -> assertEquals("Please fill out this field.", reservePage.getUsernameMessage()),
        () -> assertEquals("Please fill out this field.", reservePage.getEmailMessage())
    );
  }

  @Test
  @Order(8)
  @DisplayName("It should be an error when submitting [tel]")
  void testInvalidInputOnSubmitTel() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);
    var originalHandles = driver.getWindowHandles();

    var plansPage = topPage.goToPlansPage();
    plansPage.openPlanByTitle("Plan with special offers");
    sleep(500);
    var newHandles = driver.getWindowHandles();
    var newHandle = getNewWindowHandle(originalHandles, newHandles);
    driver.switchTo().window(newHandle);
    var reservePage = new ReservePage(driver);

    reservePage.setUsername("");
    reservePage.setContact(Contact.TELEPHONE);
    reservePage.setTel("");
    reservePage.goToConfirmPageExpectingFailure();

    assertAll("Error messages",
        () -> assertEquals("Please fill out this field.", reservePage.getUsernameMessage()),
        () -> assertEquals("Please fill out this field.", reservePage.getTelMessage())
    );
  }

  @Test
  @Order(9)
  @DisplayName("It should be successful the reservation [not logged in] [initial values]")
  void testReserveSuccess() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);
    var originalHandles = driver.getWindowHandles();

    var plansPage = topPage.goToPlansPage();
    plansPage.openPlanByTitle("Plan with special offers");
    sleep(500);
    var newHandles = driver.getWindowHandles();
    var newHandle = getNewWindowHandle(originalHandles, newHandles);
    driver.switchTo().window(newHandle);
    var reservePage = new ReservePage(driver);

    var expectedStart = LocalDate.now().plusDays(1);
    var expectedEnd = LocalDate.now().plusDays(2);
    String expectedTotalBill;
    if (expectedStart.getDayOfWeek() == SUNDAY || expectedStart.getDayOfWeek() == SATURDAY) {
      expectedTotalBill = "Total $87.50 (included taxes)";
    } else {
      expectedTotalBill = "Total $70.00 (included taxes)";
    }
    var expectedTerm = longFormatter.format(expectedStart) + " - " + longFormatter.format(expectedEnd) + ". 1 night(s)";

    reservePage.setUsername("the tester");
    reservePage.setContact(Contact.NO);
    var confirmPage = reservePage.goToConfirmPage();

    assertAll("Confirm reservation",
        () -> assertEquals(expectedTotalBill, confirmPage.getTotalBill()),
        () -> assertEquals("Plan with special offers", confirmPage.getPlanName()),
        () -> assertEquals(expectedTerm, confirmPage.getTerm()),
        () -> assertEquals("1 person(s)", confirmPage.getHeadCount()),
        () -> assertEquals("none", confirmPage.getPlans()),
        () -> assertEquals("the tester", confirmPage.getUsername()),
        () -> assertEquals("not required", confirmPage.getContact()),
        () -> assertEquals("none", confirmPage.getComment())
    );

    confirmPage.doConfirm();
    assertEquals("We look forward to visiting you.", confirmPage.getModalMessage());
    confirmPage.close();
    assertTrue(wait.until(ExpectedConditions.numberOfWindowsToBe(1)));
  }

  @Test
  @Order(10)
  @DisplayName("It should be successful the reservation [logged in]")
  void testReserveSuccess2() {
    driver.get(BASE_URL);
    var topPage = new TopPage(driver);
    var loginPage = topPage.goToLoginPage();
    var myPage = loginPage.doLogin("clark@example.com", "password");
    var originalHandles = driver.getWindowHandles();

    var plansPage = myPage.goToPlansPage();
    plansPage.openPlanByTitle("Premium plan");
    sleep(500);
    var newHandles = driver.getWindowHandles();
    var newHandle = getNewWindowHandle(originalHandles, newHandles);
    driver.switchTo().window(newHandle);
    var reservePage = new ReservePage(driver);

    var expectedStart = LocalDate.now().plusDays(90);
    var expectedEnd = LocalDate.now().plusDays(92);
    String expectedTotalBill;
    if (expectedStart.getDayOfWeek() == SATURDAY) {
      expectedTotalBill = "Total $1,120.00 (included taxes)";
    } else if (expectedStart.getDayOfWeek() == SUNDAY || expectedStart.getDayOfWeek() == FRIDAY) {
      expectedTotalBill = "Total $1,020.00 (included taxes)";
    } else {
      expectedTotalBill = "Total $920.00 (included taxes)";
    }
    final var expectedTerm = longFormatter.format(expectedStart) + " - " + longFormatter.format(expectedEnd) + ". 2 night(s)";

    reservePage.setReserveTerm("2");
    reservePage.setHeadCount("4");
    reservePage.setBreakfastPlan(true);
    reservePage.setEarlyCheckInPlan(true);
    reservePage.setSightseeingPlan(false);
    reservePage.setContact(Contact.EMAIL);
    reservePage.setComment("aaa\n\nbbbbbbb\ncc");
    reservePage.setReserveDate(shortFormatter.format(expectedStart));
    var confirmPage = reservePage.goToConfirmPage();

    assertAll("Confirm reservation",
        () -> assertEquals(expectedTotalBill, confirmPage.getTotalBill()),
        () -> assertEquals("Premium plan", confirmPage.getPlanName()),
        () -> assertEquals(expectedTerm, confirmPage.getTerm()),
        () -> assertEquals("4 person(s)", confirmPage.getHeadCount()),
        () -> assertTrue(confirmPage.getPlans().contains("Breakfast")),
        () -> assertTrue(confirmPage.getPlans().contains("Early check-in")),
        () -> assertFalse(confirmPage.getPlans().contains("Sightseeing")),
        () -> assertEquals("Clark Evans", confirmPage.getUsername()),
        () -> assertEquals("Email: clark@example.com", confirmPage.getContact()),
        () -> assertEquals("aaa\n\nbbbbbbb\ncc", confirmPage.getComment())
    );

    confirmPage.doConfirm();
    assertEquals("We look forward to visiting you.", confirmPage.getModalMessage());
    confirmPage.close();
    assertTrue(wait.until(ExpectedConditions.numberOfWindowsToBe(1)));
  }
}
