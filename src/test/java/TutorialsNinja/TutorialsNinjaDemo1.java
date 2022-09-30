package TutorialsNinja;


import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TutorialsNinjaDemo1 {

	static WebDriver driver ; 
	static String errorMEssageText;
	static Wait<WebDriver> wait;

	@BeforeTest
	public void driverSetup() {


		ChromeOptions chromeOptions = new ChromeOptions();

		// This will make Selenium WebDriver to wait for the entire page is loaded.
		chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

		//This capability checks whether an expired (or) invalid TLS Certificate is used while navigating during a session
		chromeOptions.setAcceptInsecureCerts(true);

		//Specifies when to interrupt an executing script in a current browsing context. The default timeout 30,000 ms
		chromeOptions.setScriptTimeout(Duration.ofSeconds(30));

		//Specifies the time interval in which web page needs to be loaded in a current browsing context. The default timeout 300,000 ms
		chromeOptions.setPageLoadTimeout(Duration.ofMillis(30000));

		//This specifies the time to wait for the implicit element location strategy when locating elements. The default timeout 0 ms
		chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(10));

		driver = new ChromeDriver(chromeOptions);

		driver.get("http://tutorialsninja.com/demo/index.php");

		driver.manage().window().maximize();

		wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(10))
				.pollingEvery(Duration.ofMillis(500));

	}


	@Test (priority = 1)
	public void orderCamera() {

		WebElement currencyBtn = driver.findElement(By.xpath("//form//button//span[contains(text(),'Currency')]"));	
		currencyBtn.click();


		WebElement eurCurrencyBtn = driver.findElement((By.xpath("//form//div//button[@name='EUR']")));

		JavascriptExecutor js =  (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true)",eurCurrencyBtn);

		eurCurrencyBtn.click();


		WebElement cameraLink = driver.findElement(By.xpath("//div[@class='product-thumb transition']//a[contains(text(),'Canon EOS 5D')]"));
		cameraLink.click();

		WebElement addToCart = driver.findElement(By.xpath("//div[@class='form-group']/button[@id='button-cart']"));
		addToCart.click();

		WebElement errorMEssage = driver.findElement(By.xpath("//div[@class='text-danger']"));

		String errorMessageText = errorMEssage.getText();
		System.out.println(errorMessageText);

		WebElement homeScreen = driver.findElement(By.xpath("//div[@id='product-product']//a[@href='http://tutorialsninja.com/demo/index.php?route=common/home']"));
		homeScreen.click();

	}

	@Test (priority = 2)
	public  void orderProductIphone() {

		WebElement iphoneLink = driver.findElement(By.xpath("//div[@class='product-thumb transition']//a[contains(text(),'iPhone')]"));

		JavascriptExecutor js =  (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true)",iphoneLink);


		iphoneLink.click();

		WebElement productQuantity = driver.findElement(By.id("input-quantity"));
		productQuantity.clear();
		productQuantity.sendKeys("2");

		addToCart();

		displaySuccessMessage();
		updateCard();


	} 


	public void addToCart() {

		WebElement addToCartProduct = driver.findElement(By.id("button-cart"));
		addToCartProduct.click();

		System.out.println("Product Added to the cart\n");
	}


	public void displaySuccessMessage() {


		WebElement successMsgElement = driver.findElement(By.xpath("//div[@class='alert alert-success alert-dismissible']"));

		String successMessage = removeLastCharacterFromMessage(successMsgElement.getText());

		System.out.println(successMessage+"\n");

	}


	public  String removeLastCharacterFromMessage(String str) {

		str = str.substring(0, (str.length()-1));

		return str;

	}

	public void updateCard() {


		viewCart();
		WebElement quantity = driver.findElement(By.xpath("//div[@class='input-group btn-block']/input[@type = 'text']"));
		quantity.clear();
		quantity.sendKeys("3");

		WebElement update = driver.findElement(By.xpath("//div[@class='input-group btn-block']/span/button[@data-original-title = 'Update']"));

		update.click();

		System.out.println("Quantity of iphone got updated\n");

	}


	public  void viewCart() {


		WebElement clickCart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cart-total")));
		clickCart.click();

		WebElement viewCart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div//p[@class='text-right']/a[@href='http://tutorialsninja.com/demo/index.php?route=checkout/cart']")));
		viewCart.click();

		System.out.println("Successfully View Cart");

	}

	@Test (dependsOnMethods = {"orderProductIphone"})

	public void checkoutIphone() throws InterruptedException {

		WebElement echoTax = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//table//strong[contains(text(),'Eco')]/parent::td/following-sibling::td)[2]")));

		WebElement vat = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//table//strong[contains(text(),'VAT')]/parent::td/following-sibling::td)[2]")));

		String echoTaxAmout = echoTax.getText();
		String vatAmount = vat.getText();

		System.out.println("Echo Tax:" + echoTaxAmout +"\n" + "Vat Amount \n:" +vatAmount);

		WebElement checkout= driver.findElement(By.xpath("//div//a[contains(text(),'Checkout')]"));
		checkout.click();

		System.out.println("Proceeded to checkout\n");


		WebElement errorMessage = driver.findElement(By.xpath("//div[contains(@class,'alert-danger') and contains(@class,'alert-dismissible')]"));
		String errorMessageText = errorMessage.getText();
		String errorMsg = removeLastCharacterFromMessage(errorMessageText);

		System.out.println(errorMsg);

		WebElement removeItem = driver.findElement(By.xpath("//div[@class='input-group btn-block']/span//button[contains(@data-original-title,'Remove')]"));
		removeItem.click();

		System.out.println("Item removed Successfully\n");

		Thread.sleep(1000); //without sleep method  it wont click home screen button


		WebElement homeScreen = driver.findElement(By.xpath("//ul[@class='breadcrumb']//a[@href='http://tutorialsninja.com/demo/index.php?route=common/home']"));

		//wait.until(ExpectedConditions.elementToBeClickable(homeScreen));

		homeScreen.click();

		System.out.println("Go to home screen\n");

	}


	@Test (priority = 3)

	public void orderProductMacBook() throws InterruptedException {

		WebElement mackBookLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div//div[contains(@class,'caption')]//a[contains(text(),'MacBook')]")));
		mackBookLink.click();

		WebElement qantityElement = driver.findElement(By.xpath("//div//label[contains(text(),'Qty')]/../input[@id='input-quantity']"));

		String macBookQuantity = qantityElement.getAttribute("value");

		if(macBookQuantity.equals("1")) {

			addToCart();
			displaySuccessMessage();
			System.out.println("MacBook added to your cart\n");

		}else {

			System.out.println("Please select quantity!!\n");
		}


		viewCart();

		applyCouponCode();

		applyGiftCertificate();


	}


	public void  applyCouponCode() {


		WebElement couponCodeTab = driver.findElement(By.xpath("//h4[@class='panel-title']/a[contains(text(),'Use Coupon Code')] "));
		couponCodeTab.click();

		WebElement couponCodeElement = driver.findElement(By.id("input-coupon"));
		couponCodeElement.sendKeys("ABCD123");


		WebElement applyCouponElement = driver.findElement(By.id("button-coupon"));
		applyCouponElement.click();

		System.out.println("Coupon applied on your order\n");
		displayErrorMessage();

		couponCodeElement.clear();

		System.out.println("Removed the coupon code!!\n");




	}


	public void applyGiftCertificate() throws InterruptedException  {


		WebElement giftCertificateTab = wait.until(ExpectedConditions.elementToBeClickable((By.xpath("//h4[@class='panel-title']/a[contains(text(),'Use Gift Certificate ')]"))));
		giftCertificateTab.click();

		WebElement giftCertificateElement = driver.findElement(By.id("input-voucher"));
		giftCertificateElement.sendKeys("AXDFGH123");  

		WebElement applyGiftCertificateElement = driver.findElement(By.id("button-voucher"));
		applyGiftCertificateElement.click();	


		System.out.println("Gift Certificate applied on your order\n");

		Thread.sleep(1000);

		displayErrorMessage();

		giftCertificateElement.clear();

		System.out.println("Removed Gift Certificate !!\n ");



	}


	public void displayErrorMessage() {

		WebElement errorMessageElement = driver.findElement(By.xpath("//div[@class='alert alert-danger alert-dismissible']"));

		String wrongCouponMsg = errorMessageElement.getText();

		System.out.println(removeLastCharacterFromMessage(wrongCouponMsg)+"\n");



	}



	@Test (dependsOnMethods = {"orderProductMacBook"})
	public  void checkouProductMacBook() {


		System.out.println("Proceed to checkout\n");

		WebElement checkoutElement = driver.findElement(By.xpath("//div[@class='buttons clearfix']//a[contains(text(),'Checkout')]"));

		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true)",checkoutElement );


		wait.until(ExpectedConditions.elementToBeClickable(checkoutElement));
		checkoutElement.click();

		WebElement registerElement = driver.findElement(By.xpath("//div[@class='radio']//input[@value='register']"));
		registerElement.click();

		System.out.println("Selected Register Account Option\n");

		driver.findElement(By.id("button-account")).click();

		System.out.println("Continue towards account and billing details\n");

	}

	@Test (dependsOnMethods = {"checkouProductMacBook"})
	public  void accountAndBillingDetails() throws InterruptedException {


		WebElement firstNameElement = driver.findElement(By.id("input-payment-firstname"));
		firstNameElement.sendKeys("Poorva");
		String firstName = firstNameElement.getAttribute("value");

		System.out.println("First Name after fname tag:" + firstName);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.panel-body")));

		WebElement lastNameElement = driver.findElement(By.id("input-payment-lastname"));
		lastNameElement.sendKeys("Suryawanshi");

		WebElement emailElement = driver.findElement(By.id("input-payment-email"));

		System.out.println("First Name after email tag:" + firstName);

		String randomEmail= generateEmail(firstName);
		emailElement.sendKeys(randomEmail);


		System.out.println("EMAIL ID :"+ randomEmail);

		WebElement phoneElement =driver.findElement(By.id("input-payment-telephone"));
		phoneElement.sendKeys("9876567");

		WebElement companyNameElement = driver.findElement(By.id("input-payment-company"));
		companyNameElement.sendKeys("Amdocs");

		WebElement address1Element = driver.findElement(By.id("input-payment-address-1"));
		address1Element.sendKeys("24/11,ganesh darshan");


		WebElement address2Element = driver.findElement(By.id("input-payment-address-2"));
		address2Element.sendKeys("shivajinagar");


		WebElement cityElement = driver.findElement(By.id("input-payment-city"));
		cityElement.sendKeys("Pune");

		WebElement postCodeElement = driver.findElement(By.id("input-payment-postcode"));
		postCodeElement.sendKeys("Pune");

		WebElement countryElement = driver.findElement(By.id("input-payment-country"));
		Select countries = new Select(countryElement);
		countries.selectByVisibleText("United States");

		WebElement regionElement = driver.findElement(By.id("input-payment-zone"));
		Select regions = new Select(regionElement);
		regions.selectByVisibleText("New York");


		WebElement passwordElement = driver.findElement(By.id("input-payment-password"));
		passwordElement.sendKeys("Password");

		WebElement passwordConfirmElement = driver.findElement(By.id("input-payment-confirm"));
		passwordConfirmElement.sendKeys("Password");


		WebElement newsLetterCheck  = driver.findElement(By.id("newsletter"));
		newsLetterCheck.click();

		WebElement policyAgreeElement  = driver.findElement(By.xpath("//div[@class='buttons clearfix']//input[@name='agree']"));
		policyAgreeElement.click();


		WebElement continueRegisterElement  = driver.findElement(By.id("button-register"));
		wait.until(ExpectedConditions.elementToBeClickable(continueRegisterElement));
		continueRegisterElement.click();

		System.out.println("Personal Details entered  successfully \n");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.collapse.collapse.in")));

		WebElement commentElement = driver.findElement(By.xpath("//div[@class='panel-body']/p/textarea[@name='comment']"));
		commentElement.sendKeys("This is a comment for mac book!!!");

		WebElement agreeElement = driver.findElement(By.xpath("//div[@class='buttons']//input[@name='agree']"));
		agreeElement.click();

		WebElement continueCheckoutElement = driver.findElement(By.id("button-payment-method"));
		continueCheckoutElement.click();

		System.out.println("Added Comment successfully \n");

		displayErrorMessage();
		System.out.println("Payment Error !!!\n");


	}

	@Test (dependsOnMethods = {"accountAndBillingDetails"})
	public void contactUs() {


		WebElement contactUsElement =  driver.findElement(By.xpath("//div[@class='container']//a[contains(text(),'Contact Us')]"));

		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true)", contactUsElement);

		wait.until(ExpectedConditions.elementToBeClickable(contactUsElement));

		contactUsElement.click();

		System.out.println("Click on contact us hyperlink\n");

		WebElement enquiryTxtArea =  driver.findElement(By.id("input-enquiry"));
		enquiryTxtArea.sendKeys("Payment option is not available for mackbook");


		WebElement submitConactUS  = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@class='btn btn-primary']")));
		submitConactUS.click();


		WebElement continueElement =  driver.findElement(By.xpath("//div[@id='content']//a[contains(text(),'Continue')]"));
		continueElement.click();

		System.out.println("submit a contact request and click continue for continue shopping \n");


	}

	public String generateEmail(String firstName) {


		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(10000);
		String randomEmail = firstName + randomInt +"@gmail.com";

		return randomEmail;

	}


	@AfterTest
	public void closeDriver() throws InterruptedException {

		Thread.sleep(10000);
		driver.quit();
	}



}

