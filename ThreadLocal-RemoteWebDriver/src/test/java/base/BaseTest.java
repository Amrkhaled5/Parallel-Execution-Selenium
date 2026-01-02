package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.net.MalformedURLException;
import java.net.URL;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    private static ThreadLocal<WebDriver> threadDriver=new ThreadLocal<>();
    private static final String HUB_URL = "http://localhost:4444";


    @BeforeClass(alwaysRun = true)
    @Parameters("browser")
    public void setUp(String browser) throws MalformedURLException {

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            driver = new RemoteWebDriver(new URL(HUB_URL), options);

        } else if (browser.equalsIgnoreCase("edge")) {
            System.setProperty("webdriver.edge.driver", "msedgedriver.exe");
            EdgeOptions options = new EdgeOptions();
            driver = new RemoteWebDriver(new URL(HUB_URL), options);

        } else {
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        threadDriver.set(driver);
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    public WebDriver getDriver() {
        return threadDriver.get();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            threadDriver.remove();
        }
    }
}
