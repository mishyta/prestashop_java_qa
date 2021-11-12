package com.corp;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class MainPage extends BasePage {

    public final String URL = "http://prestashop-automation.qatestlab.com.ua/ru/";
    public final GoodsCard goodsCard = new GoodsCard(driver);

    @FindBy(css = ".currency-selector span.expand-more")
    private WebElement currencyDD;

    @FindBy(css = ".currency-selector ul.dropdown-menu")
    private WebElement currencyDDValues;

    @FindBy(xpath = "//*[@class='thumbnail-container']//*[@class='price']")
    private WebElement productCardCurrency;

    @FindBy(css = "[name=\"s\"]")
    private WebElement searchInput;

    @FindBy(css = ".total-products")
    private WebElement totalSearchProducts;

    @FindBy (css = "div.products-sort-order")
    private WebElement sortDD;

    @FindBy (css = "div.products-sort-order div.dropdown-menu")
    private WebElement sortDDValues;

    public enum PageCurrency{
        USD,
        EUR,
        UAH
    }

    public enum PageSortBy{

        Relevance("Релевантность"),
        NameAtoZ("Названию: от А к Я"),
        NameZtoA("Названию: от Я к А"),
        PriceLtoH("Цене: от низкой к высокой"),
        PriceHtoL("Цене: от высокой к низкой");


        private final String value;

        PageSortBy(String value){
            this.value = value;
        }

        public String getValue(){
            return value;
        }
    }


    public MainPage(WebDriver driver) {
        super(driver);

    }

    public void changeCurrency(PageCurrency currency){
        changeDDValue(currencyDD,currencyDDValues, currency.toString());
    }

    public void assertPageCurrency(){
        char pageCurrency = currencyDD.getText().charAt(currencyDD.getText().length()-1);
        char productCurrency = productCardCurrency.getText().charAt(productCardCurrency.getText().length()-1);
        assert  pageCurrency == productCurrency;
    }


    public void search(String value){
        searchInput.sendKeys(value+Keys.ENTER);
//        searchInput.sendKeys(Keys.ENTER);
    }

    private int getValueTotalSearchProducts(){
        return Character.getNumericValue(totalSearchProducts.getText().charAt(totalSearchProducts.getText().length()-2));
    }

    public void assertTotalResultSearch(){
        assert goodsCard.getTotalNumberOfGoodsAtPage() == getValueTotalSearchProducts();
    }

    public void sortBy(PageSortBy by){
        changeDDValue(sortDD, sortDDValues, by.getValue());
    }
    public void assertSortByPrices(){

    }





}
