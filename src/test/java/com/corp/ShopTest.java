package com.corp;

import org.junit.*;
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
    public  void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        page = new MainPage(driver);
    }


    /**
     *  Perform verification that the price of products in the
     *  "Popular Products" section is indicated in accordance
     *  with the installed currency in the header of the site (USD, EUR, UAH).
     */

    @Test
    public void testCurrencyComparison() {

        page.openPage(page.URL);
        page.changeCurrency("UAH");
        page.assertPageCurrency();


    }

    /**
     *  Take the check that the page "Search results" contains the inscription
     *  "Goods: X", where X is the number of truly found items.
     */

    @Test
    public void testTotalSearchProducts(){

        page.openPage(page.URL);
        page.search("dress");
        page.countElements(page.goodsCard.goods);
        page.assertTotalResultSearch();

    }

    /**
     * Check that the price of all the results shown is displayed in dollars.
     */
    @Test
    public void test_check_product_cards_currency(){

        page.openPage(page.URL);
        page.changeCurrency("USD");
        page.search("dress");
        page.goodsCard.assertGoodsCardsPriceMatchPageCurrency();

    }

    @After
    public void tearDown() {
        System.out.println(driver);
        driver.quit();
    }

}
