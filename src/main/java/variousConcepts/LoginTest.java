package variousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {
	WebDriver driver;
	String url;
	String browser;
	By userNameField = By.xpath("//input[@id='username']");
	By passwordField = By.xpath("//input[@id='password']");
	By signInButtonField = By.xpath("//button[@name='login']");
	By dashboardHeaderField = By.xpath("//h2[text()=' Dashboard ']");
	By customerMenuField = By.xpath("//*[@id=\"side-menu\"]/li[3]/a/span[1]");
	By addCustomerField = By.xpath("//*[@id=\"side-menu\"]/li[3]/ul/li[1]/a");
	By fullNameField = By.xpath("//*[@id='account']");
	By companyDropDownField = By.xpath("//select[@id='cid']");
By addCustomerHeaderField = By.xpath("//h5[text()='Add Contact']");
	//By addCustomerHeaderField = By.xpath("//*[@id=\"page-wrapper\"]/div[3]/div[1]/div/div/div/div[1]/h5");
	
	By countryDropDownField = By.xpath("//select[@id='country']");
	By emailField = By.xpath("//*[@id=\"email\"]");
	
	@BeforeClass
	public void readConfig() {
	//InputStream, BufferedReader, FileReader, Scanner
		
		try {
			InputStream input= new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop= new Properties();
			
			prop.load(input);
			browser = prop.getProperty("browser");
			url= prop.getProperty("url");
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	@BeforeMethod
	public void init() {

		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "driver\\chromedriver.exe");
			driver = new ChromeDriver();

		} else if (browser.equalsIgnoreCase("firefox")){
			System.setProperty("webdriver.gecko.driver", "driver\\geckodriver.exe");
			driver = new FirefoxDriver();
		}

		driver.manage().deleteAllCookies();
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

	//@Test(priority=2)
	public void loginTest() {
		driver.findElement(userNameField).sendKeys("demo@techfios.com");
		driver.findElement(passwordField).sendKeys("abc123");
		driver.findElement(signInButtonField).click();

		Assert.assertEquals(driver.findElement(dashboardHeaderField).getText(),"Dashboard", "Page not found!!");
	}
	
	@Test(priority=1)
	public void addCustomer() throws InterruptedException {
		loginTest();
		driver.findElement(customerMenuField).click();
		driver.findElement(addCustomerField).click();
		
		Thread.sleep(3000);//customized method for explicit wait
		
		Assert.assertEquals(driver.findElement(addCustomerHeaderField).getText(),"Add Contact","Page not found");
		
		
		generateRandomNumber(999);
		driver.findElement(fullNameField).sendKeys("Selenium"+generateRandomNumber(999));
		

		selectFromDropDown(driver.findElement(companyDropDownField),"Techfios");
		
		driver.findElement(emailField).sendKeys(generateRandomNumber(999)+"abc123@techfios.com");
		//selectFromDropDown(driver.findElement(countryDropDownField),"Afghanistan");
		selectFromDropDown(countryDropDownField,"Afghanistan");
		
	}
	private int generateRandomNumber(int boundaryNum) {
		Random rnd= new Random();
		int genNum= rnd.nextInt(boundaryNum);
		return genNum;
	}
	
	private void selectFromDropDown(WebElement element, String visibleText) {

		Select sel = new Select(element);
		sel.selectByVisibleText(visibleText);
	
	}
	private void selectFromDropDown(By locator, String visibleText) {
		
	//	Thread.sleep(5000);
		Select sel = new Select(driver.findElement(locator));
		
		//sel.selectByVisibleText(visibleText);
		sel.selectByValue(visibleText);
		
	}
}
