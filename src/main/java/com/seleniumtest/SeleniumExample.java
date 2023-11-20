package com.seleniumtest;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ByTagName;
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

        driver = new ChromeDriver();

        driver.get("https://dell:8443/now/MainDesktop.jsp");

        wait = new WebDriverWait(driver, Duration.ofSeconds(3));

      testFunction("Book", "Book",6,"BookShop");
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
    public static void clickAndSendKeys(WebElement element, String key ){
        element.click();
        element.sendKeys(key);
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
        WebElement x = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("x-tool")));
        x.click();
    }
    public static void addToProcessDefinition(String processname,String processtype,String processPath) throws InterruptedException, TimeoutException
    {
       String processName = processname + "Session.xml";
        openMenu();
        search("Process Definition");
        WebElement textInput = driver.findElement(By.tagName("body"));
        clickAndSendKeys(textInput, Keys.chord(Keys.ALT, Keys.F2));
        
        

       WebElement code = wait.until(ExpectedConditions.elementToBeClickable(By.name("Code")));
       WebElement description = wait.until(ExpectedConditions.elementToBeClickable(By.name("Description")));
       WebElement menuDescription = wait.until(ExpectedConditions.elementToBeClickable(By.name("MenuDescr")));
       WebElement alternateText = wait.until(ExpectedConditions.elementToBeClickable(By.name("AlternateText")));
       clickAndSendKeys(code, processname);
       clickAndSendKeys(description, processname);
       clickAndSendKeys(menuDescription, processname);
       clickAndSendKeys(alternateText, processname);
       WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='tab-2603']"))); 
       element.click();
       WebElement urlProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("InternalURL")));
       clickAndSendKeys(urlProcess, processtype);

       WebElement path = findWithRetry(By.name("UIXMLPath"), processName);
       clickAndSendKeys(path,processPath);
       WebElement classname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("UIXMLName")));
       classname.click();
       classname.sendKeys(processName);
       classname.sendKeys(Keys.chord(Keys.ALT,Keys.F3));


       try{
       WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div[4]/div[2]/div/div/div/div[2]/div/div/div[2]/div/div/div[1]/div[2]/div[2]/div/div[1]/table/tbody/tr[1]/td[2]/div")));
       System.out.println("Could not create" + processName + ".Reason: " + errorMessage.getText());
       }catch(org.openqa.selenium.NoSuchElementException e){
            System.out.println(processName + " was created sucessfully.");
       }catch(org.openqa.selenium.TimeoutException i){
        System.out.println("processName");
       }
       exitCurrentWindow();
    }
    
    public static void testFunction(String functionName,String processName,Integer sequence,String MenuName) throws InterruptedException{
        logMeIn("system", "training");
        Thread.sleep(1000);
        addToProcessDefinition(processName, "HeaderRowProcess.abs", "com/bookshop");
        addProcessToMenuDefinitionWithinExistingMenu(MenuName,functionName ,sequence);
    }

    // Add process to menu definition test
    public static void addProcessToMenuDefinitionWithinExistingMenu(String menuName, String processName,Integer seq) throws InterruptedException{
        openMenu();
        WebElement search = searchWithoutEntering("Menu Definition");
        search.sendKeys(Keys.ENTER);
        WebElement initialMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath( String.format("//div[text() = '%s']",menuName))));
        initialMenu.click();
        WebElement MenuElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text() = 'Menu Items']")));
        MenuElement.click();
        WebElement ClicableDiv = driver.findElement(By.tagName("body")); //try to find a better way to navigate to the button
        ClicableDiv.click();
        ClicableDiv.sendKeys(Keys.chord(Keys.ALT,Keys.F2));
        //WebElement newButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@id,'toolbar')]/descendant::span")));
        //newButton.click();

        WebElement sequence = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Sequence")));
        sequence.click();
        sequence.sendKeys(Integer.toString(seq));

        WebElement process = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ProcessCode")));
        clickAndSendKeys(process, processName);
        process.sendKeys(Keys.chord(Keys.ALT,Keys.F3));
        
       //WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text() = 'Save']")));


        exitCurrentWindow();

    }

    public static WebElement findWithRetry(By type,String query){
        WebElement webElement;
         try{
            webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(type));
        }catch(org.openqa.selenium.TimeoutException e){
            String newName = incrementNumbers(query);

            webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(newName)));
        }
        return webElement;
    }
    private static String incrementNumbers(String input) {
        // Define a regular expression to capture groups of letters and numbers
        String regex = "([a-zA-Z]+|\\d+)(-([a-zA-Z]+|\\d+))*";

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(input);

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String group = matcher.group();
            if (group.matches("\\d+")) {
                // If the group is a number, increment it
                int number = Integer.parseInt(group) + 1;
                result.append(number);
            } else {
                // If the group is letters, append it as is
                result.append(group);
            }

            // Append the separator if there is one
            if (matcher.end() < input.length() && input.charAt(matcher.end()) == '-') {
                result.append('-');
            }
        }

        return result.toString();
    }
    

    // Create random process


    // Create ui
}