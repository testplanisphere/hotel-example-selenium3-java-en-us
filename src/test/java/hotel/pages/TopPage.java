package hotel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TopPage {

  private WebDriver driver;

  public TopPage(WebDriver driver) {
    this.driver = driver;
    if (!this.driver.getTitle().equals("HOTEL PLANISPHERE - Website for Practice Test Automation")) {
      throw new IllegalStateException("wrong page: " + this.driver.getTitle());
    }
  }

  public LoginPage goToLoginPage() {
    var loginLink = driver.findElement(By.linkText("Login"));
    loginLink.click();
    return new LoginPage(driver);
  }

  public SignupPage goToSignupPage() {
    var signupLink = driver.findElement(By.linkText("Sign up"));
    signupLink.click();
    return new SignupPage(driver);
  }

  public PlansPage goToPlansPage() {
    var planLink = driver.findElement(By.linkText("Reserve"));
    planLink.click();
    return new PlansPage(driver);
  }
}
