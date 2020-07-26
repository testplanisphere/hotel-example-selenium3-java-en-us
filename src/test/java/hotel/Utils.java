package hotel;

import java.util.ArrayList;
import java.util.Collection;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Utils {

  public static final String BASE_URL = "https://hotel.testplanisphere.dev/en-US";

  private Utils() {
    throw new AssertionError();
  }

  public static WebDriver createWebDriver() {
    var githubActions = Boolean.parseBoolean(System.getenv("GITHUB_ACTIONS"));
    var options = new ChromeOptions();
    if (githubActions) {
      options.setHeadless(true);
    }
    var driver = new ChromeDriver(options);
    driver.manage().window().setSize(new Dimension(1920, 1080));
    return driver;
  }

  public static String getNewWindowHandle(Collection<String> handlesBeforeOpen, Collection<String> handlesAfterOpen) {
    var handles = new ArrayList<>(handlesAfterOpen);
    handles.removeAll(handlesBeforeOpen);
    if (handles.isEmpty()) {
      throw new RuntimeException("Cannot find new window");
    } else if (handles.size() > 1) {
      throw new RuntimeException("There are multiple windows");
    } else {
      return handles.get(0);
    }
  }

  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ign) {
      // ignore
    }
  }
}
