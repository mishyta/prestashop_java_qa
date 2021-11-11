package com.corp;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainPage extends BasePage {

    public final String URL = "http://prestashop-automation.qatestlab.com.ua/ru/";
    public final GoodsCard goodsCard = new GoodsCard(driver);

    @FindBy(css = ".currency-selector span.expand-more")
    private WebElement currencyDD;

    @FindBy(xpath = "//*[@class='thumbnail-container']//*[@class='price']")
    private WebElement productCardCurrency;

    @FindBy(css = "[name=\"s\"]")
    private WebElement searchInput;


    @FindBy(css = ".total-products")
    private WebElement totalSearchProducts;



    public MainPage(WebDriver driver) {
        super(driver);

    }

    public void changeCurrency(String value){
        currencyDD.click();
        driver.findElement(By.xpath("//*[@id=\"_desktop_currency_selector\"]//*[contains(text(),'"
                + value +"')]")).click();
    }

    public void assertPageCurrency(){
        char pageCurrency = currencyDD.getText().charAt(currencyDD.getText().length()-1);
        char productCurrency = productCardCurrency.getText().charAt(productCardCurrency.getText().length()-1);
        assert  pageCurrency == productCurrency;
    }


    public void search(String value){
        searchInput.sendKeys(value);
        searchInput.sendKeys(Keys.ENTER);
    }

    private int getValueTotalSearchProducts(){
        return Character.getNumericValue(totalSearchProducts.getText().charAt(totalSearchProducts.getText().length()-2));
    }

    public void assertTotalResultSearch(){
        assert goodsCard.getTotalNumberOfGoodsAtPage() == getValueTotalSearchProducts();
    }





}
