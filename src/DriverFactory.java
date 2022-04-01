import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;

/**
 * Created by ravikanthk on 3/31/22.
 */
public class DriverFactory {
    public static ChromeDriver getChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "src/resources/chromedriver");

        DesiredCapabilities caps = new DesiredCapabilities();

        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        ChromeDriver driver = new ChromeDriver(caps);

        return driver;
    }
}
