package tests;

import Pages.HomePage;
import Pages.LoginPage;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    private HomePage homePage;
    private LoginPage loginPage;


    @BeforeClass
    public void callClasses(){
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
    }
    @DataProvider(name = "loginData" )
    public Object[][] loginDataProvider() {
        return new Object[][]{

            {"email@domain", "password", "Invalid email addresss"},
            {"email@domain.", "password", "Invalid email addresss"}
        };
    }
    @Test(dataProvider = "loginData", description = "Invalid login test cases")
    public void testLoginInvalidData(String email, String password, String expectedErrorMsg) {

        homePage.goToHomepage();
        homePage.clickSignIn();
        loginPage.login(email, password);

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid email");
        Assert.assertTrue(loginPage.isErrorMessageDisplayedCorrectError(expectedErrorMsg),"Error message do not reflect to the corret error");
    }
}
