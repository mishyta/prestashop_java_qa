package com.corp;

import com.google.common.collect.Ordering;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.corp.GoodsCard.goodsCards;


/**
 * <h1>POM MainPage</h1>
 */
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
    private static WebElement sortDD;

    @FindBy (css = "div.products-sort-order div.dropdown-menu")
    private WebElement sortDDValues;


    public enum PageCurrency{
        USD,
        EUR,
        UAH
    }

    public enum PageSortBy {

        NameAtoZ("Названию: от А к Я"),
        NameZtoA("Названию: от Я к А"),
        PriceHtoL("Цене: от высокой к низкой"),
        PriceLtoH("Цене: от низкой к высокой"),
        Relevance("Релевантность");


        private final String value;

        PageSortBy(String value) {
            this.value = value;
        }


    }

    public MainPage(WebDriver driver) {
        super(driver);

    }

    /**
     * Check sort selected
     * @param by PageSortBy value for verification
     * @return true if selected / false if not
     */
    @Step("Check sort selected")
    private static boolean checkSortSelected(PageSortBy by){
        return sortDD.getText().startsWith(by.value);
    }



    /**
     * Change page currency
     * @param currency for what value change page currency
     */
    @Step("Change page currency to: {currency}")
    public void changeCurrency(PageCurrency currency){
        changeDDValue(currencyDD,currencyDDValues, currency.toString());
    }

    /**
     * @return page currency
     */
    public  String getPageCurrency(){
        return String.valueOf(currencyDD.getText().charAt(currencyDD.getText().length()-1));
    }

    /**
     * Assert Selected page currency with 1st goods
     */
    @Step("Assert page currency equals")
    public void assertPageCurrency(){
        Assert.assertEquals(getPageCurrency(),GC.getPriceCurrency(goodsCards.get(1)));
    }

    /**
     * Search on page
     * @param value for search
     */
    @Step("Search: \"{value}\"")
    public void search(String value){
        searchInput.sendKeys(value+Keys.ENTER);
    }

    /**
     * @return Get value from the goods shown counter
     */
    @Step("Get value from the goods shown counter")
    private int getValueTotalSearchProducts(){
        return Character.getNumericValue(totalSearchProducts.getText().charAt(totalSearchProducts.getText().length()-2));
    }

    /**
     * Assert that total number of the goods shown equals the goods shown counter
     */
    @Step("Assert that total number of the goods shown equals the goods shown counter")
    public void assertTotalResultSearch(){
        Assert.assertEquals(getTotalNumberOfGoodsAtPage(),getValueTotalSearchProducts());
    }

    /**
     * Counting the goods shown
     * @return number of goods present on page
     */
    @Step("Counting the goods shown ")
    public int getTotalNumberOfGoodsAtPage(){
        return countElements(goodsCards);
    }

    /**
     * Assert that goods that present on page currencies equals page currency
     */
    @Step("Assert that goods currencies equals page currency")
    public void assertGoodsCardsPriceMatchPageCurrency(){
//        for (WebElement goods: goodsCards){
//            Assert.assertEquals(GC.getPriceCurrency(goods), getPageCurrency());
//        }
        goodsCards.forEach(goods -> Assert.assertEquals(GC.getPriceCurrency(goods), getPageCurrency()));
    }

    /**
     * Sort products
     * @param sortByValue which selected to sort page
     */
    @Step("Sort products: \"{sortByValue.value}\"")
    public void sortBy(PageSortBy sortByValue){

        changeDDValue(sortDD, sortDDValues, sortByValue.value);

        (new WebDriverWait(driver, 10)).until((ExpectedCondition<Boolean>) d -> sortDD.getText().startsWith(sortByValue.value));

    }

    /**
     * @return true if sort by order / false if not
     */
    private boolean checkSortByOrder(){
        if (checkSortSelected(PageSortBy.PriceHtoL)) {
            return Ordering.natural().reverse().isOrdered(GC.getRegPrices());
        }
        else if (checkSortSelected(PageSortBy.NameAtoZ)) {
            return Ordering.natural().isOrdered(GC.getTitles());
        }
        else return false;
    }


    /**
     * @return true if sort not orderly / false if not
     */
    private boolean assertSortNotOrderly(){
        if (checkSortSelected(PageSortBy.PriceLtoH)) {
            return Ordering.natural().isOrdered(GC.getRegPrices());
        }
        else if (checkSortSelected(PageSortBy.NameZtoA)) {
            return Ordering.natural().reverse().isOrdered(GC.getTitles());
        }
        else return false;
    }


    /**
     * Assert sorting
     */
    @Step("Assert sorting")
    public void assertSort(){
        if (checkSortSelected(PageSortBy.PriceHtoL) | checkSortSelected(PageSortBy.NameAtoZ)){
            Assert.assertTrue(checkSortByOrder(), "Goods are not sorted ");
        }
        else if (checkSortSelected(PageSortBy.PriceLtoH) | checkSortSelected(PageSortBy.NameZtoA)){
            Assert.assertTrue(assertSortNotOrderly(), "Goods are not sorted ");
        }
    }

    /**
     * Assert that goods with discount have:
     *      - have percents in discount;
     *      - have price before discount;
     *      - have price after discount.
     */
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

    /**
     * Assert that new price matches discount
     */
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
