package com.howbuy.simu;

import com.howbuy.common.TestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;


/**
 * 高端产品购买页面
 * Created by yang.zhou on 2017/9/29.
 *
 * @author yang.zhou
 */
public class HighEndBuyPage extends BasePage {

    private static final Log logger = LogFactory.getLog(HighEndBuyPage.class);

    // 基金代码查询
    @FindBy(id = "searchFund_")
    private WebElement searchFundText;

    // 查询确定按钮
    @FindBy(id = "searchFundBtn_")
    private WebElement searchFundBtn;

    // 模态框
    @FindBy(className = "dialogLoad")
    private WebElement dialog;

    // 默认汇款方式
    @FindBy(xpath = "//p[contains(text(),'默认回款方式')]")
    private WebElement defaultRemitText;

    //选择储蓄罐
    @FindBy(linkText = "回款至储蓄罐")
    private WebElement remitBoxText;


    //选择回到银行卡
    @FindBy(linkText = "回款至银行卡")
    private WebElement remitCardText;


    // 暂无数据元素
    @FindBy(xpath = "//p[text()='暂无数据']")
    private WebElement noFund;

    // 购买按钮-可购买状态
    @FindBy(css = "a[href^='buyindex.html']")
    private WebElement buyIndexBtn;

    // 净购买金额
    @FindBy(id = "buyAmount")
    private WebElement buyAmountText;

    // 选择储蓄罐
    @FindBy(xpath = "//span[text()='储蓄罐']")
    private WebElement savingsBankLink;

    // 选择银行卡
    @FindBy(xpath = "//span[text()='银行卡代扣']")
    private WebElement bankCardLink;

    // 选择线下转账
    @FindBy(xpath = "//span[text()='银行转账划款']")
    private WebElement offlineTransferLink;

    // 选择银行卡
    @FindBy(css = "input[name=bank2]")
    private List<WebElement> bankCardRadioes;

    // 支行名称
    @FindBy(css = "input[data-bind*='bankSubName']")
    private WebElement bankSubNameText;

    // 电子合同签名
    @FindBy(xpath = "//p[text()='电子合同签订']")
    private List<WebElement> signingElecText;

    // 合同复选框
    @FindBy(id = "hetong")
    private WebElement hetongBox;

    // 检查所有合同选项
    @FindBy(id = "all")
    private WebElement allBox;

    // 检查是否存在勾选框
    @FindBy(id = "all")
    private List<WebElement> checkBox;

    // 下一步按钮
    @FindBy(linkText = "下一步")
    private WebElement nextStepBtn;

    // 交易密码
    @FindBy(css = "input[data-bind*='txPassword']")
    private WebElement txPasswordText;

    // 购买确认信息按钮
    @FindBy(linkText = "确认")
    private WebElement nextStepTwoBtn;

    // 手机号码
    @FindBy(css = "input[data-bind*='mobile']")
    private WebElement mobileText;

    // 获取短信验证码
    @FindBy(linkText = "获取短信验证码")
    private WebElement getVerifyCodeBtn;

    // 验证码倒计时
    @FindBy(partialLinkText = "秒")
    private WebElement getVerifyCode;

    // 验证码
    @FindBy(id = "verifyCode")
    private WebElement verifyCodeText;

    // 确认购买
    @FindBy(linkText = "确认购买")
    private WebElement checkVerifyCodeBtn;

    // 购买成功
    @FindBy(xpath = "//*[contains(text(),'购买申请')] | //span[text()='还需2步']")
    private WebElement buyingText;


