package com.baidu;

/**
 * @author summe
 * @date 2018/3/11
 */

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class BaiduTest {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {

//        System.setProperty("webdriver.chrome.driver","D:\\selenium\\chromedriver_2.36\\chromedriver.exe");
//        driver = new ChromeDriver();

        System.setProperty("webdriver.gecko.driver","D:\\selenium\\geckodriver_v0.19.1\\geckodriver.exe");
        driver = new FirefoxDriver();
        baseUrl = "https://www.katalon.com/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testUntitledTestCase() throws Exception {
        driver.get("https://www.baidu.com/");
        driver.findElement(By.id("kw")).click();
        driver.findElement(By.id("kw")).clear();
        driver.findElement(By.id("kw")).sendKeys("python");
        driver.findElement(By.id("kw")).sendKeys(Keys.ENTER);
        driver.findElement(By.linkText("Python 基础教程 | 菜鸟教程")).click();
        // ERROR: Caught exception [ERROR: Unsupported command [selectWindow | win_ser_1 | ]]
        driver.findElement(By.linkText("Python 条件语句")).click();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
