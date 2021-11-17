package com.corp;

import com.google.common.collect.Ordering;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import static com.corp.GoodsCard.goodsCards;


public class MainPage extends BasePage {

    public final String URL = "http://prestashop-automation.qatestlab.com.ua/ru/";
    public final GoodsCard GC = new GoodsCard(driver);

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

    @SuppressWarnings("NonAsciiCharacters")
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
    @Step("Change page currency to: {currency}")
    public void changeCurrency(PageCurrency currency){
        changeDDValue(currencyDD,currencyDDValues, currency.toString());
    }

    public  String getPageCurrency(){
        return String.valueOf(currencyDD.getText().charAt(currencyDD.getText().length()-1));
    }

    @Step("Assert pag currency equals")
    public void assertPageCurrency(){
        Assert.assertEquals(getPageCurrency(),GC.getPriceCurrency(goodsCards.get(1)));
    }

    @Step("Search: \"{value}\"")
    public void search(String value){
        searchInput.sendKeys(value+Keys.ENTER);
    }

    @Step("Get value from the goods shown counter")
    private int getValueTotalSearchProducts(){
        return Character.getNumericValue(totalSearchProducts.getText().charAt(totalSearchProducts.getText().length()-2));
    }

    @Step("Assert that total number of the goods shown equals the goods shown counter")
    public void assertTotalResultSearch(){
        assert getTotalNumberOfGoodsAtPage() == getValueTotalSearchProducts();
    }

    @Step("Counting the goods shown ")
    public int getTotalNumberOfGoodsAtPage(){
        return countElements(goodsCards);
    }

    @Step("Assert that goods currencies equals page currency")
    public void assertGoodsCardsPriceMatchPageCurrency(){
        for (WebElement goods: goodsCards){
            Assert.assertEquals(GC.getPriceCurrency(goods), getPageCurrency());
        }
    }

    @Step("Sort products: \"{sortByValue.value}\"")
    public void sortBy(PageSortBy sortByValue){

        changeDDValue(sortDD, sortDDValues, sortByValue.getValue());

        (new WebDriverWait(driver, 10)).until((ExpectedCondition<Boolean>) d -> sortDD.getText().startsWith(sortByValue.getValue()));

    }

    @Step("Assert sorting")
    public void assertSort(){
        if (sortDD.getText().startsWith(PageSortBy.PriceHtoL.getValue())){
            assert Ordering.natural().reverse().isOrdered(GC.getRegPrices());
        }
        else if (sortDD.getText().startsWith(PageSortBy.PriceLtoH.getValue())){
            assert Ordering.natural().isOrdered(GC.getRegPrices());
        }
        else if (sortDD.getText().startsWith(PageSortBy.NameAtoZ.getValue())){
            assert Ordering.natural().isOrdered(GC.getTitles());
        }
        else if (sortDD.getText().startsWith(PageSortBy.NameZtoA.getValue())){
            assert Ordering.natural().reverse().isOrdered(GC.getTitles());
        }
        else {
            System.out.println("Selected sorting are not checked");
        }
    }

    @Step("Assert that goods with discount have values")
    public void assertDiscountedGoodsForContainsValues(){
        for(WebElement goods:goodsCards){
            if(GC.checkDiscountExists(goods)){
                Assert.assertTrue(GC.checkPercentsAtDiscountExists(goods),
                        GC.getTitle(goods) + " don't have \"%\" at discount");
                Assert.assertTrue(GC.checkRegPriceExists(goods),
                        GC.getTitle(goods) + " don't have regular price");
                Assert.assertTrue(GC.checkPriceExists(goods),
                        GC.getTitle(goods) + "don't have price");
            }
        }
    }

    @Step("Verification of Discount")
    public void assertGoodsDiscountMatches(){
        for (WebElement goods:goodsCards){
            if(GC.checkDiscountExists(goods)){
                BigDecimal discount = BigDecimal.valueOf(GC.getDiscount(goods))
                        .setScale(2, RoundingMode.HALF_UP);
                Assert.assertEquals(discount,GC.calculateDiscount(goods));
            }
        }

    }

}
