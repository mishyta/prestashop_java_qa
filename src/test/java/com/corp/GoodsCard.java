package com.corp;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoodsCard extends BasePage{


    @FindBy(css = ".thumbnail-container")
    public static List<WebElement> goodsCards;


    private enum PriceAttributes {

        Price(By.cssSelector("span.price")),
        RegPrice(By.cssSelector("span.regular-price")),
        Discount(By.cssSelector("span.discount-percentage"));
        private final By value;

        PriceAttributes(By value){
            this.value = value;
        }
        public By getValue(){
            return value;
        }
    }

    private enum StrAttributes{

        Price(By.cssSelector("span.price")),
        Title(By.cssSelector("h1.product-title")),
        Discount(By.cssSelector("span.discount-percentage"));
        private final By value;

        StrAttributes(By value){
            this.value = value;
        }
        public By getValue(){
            return value;
        }
    }

    public GoodsCard(WebDriver driver) {
        super(driver);
    }

    private float getAttribute(WebElement element, PriceAttributes attribute ){
        String atr = element.findElement(attribute.getValue()).getText();
        StringBuffer stringBuffer = new StringBuffer(atr);
        stringBuffer.deleteCharAt(stringBuffer.length()-1);
        return Float.parseFloat(stringBuffer.toString().replace(',','.'));

    }

    private String getAttribute(WebElement element, StrAttributes attributes){
        return element.findElement(attributes.getValue()).getText();
    }

    public float getPrice(WebElement goodsCard) {
        return getAttribute(goodsCard,PriceAttributes.Price);
    }

    public float getRegPrice(WebElement goodsCard) {
        return getAttribute(goodsCard,PriceAttributes.RegPrice);
    }

    public float getDiscount(WebElement goodsCard) {
        return (float) (getAttribute(goodsCard,PriceAttributes.Discount) * -0.01);
    }

    public String getTitle(WebElement goodsCard){
        return getAttribute(goodsCard, StrAttributes.Title);
    }

    public char getCurrency(WebElement goodsCard){
        String prc = getAttribute(goodsCard, StrAttributes.Price);
        return prc.charAt(prc.length()-1);
    }

    public char getPriceCurrency(WebElement goodsCard) {
        String str = getAttribute(goodsCard,StrAttributes.Price);
        return str.charAt(str.length()-1);
    }

    public  List<Float> getPrices(){

        List<Float> result = new ArrayList<>();

        for(WebElement goods: goodsCards){
            result.add(getPrice(goods));
        }

        return result;
    }

    public  List<Float> getRegPrices(){

        List<Float> result = new ArrayList<>();

        driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);

        for(WebElement goods: goodsCards){
            try {
                goods.findElement(PriceAttributes.RegPrice.value);
            }
            catch (NoSuchElementException e){
                result.add(getPrice(goods));
                continue;
            }
            result.add(getRegPrice(goods));

        }


        return result;
    }

    public List<String> getTitles(){

        List<String> result = new ArrayList<>();

        for(WebElement goods: goodsCards){
            result.add(getTitle(goods));
        }

        return result;
    }
    @ImplicitlyWait(0)
    public Boolean checkPriceExists(WebElement goods){
        driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);

        return !goods.findElements(PriceAttributes.Price.value).isEmpty();

    }
    public Boolean checkDiscountExists(WebElement goods){
        driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);

        return !goods.findElements(PriceAttributes.Discount.value).isEmpty();
    }

    public Boolean checkRegPriceExists(WebElement goods){
        driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);

        return !goods.findElements(PriceAttributes.RegPrice.value).isEmpty();
    }

    public Boolean checkPercentsAtDiscountExists(WebElement goods){
        String discStr = getAttribute(goods,StrAttributes.Discount);
        return discStr.endsWith("%");
    }

    public BigDecimal calculateDiscount(WebElement goods){

        BigDecimal d = new BigDecimal(getDiscount(goods))
                .setScale(2, RoundingMode.HALF_UP);
        System.out.println(d);
        return new BigDecimal((getRegPrice(goods)-getPrice(goods))/getRegPrice(goods))
                .setScale(2, RoundingMode.HALF_UP);
    }







}
