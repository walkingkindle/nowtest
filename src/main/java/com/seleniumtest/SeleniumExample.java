package com.seleniumtest;
import java.time.Duration;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.netty.handler.timeout.TimeoutException;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class SeleniumExample {

    public static WebDriver driver;
    public static Actions actions; 
    public static WebDriverWait wait;

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:/Windows/chromedriver.exe");
        Scanner scanner = new Scanner(System.in);

        driver = new ChromeDriver();

        driver.get("https://dell:8443/now/MainDesktop.jsp");

        wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        testFunction("system","training","BookTransferParameter.xml", "HeaderRowProcess.abs","com/bookshop",6,"BookShop");

        System.out.println("Test Completed, press any key to close the browser");
        String anyKey = scanner.nextLine();

        // Close the WebDriver
        driver.quit();
    }




    //-----------------------------------------------------------------------  TESTER METHODS ----------------------------------------------------------------------------------------

    public static void testFunction(String username, String password,String processName,String processType, String processPath,Integer sequence,String MenuName) throws InterruptedException{
        logMeIn(username, password);
        Thread.sleep(1000);
        addToProcessDefinition(processName, processType, processPath);  
        addProcessToMenuDefinitionWithinExistingMenu(MenuName,processName ,sequence);
    }

        public static boolean addProcessToMenuDefinitionWithinExistingMenu(String menuName, String processName,Integer seq) throws InterruptedException{
        openMenu();
        String processNameWithoutXml = stripXmlExtension(processName);
        WebElement search = searchWithoutEntering("Menu Definition");
        search.sendKeys(Keys.ENTER);
        WebElement initialMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath( String.format("//div[text() = '%s']",menuName))));
        initialMenu.click();
        WebElement MenuElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text() = 'Menu Items']")));
        MenuElement.click();
        
        getNew();
        WebElement sequence = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Sequence")));
        clickAndSendKeys(sequence, Integer.toString(seq));
        WebElement process = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ProcessCode")));
        clickAndSendKeys(process, processNameWithoutXml);
        process.sendKeys(Keys.chord(Keys.ALT,Keys.F3));

        return catchErrorOnCreating(processName);
    }







        public static boolean addToProcessDefinition(String processname,String processtype,String processPath) throws InterruptedException, TimeoutException
    {
        openMenu();
        search("Process Definition");
        getNew(); 
        String codeName = stripXmlExtension(processname);

       WebElement code = wait.until(ExpectedConditions.elementToBeClickable(By.name("Code")));
       WebElement description = wait.until(ExpectedConditions.elementToBeClickable(By.name("Description")));
       WebElement menuDescription = wait.until(ExpectedConditions.elementToBeClickable(By.name("MenuDescr")));
       WebElement alternateText = wait.until(ExpectedConditions.elementToBeClickable(By.name("AlternateText")));
       clickAndSendKeys(code, codeName);
       clickAndSendKeys(description, processname);
       clickAndSendKeys(menuDescription, processname);
       clickAndSendKeys(alternateText, processname);
       WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@data-qtip,'URL')]")));  
       element.click();
       WebElement urlProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("InternalURL")));
       clickAndSendKeys(urlProcess, processtype,Keys.ENTER);

       WebElement path = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("UIXMLPath")));
       clickAndSendKeys(path,processPath);
       WebElement classname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("UIXMLName")));
       classname.click();
       classname.sendKeys(processname);
       classname.sendKeys(Keys.chord(Keys.ALT,Keys.F3));

        return catchErrorOnCreating(processname);


    }



    public static void logMeIn(String username,String password) throws InterruptedException{
        try{
        WebElement element1 = driver.findElement(By.xpath("/html/body/div[1]/div/div[3]/div/form/div[2]/div[1]/div/div/div[2]/div[1]/div/div/input']"));
        element1.click();
       }
       catch(Exception e){
        //not secure website exception
          WebElement notSecure = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("details-button")));
          notSecure.click();
          WebElement proceed = driver.findElement(By.id("proceed-link"));
          proceed.click();
        }
        WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("j_username-inputEl")));
        clickAndSendKeys(usernameField, username);
        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.id("j_password-inputEl")));
        clickAndSendKeys(passwordField, password);
        actions = new Actions(driver);
        actions.sendKeys(Keys.RETURN).perform();
    }









    //------------------------------------------------------------------------------------- HELPER METHODS -------------------------------------------------------------

    public static void clickAndSendKeys(WebElement element, String key ){
        element.click();
        element.sendKeys(key);
    }

    public static void clickAndSendKeys(WebElement element, String key, Keys additionalKey) throws InterruptedException{
        element.click();
        element.sendKeys(key);
        Thread.sleep(2000);
        element.sendKeys(additionalKey);
    }
    public static void openMenu(){
        WebElement body = driver.findElement(By.tagName("body"));
        body.sendKeys(Keys.ALT + "M" + Keys.BACK_SPACE);
    }

    public static void search(String query){
       WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-menu-inputEl"))); 
       clickAndSendKeys(search,query); 
       search.sendKeys(Keys.ENTER);

    }
    public static WebElement searchWithoutEntering(String query){
        WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-menu-inputEl"))); 
       clickAndSendKeys(search,query);
       return search;
    }
    public static void exitCurrentWindow(){
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'x-tool-close')]"))).click(); //needs fixing
    }

    public static void getNew() throws InterruptedException{
        Thread.sleep(1000);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@id,'ext-comp')]"))).click();

    }
    public static String stripXmlExtension(String processname) {
        // Check if the file name ends with ".xml" before removing it
        if (processname.endsWith(".xml")) {
            // Remove the ".xml" part
            return processname.substring(0, processname.length() - 4);
        } else {
            // If the file name doesn't end with ".xml," return the original name
            return processname;
        }
    }


    public static Boolean catchErrorOnCreating(String processName){
        try{
       WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div[4]/div[2]/div/div/div/div[2]/div/div/div[2]/div/div/div[1]/div[2]/div[2]/div/div[1]/table/tbody/tr[1]/td[2]/div"))); 
       System.out.println("Could not create" + processName + ".Reason: " + errorMessage.getText());
       return false;
       }catch(org.openqa.selenium.NoSuchElementException e){
            System.out.println(processName + " was created sucessfully.");
            return true;
       }catch(org.openqa.selenium.TimeoutException i){
        System.out.println("processName");
        return true;
       }finally{
        exitCurrentWindow();
       }
    }

}

    


    // Add process to menu definition test


