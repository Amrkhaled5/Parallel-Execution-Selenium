package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private WebDriver driver;

    public LoginPage(WebDriver driver){
        this.driver=driver;
    }

    //Locators for create Account
    private final By newEmailInput=By.id("email_create");
    private final By createAccountButton=By.id("SubmitCreate");
    private final By accountAlreadyRegisterdMsg=By.id("create_account_error");

    //Locators for login
    private final By emailLoginInput =By.id("email");
    private final By passwordLoginInput =By.id("passwd");
    private final By signInButton =By.id("SubmitLogin");
    private final By ErrorMessage = By.cssSelector(".alert.alert-danger");

    //access locators for new Account
    public void creatAccountWithNewEmail(String email){
        driver.findElement(newEmailInput).sendKeys(email);
    }
    public void clickCreateAccount() {
        driver.findElement(createAccountButton).click();
    }
    public boolean isErrorMessageDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(ErrorMessage));
        return errorMessage.isDisplayed();
    }
    public boolean isErrorMessageDisplayedCorrectError(String msg){
        WebElement  msgBox=driver.findElement(ErrorMessage);
        if(msgBox.getText().contains(msg)){
            return true;
        }
        return false;
    }

    //access locators for login
    public void login(String email, String password) {
        driver.findElement(emailLoginInput).sendKeys(email);
        driver.findElement(passwordLoginInput).sendKeys(password);
        driver.findElement(signInButton).click();
    }

}
