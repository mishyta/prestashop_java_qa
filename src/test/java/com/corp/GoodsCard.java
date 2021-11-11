package com.corp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class GoodsCard extends BasePage{


    @FindBy(css = ".thumbnail-container")
    public static List<WebElement> goods;



    public GoodsCard(WebDriver driver) {
        super(driver);
    }


    public  ArrayList<String> getPrices(){

        final By price = By.cssSelector("span.price");
        ArrayList<String> result = new ArrayList<String>();

        for(WebElement good:goods){
            result.add(good.findElement(price).getText());
        }

        return result;
    }

    public int getTotalNumberOfGoodsAtPage(){
        return countElements(goods);
    }


    public void assertGoodsCardsPriceMatchPageCurrency(){
        ArrayList<String> goodsPricesCurrency = getPrices();
        for (String price:goodsPricesCurrency){
            assert price.charAt(price.length()-1) == '$';
        }
    }


}