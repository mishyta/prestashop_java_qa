package com.corp;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class GoodsCard extends BasePage{


    @FindBy(css = ".thumbnail-container")
    public static List<WebElement> goodsCards;


    private enum PriceAttributes {

        Discount(By.cssSelector("span.discount-percentage")),
        Price(By.cssSelector("span.price")),
        RegPrice(By.cssSelector("span.regular-price"));
        private final By value;

        PriceAttributes(By value) {
            this.value = value;
        }

        public By getValue() {
            return value;
        }
    }

    private enum StrAttributes {

        Discount(By.cssSelector("span.discount-percentage")),
        Price(By.cssSelector("span.price")),
        Title(By.cssSelector("h1.product-title"));
        private final By value;

        StrAttributes(By value) {
            this.value = value;
        }

        public By getValue() {
            return value;
        }
    }

    public GoodsCard(WebDriver driver) {
        super(driver);
    }

    /**
     * method to work with PriceAttributes of goods
     * @param element - an element from which to take an attribute
     * @param attribute - which attribute to take
     * @return double attribute
     */
    private static double getAttribute(WebElement element, PriceAttributes attribute ){
        String atr = element.findElement(attribute.getValue()).getText();
        StringBuilder stringBuffer = new StringBuilder(atr);
        stringBuffer.deleteCharAt(stringBuffer.length()-1);
        return Float.parseFloat(stringBuffer.toString().replace(',','.'));

    }

    /**
     * method to work with StrAttributes of goods
     * @param element - an element from which to take an attribute
     * @param attribute - which attribute to take
     * @return String attribute
     */
    @Step("{attributes.value}")
    private String getAttribute(WebElement element, StrAttributes attribute){
        return element.findElement(attribute.getValue()).getText();
    }

    @Step("Get price of goods card")
    public static double getPrice(WebElement goodsCard) {
        return getAttribute(goodsCard,PriceAttributes.Price);
    }

    @Step("Get regular price of goods card")
    public static double getRegPrice(WebElement goodsCard) {
        return getAttribute(goodsCard,PriceAttributes.RegPrice);
    }

    @Step("Get discount of goods card")
    public double getDiscount(WebElement goodsCard) {

        return (getAttribute(goodsCard,PriceAttributes.Discount) * -0.01);
    }

    @Step("Get tittle of goods card")
    public String getTitle(WebElement goodsCard){
        return getAttribute(goodsCard, StrAttributes.Title);
    }

    @Step("Get price currency of goods card")
    public String getPriceCurrency(WebElement goodsCard) {
        String str = getAttribute(goodsCard,StrAttributes.Price);
        return String.valueOf(str.charAt(str.length()-1));
    }

    @Step("Get list of prices the goods shown")
    public  List<Double> getPrices(){
        List<Double> result = new ArrayList<>();
        goodsCards.forEach(goods -> result.add(getPrice(goods)));
        return result;
    }



    @Step("Get list of regular prices the goods shown")
    public  List<Double> getRegPrices(){
        List<Double> result = new ArrayList<>();
        goodsCards.forEach(goods -> {
            if(checkRegPriceExists(goods)){
                result.add(getRegPrice(goods));
            }
            else{
                result.add(getPrice(goods));
            }
        });
        return result;
    }

    @Step("Get list of tittles the goods shown")
    public List<String> getTitles(){
        List<String> result = new ArrayList<>();
        goodsCards.forEach(goods -> result.add(getTitle(goods)));
        return result;
    }

    @Step("{attribute.value}")
    public boolean checkAttributeExists(WebElement goods, PriceAttributes attribute){
        return !goods.findElements(attribute.value).isEmpty();
    }

    @Step("Checking existence of price")
    public boolean checkPriceExists(WebElement goods){
        return checkAttributeExists(goods,PriceAttributes.Price);

    }

    @Step("Checking existence of discount")
    public boolean checkDiscountExists(WebElement goods){
        return checkAttributeExists(goods,PriceAttributes.Discount);
    }

    @Step("Checking existence of regular price")
    public boolean checkRegPriceExists(WebElement goods){
        return checkAttributeExists(goods,PriceAttributes.RegPrice);
    }

    @Step("Checking existence of percent in discount")
    public boolean checkPercentsAtDiscountExists(WebElement goods){
        String discStr = getAttribute(goods,StrAttributes.Discount);
        return discStr.endsWith("%");
    }

    @Step("Calculate discount")
    public BigDecimal calculateDiscount(WebElement goods){


        return BigDecimal.valueOf((getRegPrice(goods) - getPrice(goods)) / getRegPrice(goods))
                .setScale(2, RoundingMode.HALF_UP);
    }







}
