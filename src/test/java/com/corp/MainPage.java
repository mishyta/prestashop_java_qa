package com.corp;

import com.google.common.collect.Ordering;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    public enum PageCurrency{
        $,
        €,
        ₴
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

//    public enum PageSortByPrices extends PageSortBy{
//
//    }


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
        assert getTotalNumberOfGoodsAtPage() == getValueTotalSearchProducts();
    }

    public int getTotalNumberOfGoodsAtPage(){
        return countElements(goodsCards);
    }

    public void assertGoodsCardsPriceMatchPageCurrency(){
        for (WebElement goods: goodsCards){
            assert GC.getCurrency(goods) == '$';
        }
    }

    public void sortBy(final PageSortBy sortByValue){

        changeDDValue(sortDD, sortDDValues, sortByValue.getValue());

        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return sortDD.getText().startsWith(sortByValue.getValue());
            }
        });

    }

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

    public void assertGoodsDiscountMatches(){

    }

}
