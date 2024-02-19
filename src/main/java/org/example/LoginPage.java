package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {

    public void login(WebDriver driver){

        // Navigate to the login page
        driver.get(Constants.LOGIN_URL);

        WebElement username = driver.findElement(By.xpath("//*[@id=\":r1:\"]\n"));
        WebElement password = driver.findElement(By.xpath("//*[@id=\":r2:\"]\n"));
        WebElement login_button = driver.findElement(By.xpath("/html/body/div[1]/div[4]/main/div/div/div/div[2]/div/div[2]/div[4]/button\n"));
        username.sendKeys(Constants.USERNAME);
        password.sendKeys(Constants.PASSWORD);
        login_button.click();

    }

}
