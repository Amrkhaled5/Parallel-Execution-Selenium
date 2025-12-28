package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountCreationPage {
    private WebDriver driver;

    public AccountCreationPage(WebDriver driver) {
        this.driver = driver;
    }

    //Locators
    private By titleMr=By.id("id_gender1");
    private By titleMrs=By.id("id_gender2");
    private By firstName=By.id("customer_firstname");
    private By lastName=By.id("customer_lastname");
    private By email=By.id("email");
    private By password=By.id("passwd");
    private By dayOfBirth=By.id("days");
    private By monthOfBirth=By.id("months");
    private By yearOfBirth=By.id("years");
    private By newsletterCheckbox =By.id("newsletter");
    private By registerButton  =By.id("submitAccount");
    private By signoutLink = By.className("logout");
    private By successMessage = By.cssSelector(".alert.alert-success");
    private By ErrorMessage = By.cssSelector(".alert.alert-danger");

    //Use Locators
    public void selectTitle(String title) {
        if (title.equalsIgnoreCase("Mr")) {
            driver.findElement(titleMr).click();
        } else {
            driver.findElement(titleMrs).click();
        }
    }

    public void enterFirstName(String fname) {
        driver.findElement(firstName).sendKeys(fname);
    }

    public void enterLastName(String lname) {
        driver.findElement(lastName).sendKeys(lname);
    }

    public void enterPassword(String pass) {
        driver.findElement(password).sendKeys(pass);
    }

    public void selectDOB(String day, String month, String year) {
        new Select(driver.findElement(dayOfBirth)).selectByValue(day);
        new Select(driver.findElement(monthOfBirth)).selectByValue(month);
        new Select(driver.findElement(yearOfBirth)).selectByValue(year);
    }

    public void subscribeToNewsletter() {
        driver.findElement(newsletterCheckbox).click();
    }

    public void clickRegister() {
        driver.findElement(registerButton).click();
    }

    public boolean isAccountCreatedMessageDisplayed() {
        return driver.findElement(successMessage).isDisplayed();
    }

    public void clickLogout() {
        driver.findElement(signoutLink).click();
    }

    public boolean isSignoutVisible() {
        return driver.findElement(signoutLink).isDisplayed();
    }

    public boolean isValidationErrorApper() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(ErrorMessage));
        return errorMessage.isDisplayed();
    }

}
