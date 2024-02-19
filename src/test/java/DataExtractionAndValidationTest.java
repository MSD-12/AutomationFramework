import org.example.LoginPage;
import org.example.BaseClass;
import org.example.Constants;
import org.example.Helper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DataExtractionAndValidationTest extends BaseClass {

    @Test(description = "This TC will go to chargebacks/transactions then select location and market place and create csv file.")
    public void TC_001_DataExtractionAndValidationTest() {

        // Generating a unique file name for output
        String fileName = "outputFile/part2/" + System.currentTimeMillis() + "table_data.csv";

        // Setting implicit wait time for elements to load
        driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);

        // Instantiate LoginPage object
        LoginPage loginPage = new LoginPage();

        // Logging in using credentials
        loginPage.login(driver);

        // Waiting for the URL to change after successful login
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(35));
        wait.until(ExpectedConditions.urlToBe("https://app.tryloop.ai/home"));

        // Handling pop-up
        Helper.findAndClickOnElement(driver, "//button[text()='Skip for now']");

        // Navigating to transactions page
        driver.get(Constants.TRANSACTIONS_PAGE_URL);

        // Waiting for a dropdown element to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='root']/div[@id='main_start_app']/main/div/div/header/div/div/button[1]")));

        // Handling dropdown selection
        Helper.findAndClickOnElement(driver,"//button[@data-testid='selectBtn' ]/span[text()='Locations']");//"//div[@role='presentation']//button[text()='Apply']");

        // click on clear button
        Helper.findAndClickOnElement(driver, "//div[@role='presentation']//button[text()='Clear']");

        // select "Artisan Alchemy" location
        Helper.findAndClickOnElement(driver,"//div[@aria-label='Artisan Alchemy']//input[@type='checkbox']");

        // select "Blissful Buffet" location
        Helper.findAndClickOnElement(driver,"//div[@aria-label='Blissful Buffet']//input[@type='checkbox']");

        // Apply all selected changes
        Helper.findAndClickOnElement(driver,"//div[@role='presentation']//button[2]");

        // click on marketplace dropDown
        Helper.findAndClickOnElement(driver, "//button[@data-testid='selectBtn' ]/span[text()='Marketplaces']" );

        // click on clear button
        Helper.findAndClickOnElement(driver, "//div[@role='presentation']//button[text()='Clear']");

        // select "Grubhub" market place
        Helper.findAndClickOnElement(driver, "//div[@aria-label='Grubhub']//input[@type='checkbox']");

        // click on apply changes
        Helper.findAndClickOnElement(driver, "//div[@role='presentation']//button[@data-testid='applyBtn']");


        // Find the table element
        try (FileWriter csvWriter = new FileWriter(fileName)) {

            Thread.sleep(10000);
            driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);
            WebElement table = driver.findElement(By.tagName("table"));

            // Find all rows within the table
         //   List<WebElement> rows = driver.findElements(By.tagName("tr"));
            List<WebElement> headerCells = table.findElements(By.tagName("th"));


            String[] test = null;
            csvWriter.append(Helper.getHeader(headerCells).toString());
            csvWriter.append("\n");

            boolean hasNextPage = true;
            while (hasNextPage) {
                driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);

                table = driver.findElement(By.tagName("table"));
                List<WebElement> rows = table.findElements(By.tagName("tr"));

                for (WebElement row : rows) {
                    List<WebElement> columns = row.findElements(By.tagName("td"));
                    StringBuilder csvRow = new StringBuilder();

                    if (columns.size() == 5) {
                        Helper.copyFirst3Column(test, csvRow);

                    }

                    for (WebElement column : columns) {
                        csvRow.append(column.getText()).append(",");
                    }


                    if (!csvRow.isEmpty()) {
                        csvRow.deleteCharAt(csvRow.length() - 7); // Remove trailing comma
                        csvRow.append("\n");
                        csvWriter.append(csvRow.toString());
                        test = csvRow.toString().split(",");

                    }


                }

                WebElement nextPageButton = driver.findElement(By.cssSelector("button[data-testid='pagination-next']"));
                hasNextPage = Helper.clickOnNextPage(nextPageButton, driver);
            }

            System.out.println("CSV file created successfully: " + fileName);
            csvWriter.flush();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

}