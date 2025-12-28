package tests;

import Pages.HomePage;
import Pages.LoginPage;
import base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {


    @DataProvider(name = "loginData" ,parallel = true)
    public Object[][] loginDataProvider() {
        return new Object[][]{
                {"chrome", "email@domain", "password", "Invalid email address."},
                {"chrome", "empty@test.com", "wrongpass", "Authentication failed."},
                {"edge",   "email@domain.", "password", "Invalid email address."},
                {"edge",   "user4@test.com", "wrongpass", "Authentication failed."}
        };
    }
    @Test(dataProvider = "loginData", description = "Invalid login test cases")
    public void testLoginInvalidData(String browser, String email, String password, String expectedErrorMsg) {

        WebDriver driver = setUp(browser);
        try {
            HomePage homePage= new HomePage(driver);
            LoginPage loginPage= new LoginPage(driver);


            homePage.goToHomepage();
            homePage.clickSignIn();
            loginPage.login(email, password);

            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid email");
            Assert.assertTrue(loginPage.isErrorMessageDisplayedCorrectError(expectedErrorMsg),"Error message do not reflect to the corret error");
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
