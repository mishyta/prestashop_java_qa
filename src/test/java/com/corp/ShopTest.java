package com.corp;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

import static com.corp.MainPage.PageCurrency.₴;
import static com.corp.MainPage.PageCurrency.$;
import static com.corp.MainPage.PageSortBy.*;


public class ShopTest {

    public  WebDriver driver;
    public  MainPage page;

    @BeforeTest
    public  void setProp(){
        System.setProperty("webdriver.chrome.driver", "/home/mknysh/drivers/chromedriver");
    }

    @BeforeMethod
    public  void setup()  {

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        page = new MainPage(driver);
        page.openPage(page.URL);

    }


    /**
     *  Perform verification that the price of products in the
     *  "Popular Products" section is indicated in accordance
     *  with the installed currency in the header of the site (USD, EUR, UAH).
     */

    @Test
    public void currencyComparison() {

        page.changeCurrency(₴);
        page.assertPageCurrency();


    }

    /**
     *  Take the check that the page "Search results" contains the inscription
     *  "Goods: X", where X is the number of truly found items.
     */

    @Test
    public void totalSearchProducts(){

        page.search("dress");
        page.countElements(GoodsCard.goodsCards);
        page.assertTotalResultSearch();

    }

    /**
     * Check that the price of all the results shown is displayed in dollars.
     */
    @Test
    public void checkProductCardsCurrency(){

        page.changeCurrency($);
        page.search("dress");
        page.assertGoodsCardsPriceMatchPageCurrency();

    }

    @Test
    public void checkSortPriceHtoL()  {

        page.search("dress");
        page.sortBy(PriceHtoL);
        page.assertSort();


    }

    /**
     *  For discount products, a percentage discount is indicated along with the price before and '
     *  after the discount.
     */

    @Test
    public void CheckGoodsWithDiscountContainsValues(){
        page.search("dress");
        page.assertDiscountedGoodsForContainsValues();

    }

    /**
     * Check that the price before and after the discount coincides with the specified discount size.
     */

    @Test
    public void checkGoodsDiscountMatching(){

        page.search("dress");
        page.assertGoodsDiscountMatches();


    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

}