    public HighEndBuyPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
    }

    /**
     * 查询产品
     */

    private void queryFund(String fundCode) {
        try {
            wait.until(invisibilityOf(dialog));
            searchFundText.sendKeys(fundCode);
            TestUtils.sleep1s();
            searchFundBtn.click();
            clickBuyButton();
        } catch (NoSuchElementException e) {
            logger.error("查询产品失败", e);
            throw new RuntimeException("查询产品失败");
        }

    }


    /**
     * 购买按钮
     */

    public void clickBuyButton() {

        try {
            wait.until(invisibilityOf(dialog));
            buyIndexBtn.click();
        } catch (NoSuchElementException n) {
            throw new RuntimeException("该产品无法购买.");
        }
    }


    /**
     * 1、填写订单
     *
     * @param buyAmount   净购买金额
     * @param paymentType 0-储蓄罐，1-银行转账划款，2-银行代扣
     * @param index       选择第几张银行卡
     */

    public void fillInOrder(String buyAmount, int paymentType, int index) {
        wait.until(invisibilityOf(dialog));
        TestUtils.sleep2s();

        if (isRemitPopup()) {
            selectRemitText();
            TestUtils.sleep1s();
        }

        int count = 0;

        while (true) {
            try {
                buyAmountText.sendKeys(buyAmount);
                break;
            } catch (Exception e) {
                logger.error("输入申请金额失败，等待2s重试.", e);
                TestUtils.sleep2s();
                count++;
            }
            if (count > 3) {
                logger.error("输入申请金额失败,重试次数：" + count);
                throw new TimeoutException("输入申请金额异常.");
            }
        }

        //选择储蓄罐、银行卡代扣或是银行转账划款

        if (paymentType == 0) {
            TestUtils.scrollEnd(driver);
            wait.until(elementToBeClickable(nextStepBtn)).click();
            return;
        }

        if (paymentType == 1) {
            offlineTransferLink.click();
        }

        if (paymentType == 2) {
            bankCardLink.click();
        }

        int size = bankCardRadioes.size();
        if (size > 1 && size >= index) {
            WebElement bankCardChecked = bankCardRadioes.get(index - 1);
            TestUtils.scrollTo(driver, bankCardChecked.getLocation().getY());
            if (!bankCardChecked.isSelected()) {
                logger.info("选择第" + index + "张银行卡支付.");
                bankCardChecked.click();
            }
        }
        TestUtils.scrollEnd(driver);
        bankSubNameText.sendKeys("上海南京西路支行");
        wait.until(elementToBeClickable(nextStepBtn)).click();
    }


    /**
     * 2、首次购买，需要签电子合同步骤,私募购买
     */

    public void signingElecContract() {
        logger.info("电子合同签名");
        wait.until(invisibilityOf(dialog));
        if (!hetongBox.isSelected()) {
            hetongBox.click();
        }
        TestUtils.sleep1s();
        nextStepBtn.click();
        // 私募投资者声明处理，如果不存在私募声明，跳过执行
        wait.until(invisibilityOf(dialog));
        if (checkBox.size() > 0) {
            logger.info("私募投资者声明-条款处理");
            if (!allBox.isSelected()) {
                allBox.click();
            }
            TestUtils.scrollEnd(driver);
            nextStepBtn.click();
        }
    }


    /**
     * 3、确认购买
     *
     * @param txPassword 交易密码，短信验证码默认111111
     */

    public void confirmPurchase(String txPassword) {
        wait.until(invisibilityOf(dialog));
        txPasswordText.sendKeys(txPassword);
        wait.until(elementToBeClickable(nextStepTwoBtn)).click();
        TestUtils.sleep2s();
        getVerifyCodeBtn.click();
        wait.until(visibilityOf(getVerifyCode));
        verifyCodeText.sendKeys("111111");
        wait.until(elementToBeClickable(checkVerifyCodeBtn)).click();
    }


    /**
     * 4、申请购买成功
     */

    public void buyIsSuccess(String fundCode, String amount) {
        try {
            wait.until(invisibilityOf(dialog));
            wait.until(visibilityOf(buyingText));
            logger.info("产品:" + fundCode + ".金额：" + amount + ". 购买成功!");
        } catch (TimeoutException t) {
            logger.error("产品:" + fundCode + ", 金额：" + amount + ". 购买失败!");
        }
        TestUtils.screenshort(driver, "产品"+fundCode+"购买结果");
    }


    private Boolean isSign() {
        return signingElecText.size() > 0;
    }


    /**
     * 是否存在回款弹出框
     */

    private Boolean isRemitPopup() {
        try {
            new WebDriverWait(driver, 2).until(visibilityOf(defaultRemitText));
            logger.info("需要选择回款协议");
            return true;
        } catch (TimeoutException t) {
            return false;
        }
    }


    /**
     * 选择回款协议
     */
    private void selectRemitText() {
        remitCardText.click();
    }


    /**
     * 购买买基金
     */

    public void buyHighFund(String fundCode, String buyAmount, int paymentType, int index, String txPassword) {
        openBuyListPage();
        queryFund(fundCode);
        fillInOrder(buyAmount, paymentType, index);
        if (isSign()) {
            signingElecContract();
        }
        confirmPurchase(txPassword);
        buyIsSuccess(fundCode, buyAmount);
    }


    /**
     * 使用银行卡代扣
     */

    public void buyHighFund(String fundCode, String buyAmount, int index) {
        buyHighFund(fundCode, buyAmount, 2, index, "121212");
    }


    /**
     * 使用银行卡代扣，需要传交易密码
     */

    public void buyHighFund(String fundCode, String buyAmount, String txPassword) {
        buyHighFund(fundCode, buyAmount, 2, 1, txPassword);
    }


    /**
     * 使用银行卡代扣，默认选择第一张银行卡
     */

    public void buyHighFund(String fundCode, String buyAmount) {
        buyHighFund(fundCode, buyAmount, 1);
    }


    /**
     * 使用银行卡转账
     */

    public void buyByBankTransfer(String fundCode, String buyAmount, int index) {
        buyHighFund(fundCode, buyAmount, 1, index, "121212");
    }


    /**
     * 使用银行卡转账，默认选择选择第一张银行卡
     */

    public void buyByBankTransfer(String fundCode, String buyAmount) {
        buyByBankTransfer(fundCode, buyAmount, 1);
    }


    /**
     * 使用储蓄罐支付
     */

    public void buyByPiggy(String fundCode, String buyAmount) {
        buyHighFund(fundCode, buyAmount, 0, 1, "121212");
    }

}