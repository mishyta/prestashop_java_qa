package com.corp;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;




public class ShopTest {

    public static WebDriver driver;
    public static MainPage page;

    @BeforeClass
    public static void setProp(){
        System.setProperty("webdriver.chrome.driver", "/home/mknysh/drivers/chromedriver");
    }

    @Before
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

        page.changeCurrency(MainPage.PageCurrency.UAH);
        page.assertPageCurrency();


    }

    /**
     *  Take the check that the page "Search results" contains the inscription
     *  "Goods: X", where X is the number of truly found items.
     */

    @Test
    public void totalSearchProducts(){

        page.search("dress");
        page.countElements(page.goodsCard.goods);
        page.assertTotalResultSearch();

    }

    /**
     * Check that the price of all the results shown is displayed in dollars.
     */
    @Test
    public void checkProductCardsCurrency(){

        page.changeCurrency(MainPage.PageCurrency.USD);
        page.search("dress");
        page.goodsCard.assertGoodsCardsPriceMatchPageCurrency();

    }

    @Test
    public void checkSortPriceHtoL(){

        page.search("dress");
        page.sortBy(MainPage.PageSortBy.PriceHtoL);

    }

    @After
    public void tearDown() {
        driver.quit();
    }

}
