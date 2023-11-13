package com.seleniumtest;
import javax.print.attribute.standard.MediaSize.NA;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v113.indexeddb.model.Key;
import org.openqa.selenium.interactions.Actions;

public class SeleniumExample {

    public static WebDriver driver;
    public static Actions actions; 
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:/Windows/chromedriver.exe");

        driver = new ChromeDriver();

        driver.get("https://dell:8443/now/MainDesktop.jsp");

        Thread.sleep(1000);
        logMeIn("system","training");
        Thread.sleep(1000);
        addMethodToProcessDefinition("BookTransaction","HeaderRowProcess.abs","com/bookshop","BookTransactionSession.xml");
    }

    public static void logMeIn(String username,String password) throws InterruptedException{
        try{
        WebElement element1 = driver.findElement(By.xpath("/html/body/div[1]/div/div[3]/div/form/div[2]/div[1]/div/div/div[2]/div[1]/div/div/input']"));
        element1.click();
       }
       catch(Exception e){
        //not secure website exception
          Thread.sleep(1000);
          WebElement notSecure = driver.findElement(By.xpath("/html/body/div/div[2]/button[3]"));
          notSecure.click();
          WebElement proceed = driver.findElement(By.xpath("/html/body/div/div[3]/p[2]/a"));
          proceed.click();
        }
        Thread.sleep(1000);
        WebElement usernameField = driver.findElement(By.id("j_username-inputEl"));
        usernameField.click();
        usernameField.sendKeys(username);
        Thread.sleep(1000);
        WebElement passwordField = driver.findElement(By.id("j_password-inputEl"));
        passwordField.click();
        passwordField.sendKeys(password);
        actions = new Actions(driver);
        actions.sendKeys(Keys.RETURN).perform();
    }
    public static void addMethodToProcessDefinition(String processname,String processtype,String Path,String Name) throws InterruptedException{
        WebElement body = driver.findElement(By.tagName("body"));
       // actions = new Actions(driver);
       body.sendKeys(Keys.ALT + "M" + Keys.BACK_SPACE);
       Thread.sleep(1000);
       WebElement search = driver.findElement(By.id("search-menu-inputEl")); 
       search.click();
       search.sendKeys("Process Definition");
       search.sendKeys(Keys.ENTER);
       Thread.sleep(1000);
       WebElement textInput = driver.findElement(By.name("textfield-2491-inputEl"));
       textInput.click();
       textInput.sendKeys(Keys.chord(Keys.ALT,Keys.F2));
       Thread.sleep(1000);
       WebElement code = driver.findElement(By.name("Code"));
       WebElement description = driver.findElement(By.name("Description"));
       WebElement menuDescription = driver.findElement(By.name("MenuDescr"));
       WebElement alternateText = driver.findElement(By.name("AlternateText"));
       code.click();
       code.sendKeys(processname);
       description.click();
       description.sendKeys(processname);
       menuDescription.click();
       menuDescription.sendKeys(processname);
       alternateText.click();
       alternateText.sendKeys(processname);
       WebElement element = driver.findElement(By.id("tab-2600"));
       element.click();
       WebElement urlProcess = driver.findElement(By.id("combo-2529-inputEl"));
       urlProcess.click();
       urlProcess.sendKeys(processtype);
       WebElement path = driver.findElement(By.id("textfield-2530-inputEl"));
       path.click();
       path.sendKeys(Path);
       WebElement name = driver.findElement(By.id("textfield-2531-inputEl"));
       name.click(); 
       name.sendKeys(Name);
       name.sendKeys(Keys.chord(Keys.ALT,Keys.F3));
       System.out.println("Process saved sucessfully "+ Name);

    }
    

    // Add process to menu definition test


    // Create random process


    // Create ui
}