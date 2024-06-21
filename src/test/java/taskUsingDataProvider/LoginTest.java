package taskUsingDataProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginTest {
	
	
	 WebDriver driver;

	    @BeforeClass
	    public void setup() {
	        WebDriverManager.chromedriver().setup();
	        driver = new ChromeDriver();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	    }

	    @Test(dataProvider = "loginData")
	    public void loginTest(String username, String password, boolean expectedResult) {
	        driver.get("https://itassetmanagementsoftware.com/rolepermission/admin/login");

	        try {
	            WebElement usernameField = driver.findElement(By.name("username"));
	            WebElement continueBtn = driver.findElement(By.xpath("//span[text()='Continue']"));
	            WebElement passwordField = driver.findElement(By.name("password"));
	            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

	            usernameField.sendKeys(username);
	            continueBtn.click();
	            passwordField.sendKeys(password);
	            loginButton.click();

	            boolean loginSuccess = verifyLogin();
	            Assert.assertEquals(loginSuccess, expectedResult, "Login test failed for: " + username + "/" + password);

	            if (!loginSuccess) {
	                logError("Login failed for: " + username + "/" + password);
	            }

	        } catch (Exception e) {
	            logError("Exception occurred for: " + username + "/" + password + ". Error: " + e.getMessage());
	        }
	    }

	    @DataProvider(name = "loginData")
	    public Object[][] getData() {
	        return new Object[][]{
	            {"admin", "admin123", true}, // valid credentials
	            {"admin", "wrongpass", false}, // invalid password
	            {"wronguser", "admin123", false}, // invalid username
	            {"", "admin123", false}, // missing username
	            {"admin", "", false} // missing password
	        };
	    }

	    @AfterClass
	    public void tearDown() {
	        if (driver != null) {
	            driver.quit();
	        }
	    }

	    private boolean verifyLogin() {
	        try {
	            // Verify if login is successful by checking for an element present on the dashboard
	            WebElement dashboardElement = driver.findElement(By.id("dashboard")); // Adjust based on actual element
	            return dashboardElement.isDisplayed();
	        } catch (Exception e) {
	            return false;
	        }
	    }

	    private void logError(String message) {
	        try (FileWriter writer = new FileWriter("error_logs.txt", true)) {
	            writer.write(message + "\n");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

}
