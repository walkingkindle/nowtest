package com.seleniumtest;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v113.indexeddb.model.Key;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.netty.handler.timeout.TimeoutException;
import net.bytebuddy.asm.Advice.Enter;

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

        //testFunction("system","training","BookBalanceAnalysisSession.xml", "HeaderRowProcess.abs","com/bookshop",30,"BookShop");

        logMeIn("system", "training");
        Thread.sleep(1000);
        salesOrderTest();
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

    public static void salesOrderTest() throws InterruptedException{
        try{
        openMenu();
        search("Sales order");
        getNew();
        WebElement templateCode = findElementByName("TemplateCode");
        clickAndSendKeys(templateCode, Keys.chord(Keys.ALT,"L"));
        lookUpRowByText("Sales order");
        WebElement customer = findElementByName("OrderPartnerCustomerSupplierCode");
        clickAndSendKeys(customer, Keys.chord(Keys.ALT,"L"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath( String.format("//div[text()='%s']","Customer")))).click();
        //lookUpRowByText("Fashion House");
        WebElement lifeCycle = findElementByName("LifeCycleCode");
        clickAndSendKeys(lifeCycle, Keys.chord(Keys.ALT,"L"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath( String.format("//div[text()='%s']","002")))).click();
        clickAndSendKeys(lifeCycle, Keys.chord(Keys.ALT, Keys.F12));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Agents']"))).click();
        WebElement agentInput = findElementByName("Agent1Code");
        clickAndSendKeys(agentInput, Keys.chord(Keys.ALT, "L"));
        WebElement agentSubcode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='001'][ancestor::*[contains(@class, 'ABS_win-lookup')]]")));
        //clickOnMany(salesOrderSubCodes);
        agentSubcode.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Line']"))).click();
        WebElement lineTempCode = findElementByName("LineTemplateCode");
        clickAndSendKeys(lineTempCode, Keys.chord(Keys.ALT, "L"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath( "//div[text() = \"DNM\"]"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath( "//span[text()=\"Refresh\"]"))).click();
        WebElement subcode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Subcode06")));
        clickAndSendKeys(subcode, Keys.chord(Keys.ALT,"L"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()= 'Lookup (Alt+L)']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='001' and contains(@class, 'x-grid-cell-inner')]"))).click();
        }catch(org.openqa.selenium.ElementClickInterceptedException e){
            if(isElementPresent(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@id,'messagebox')]"))))){
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='No']"))).click();

            }
        }
    }    



    //-----------------------------------TODOS--------------------------------
    // 1. Create function that enters and goes inside sales order.
    // 2. The functions clicks on the necessary fields and presses next
    // 3.  The functions fills all the mandatory fields
    // 4. Presses save or "next" again
    // 5. If there is an error prompt ask the user what to do about the error.
    //6. Create a different function where the user has parameters regarding sales order attributes, and another one where the inputs are just random, for the sake of the testing.


   





    //------------------------------------------------------------------------------------- HELPER METHODS -------------------------------------------------------------
    
    public static void lookUpEach()


    public static void manyLookUpFunction(String inputString) throws InterruptedException{
        for (int i = 0; i < 7; i++){
        Pattern pattern = Pattern.compile("//d+");
        Matcher matcher = pattern.matcher(inputString);
        int number = Integer.parseInt(matcher.group());

        int increasedNumber = number - 1;

        String resultString = inputString.replace(inputString.valueOf(number), String.valueOf(increasedNumber));
        try{

        System.out.println("Clicked on " + resultString);
        }catch(org.openqa.selenium.TimeoutException e){
            break;
        }
        }

    }


    public static boolean isElementPresent(WebElement element) {
        try {
            // Attempt to find the element
            element.isDisplayed();
            return true; // Element found, return true
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false; // Element not found, return false
        }
    }


    public static void clickAndSendKeys(WebElement element, String key, Keys... additionalKeys) throws InterruptedException {
        element.click();
        element.sendKeys(key);

        for (Keys additionalKey : additionalKeys) {
            element.sendKeys(additionalKey);
        }
    }
    //use if anything else isn't possible
    public static void clickOnMany(List<WebElement> elements) {
        for (WebElement element : elements) {
            try {
                // Check if the element is clickable
                WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(element));
                clickableElement.click();
                
                // If the element is clickable, click on it and break out of the loop
                //if (clickableElement != null) {
                    //clickableElement.click();
                    //break;
                //}
            try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Sales order - Agents']"))).click();
            }catch(org.openqa.selenium.TimeoutException i){
                return;
            }
            } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                // If ElementClickInterceptedException occurs, catch the exception and continue to the next iteration
                continue;
            }
        }
    }
    public static WebElement clickOnLast(List<WebElement> elements) {
        WebElement lastElement = elements.get(elements.size() - 1);
        return lastElement;
    }
    public static void lookUpRowByText(String query){
       wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath( String.format("//div[text()='%s' and @class]",query)))).click();

    }

    public static WebElement findElementByName(String name){
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
        return element;
    }
    public static void openMenu(){
        WebElement body = driver.findElement(By.tagName("body"));
        body.sendKeys(Keys.ALT + "M" + Keys.BACK_SPACE);
    }

    public static void search(String query) throws InterruptedException{
       WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-menu-inputEl"))); 
       clickAndSendKeys(search,query); 
       search.sendKeys(Keys.ENTER);

    }
    public static WebElement searchWithoutEntering(String query) throws InterruptedException{
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


