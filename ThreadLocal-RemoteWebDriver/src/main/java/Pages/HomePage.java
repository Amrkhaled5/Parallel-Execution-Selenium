package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private final WebDriver driver;
    private final By signInLink = By.className("login");
    private final By signOutLink=By.className("logout");


    public HomePage(WebDriver driver){
        this.driver=driver;
    }

    public void goToHomepage(){
        driver.get("http://www.automationpractice.pl/index.php");
    }

    public void clickSignIn(){
        driver.findElement(signInLink).click();
    }

    public void clickSignOut(){
        driver.findElement(signOutLink).click();
    }

}
