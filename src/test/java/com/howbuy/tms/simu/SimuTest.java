package com.howbuy.tms.simu;

import com.howbuy.simu.LoginPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by yang.zhou on 2017/9/29.
 */
public class SimuTest {

    private WebDriver driver;

    private String tmsCounterUrl = "http://192.168.221.216:20380/tms-counter-console/tmscounter/html/index.html";

    private static final Log logger = LogFactory.getLog(SimuTest.class);

    @BeforeClass
    public void setDriverClass() throws Exception {
        System.setProperty("webdriver.chrome.driver", "D:\\selenium\\chromedriver_2.31\\chromedriver.exe");
        logger.info("========开始执行========");

    }

    @BeforeMethod
    public void setUp() throws Exception {
        driver = new ChromeDriver();
    }

    @Test
    public void loginSimuWeb() throws Exception{
        LoginPage login = PageFactory.initElements(driver, LoginPage.class);
        login.get("http://192.168.221.216:15080/trade/login/login.htm?targeturl=http://192.168.221.216:4085/newpcsm/buylist.html");
        login.login("321183198707210772", "qq1111");
    }


    @AfterClass
    public void tearDown() throws Exception {
        logger.info("==========测试结束==========");
    }
}
