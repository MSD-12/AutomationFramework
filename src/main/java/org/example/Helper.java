package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Helper {

    public Helper() {

    }
    public static StringBuilder getHeader(List<WebElement> headerCells){
        // Extract header texts
        StringBuilder csvRowHead = new StringBuilder();

        for (WebElement header : headerCells) {
            csvRowHead.append(header.getText()).append(",");
        }
        return csvRowHead;
    }

    public static void copyFirst3Column(String[] test, StringBuilder csvRow){

        if (test != null) {
            for (int i = 0; i <3 ; i++) {
                csvRow.append(test[i]+",");
            }
        }
    }

    public static boolean clickOnNextPage(WebElement nextPageButton, WebDriver driver) {
        if (nextPageButton.isEnabled()) {
            //nextPageButton.click();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", nextPageButton);

            return true;
        } else {
            return false; // Exit loop if "Next" button is disabled
        }
    }

    public static String listToString(ArrayList<Double> list, DecimalFormat df) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(df.format(list.get(i)));
            if (i < list.size() - 1) {
                sb.append("+");
            }
        }
        return sb.toString();
    }

    public static void findAndClickOnElement( WebDriver driver, String path){
        driver.findElement(By.xpath(path)).click();
    }



}
