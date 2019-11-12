package com.applitools.hackathon;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Runs Applitools Hackathon challenge for the demo app https://demo.applitools.com/hackathon.html
 */
public class TraditionalTests {
	private WebDriver driver;
	By usernameField = By.id("username");
	By passwordField = By.id("password");
	By loginButton = By.id("log-in");
	By remembermeCheckbox = By.xpath("//input[@type='checkbox']");

	@BeforeMethod
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", ".\\Properties\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://demo.applitools.com/hackathon.html");
		driver.manage().window().maximize();
	}

	public void login() {
		WebElement UsernameField=driver.findElement(usernameField);
		WebElement PasswordField=driver.findElement(passwordField);
		WebElement LoginButton=driver.findElement(loginButton);
		UsernameField.sendKeys("admin");
		PasswordField.sendKeys("admin");
		LoginButton.click();
	}

	// ** Login Page UI Elements Test **//

	@Test (priority = 1) 
	public void loginPageUIElementsTest() {
		WebElement UsernameField=driver.findElement(usernameField);
		WebElement PasswordField=driver.findElement(passwordField);
		WebElement LoginButton=driver.findElement(loginButton);
		WebElement RememberMe=driver.findElement(remembermeCheckbox);

		boolean usernameIsDisplayed = UsernameField.isDisplayed();
		assertEquals(usernameIsDisplayed, true, "Username field is not displayed");

		boolean passwordIsDisplayed = PasswordField.isDisplayed();
		assertEquals(passwordIsDisplayed, true, "Password field is not displayed");

		boolean loginButtonIsDisplayed = LoginButton.isDisplayed();
		assertEquals(loginButtonIsDisplayed, true, "Login Button is not displayed");

		String loginFormLabel = driver.findElement(By.className("auth-header")).getText();
		assertEquals(loginFormLabel, "Login Form", "Login Form Label is not displayed");

		String usernameLabel = driver.findElement(By.xpath("//input[@id='username']/..//label")).getText();
		assertEquals(usernameLabel, "Username", "Username Label is not displayed");

		String passwordLabel = driver.findElement(By.xpath("//input[@id='password']/..//label")).getText();
		assertEquals(passwordLabel, "Password", "Password Label is not displayed");

		String remembermeLabel = driver.findElement(By.xpath("//input[@type='checkbox']/..")).getText();
		assertEquals(remembermeLabel, "Remember Me", "Remember Me Label is not displayed");

		boolean remebermeIsUnselected = RememberMe.isSelected();
		assertEquals(remebermeIsUnselected, false, "Remember Me is selected");

		String usernameFieldPlaceholder = driver.findElement(By.id("username")).getAttribute("placeholder");
		assertEquals(usernameFieldPlaceholder, "Enter your username", "Username field placeholder is incorrect");

		String passwordFieldPlaceholder = driver.findElement(By.id("password")).getAttribute("placeholder");
		assertEquals(passwordFieldPlaceholder, "Enter your password", "Password field placeholder is incorrect");

	}

	// ** Login Page Test **//

	@Test (priority = 2)
	public void loginPageDataDrivenTest() {

		WebElement UsernameField=driver.findElement(usernameField);
		WebElement PasswordField=driver.findElement(passwordField);
		WebElement LoginButton=driver.findElement(loginButton);
		// ** Check whether error message is displayed if you don’t enter the user name
		// and password and click the login button
		LoginButton.click();
		String errorMessage = "";
		errorMessage = driver.findElement(By.xpath("//div[@class='alert alert-warning']")).getText();
		assertEquals(errorMessage, "Both Username and Password must be present", "Error message is not displayed");

		// ** Check whether error message is displayed if you only enter the user name
		// and click the login button

		UsernameField.sendKeys("admin");
		LoginButton.click();
		errorMessage = driver.findElement(By.xpath("//div[@class='alert alert-warning']")).getText();
		assertEquals(errorMessage, "Password must be present", "Error message is not displayed");

		// ** Check whether error message is displayed if you only enter the password
		// and click the login button

		UsernameField.clear();
		PasswordField.sendKeys("admin");
		LoginButton.click();
		errorMessage = driver.findElement(By.xpath("//div[@class='alert alert-warning']")).getText();
		assertEquals(errorMessage, "Username must be present", "Error message is not displayed");

		// ** Check whether login is successful if you enter the user name, password
		// and click the login button
		UsernameField.sendKeys("admin");
		PasswordField.sendKeys("admin");
		LoginButton.click();

	}

	// ** To verify that that column is in ascending order after clicking on Amounts header ** //

	@Test (priority = 3)
	public void tableSortTest() {
		String columnValues="";
		List<String> actualTableValues=new ArrayList<String>();
		List<String> expectedTableValues=new ArrayList<String>();
		login();
		WebElement AmountHeader=driver.findElement(By.xpath("//table[@id='transactionsTable']//thead//th[contains(text(),'Amount')]"));
		AmountHeader.click();
		List<WebElement> rows=driver.findElements(By.xpath("//table[@id='transactionsTable']//tbody//tr"));
		for (int i = 1; i <= rows.size(); i++) {
			columnValues=driver.findElement(By.xpath("//tbody//tr["+i+"]//td[5]")).getText();
			actualTableValues.add(columnValues);
		}
		expectedTableValues.add("- 320.00 USD, - 244.00 USD, + 17.99 USD, + 340.00 USD, + 952.23 USD, + 1,250.00 USD");
		boolean actualResult = actualTableValues.toString().equals(expectedTableValues.toString());
		assertEquals(actualResult, true, "Sort order is not working as expected");
	}

	// ** To verify the Canvas Chart ** //

	@Test (priority = 4)
	public void canvasChartTest() {
		login();
		WebElement CompareExpenses= driver.findElement(By.id("showExpensesChart"));
		CompareExpenses.click();
		WebElement CanvasChart=driver.findElement(By.id("canvas"));
		boolean canvasIsDisplayed = CanvasChart.isDisplayed();
		assertEquals(canvasIsDisplayed, true, "Canvas Chart is not displayed");

		WebElement AddDataForNextYear= driver.findElement(By.id("addDataset"));
		AddDataForNextYear.click();
		boolean canvasDataFor2019IsDisplayed = CanvasChart.isDisplayed();
		assertEquals(canvasDataFor2019IsDisplayed, true, "Canvas Chart data for year 2019 is not displayed");
	}

	// ** Test for the existence of a display ad that’s dynamic ** //

	@Test(priority = 5)
	public void dynamicContentTest() {
		driver.get("https://demo.applitools.com/hackathonApp.html?showAd=true");
		boolean imgGif1 = driver.findElement(By.xpath("//img[@src='img/flashSale.gif']")).isDisplayed();
		assertEquals(imgGif1, true, "flashSale gif image is missing");

		boolean imgGif2 = driver.findElement(By.xpath("//img[@src='img/flashSale2.gif' or @src='img/flaseSale3.gif']")).isDisplayed();
		assertEquals(imgGif2, true, "flashSale gif image 2 is missing");	
	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
}