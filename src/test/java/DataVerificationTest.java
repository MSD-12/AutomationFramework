import org.example.LoginPage;
import org.example.BaseClass;
import org.example.Constants;
import org.example.Helper;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DataVerificationTest extends BaseClass {


    @Test(description = "This TC will go to chargebacks/stores/view then get column data for each month and calculate it and compare it with grand total present on UI and create a txt file.")
    public void TC_001_DataVerificationTest() {

        // Generating a unique file name for output
        String fileName = "outputFile/part1/" + System.currentTimeMillis() + "DataVerification.txt";

        // Instantiate LoginPage object
        LoginPage loginPage = new LoginPage();

        // Perform login
        loginPage.login(driver);

        // Wait for the URL to change to the home page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Wait for a maximum of 10 seconds
        wait.until(ExpectedConditions.urlToBe("https://app.tryloop.ai/home"));

        // Skip the alert window
        WebElement skipAlertWindow = driver.findElement(By.xpath("//button[normalize-space()='Skip for now']"));
        skipAlertWindow.click();

        // Navigate to the 3P_Chargebacks/History_view_by_store page
        driver.get(Constants.HISTORY_BY_STORE_PAGE_URL);

        // Locate the dropdown element and retrieve its text
        WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div[3]/main/div/div/div[4]/div/div[1]/div[1]/div/div/div\n"));
        String dropDown = element.getText();

        // Process data if dropdown is "Reversals"
        if (dropDown.equals("Reversals")) {

            // Wait until the table has at least one row of data
            WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='MuiBox-root css-mycntp']")));
            wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(table, By.xpath("//h6[normalize-space()='Artisan Alchemy']")));

            try {
                // Create a FileWriter in append mode
                FileWriter fileWriter = new FileWriter(fileName);

                // Wrap FileWriter in BufferedWriter for efficient writing
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                boolean hasNextPage = true;
                while (hasNextPage) {

                    // Find all rows within the table
                    List<WebElement> rows = table.findElements(By.tagName("tr"));

                    List<WebElement> headerCells = table.findElements(By.tagName("th"));

                    for (int i = 2; i < headerCells.size() - 1; i++) {

                        ArrayList<Double> monthlyData = new ArrayList<>();
                        double total = 0;

                        for (int j = 1; j < rows.size() - 2; j++) {

                            String amount = (driver.findElement(By.xpath("/html/body/div[1]/div[3]/main/div/div/div[4]/div/div[1]/div[3]/div/table/tbody/tr[" + j + "]/td[" + i + "]\n")).getText()).replace("$", "").replace(",", "");
                            monthlyData.add(Double.valueOf(amount));
                            total = total + Double.parseDouble(amount);

                        }

                        // df used to maintain upto 2 values after a decimal
                        DecimalFormat df = new DecimalFormat("#.##");
                        bufferedWriter.write("[" + Helper.listToString(monthlyData, df) + "] = " + df.format(total));
                        bufferedWriter.newLine();

                        String month = driver.findElement(By.xpath("/html/body/div[1]/div[3]/main/div/div/div[4]/div/div[1]/div[3]/div/table/thead/tr/th[" + i + "]\n")).getText();
                        System.out.println("Grand total for month " + month + " : " + driver.findElement(By.xpath("/html/body/div[1]/div[3]/main/div/div/div[4]/div/div[1]/div[3]/div/table/tbody/tr[" + (rows.size() - 1) + "]/td[" + i + "]\n")).getText() + " (actual on UI) = $" + String.format("%.2f", total) + "(expected)");
                        bufferedWriter.write("Grand total for month " + month + " : " + driver.findElement(By.xpath("/html/body/div[1]/div[3]/main/div/div/div[4]/div/div[1]/div[3]/div/table/tbody/tr[" + (rows.size() - 1) + "]/td[" + i + "]\n")).getText() + " (actual on UI) = $" + String.format("%.2f", total) + "(expected)");
                        bufferedWriter.newLine(); // Add a new line after the data

                    }
                    WebElement nextPageButton = driver.findElement(By.cssSelector("button[data-testid='pagination-next']"));
                    hasNextPage = Helper.clickOnNextPage(nextPageButton, driver);

                }
                bufferedWriter.close();
                fileWriter.close();
                System.out.println("Data appended to the file successfully : "+ fileName);

            } catch (IOException e) {

                System.out.println("Error occurred during file writing operation");
                e.printStackTrace();

            }

        }

    }
}
