package com.seleniumtest;
import java.time.Duration;
import java.util.NoSuchElementException;

import javax.print.attribute.standard.MediaSize.NA;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v113.indexeddb.model.Key;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SeleniumExample {

    public static WebDriver driver;
    public static Actions actions; 
    public static WebDriverWait wait;

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:/Windows/chromedriver.exe");

        driver = new ChromeDriver();

        driver.get("https://dell:8443/now/MainDesktop.jsp");

        wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        Thread.sleep(1000);
        logMeIn("system","training");
        Thread.sleep(1000);
        addMethodToProcessDefinition("BookTransaction","HeaderRowProcess.abs","com/bookshop");
    }

    public static void logMeIn(String username,String password) throws InterruptedException{
        try{
        WebElement element1 = driver.findElement(By.xpath("/html/body/div[1]/div/div[3]/div/form/div[2]/div[1]/div/div/div[2]/div[1]/div/div/input']"));
        element1.click();
       }
       catch(Exception e){
        //not secure website exception
          WebElement notSecure = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[2]/button[3]")));
          notSecure.click();
          WebElement proceed = driver.findElement(By.xpath("/html/body/div/div[3]/p[2]/a"));
          proceed.click();
        }
        WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("j_username-inputEl")));
        clickAndSendKeys(usernameField, username);
        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.id("j_password-inputEl")));
        clickAndSendKeys(passwordField, password);
        actions = new Actions(driver);
        actions.sendKeys(Keys.RETURN).perform();
    }
    public static void clickAndSendKeys(WebElement element, String key ){
        element.click();
        element.sendKeys(key);
    }
    public static void addMethodToProcessDefinition(String processname,String processtype,String processPath) throws InterruptedException{
       String processName = processname + "Session.xml";

       WebElement body = driver.findElement(By.tagName("body"));
       body.sendKeys(Keys.ALT + "M" + Keys.BACK_SPACE);

       
       WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-menu-inputEl"))); 
       clickAndSendKeys(search, "Process Definition"); 
       search.sendKeys(Keys.ENTER);

       
       WebElement textInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("textfield-2491-inputEl")));
       clickAndSendKeys(textInput, Keys.chord(Keys.ALT, Keys.F2));
       
       WebElement code = wait.until(ExpectedConditions.elementToBeClickable(By.name("Code")));
       WebElement description = wait.until(ExpectedConditions.elementToBeClickable(By.name("Description")));
       WebElement menuDescription = wait.until(ExpectedConditions.elementToBeClickable(By.name("MenuDescr")));
       WebElement alternateText = wait.until(ExpectedConditions.elementToBeClickable(By.name("AlternateText")));
       clickAndSendKeys(code, processname);
       clickAndSendKeys(description, processname);
       clickAndSendKeys(menuDescription, processname);
       clickAndSendKeys(alternateText, processname);
       WebElement element = driver.findElement(By.id("tab-2600"));
       element.click();
       WebElement urlProcess = driver.findElement(By.id("combo-2529-inputEl"));
       clickAndSendKeys(urlProcess, processtype);

       WebElement path = driver.findElement(By.id("textfield-2530-inputEl"));
       clickAndSendKeys(path,processPath);
       WebElement name = driver.findElement(By.id("textfield-2531-inputEl"));
       clickAndSendKeys(name, processName);
       name.sendKeys(Keys.chord(Keys.ALT,Keys.F3));
       try{
       WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div[4]/div[2]/div/div/div/div[2]/div/div/div[2]/div/div/div[1]/div[2]/div[2]/div/div[1]/table/tbody/tr[1]/td[2]/div")));
       System.out.println("Could not create" + processName + ".Reason: " + errorMessage.getText());
       }catch(org.openqa.selenium.NoSuchElementException e){
            System.out.println(processName + " was created sucessfully.");
       }
    }
    

    // Add process to menu definition test


    // Create random process


    // Create ui
}