package com.corp;
import javafx.print.Collation;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;


public class ShopTest {

    public static WebDriver driver;
    public static MainPage page;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "/home/mknysh/drivers/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        page = new MainPage(driver);
    }


//        'Perform verification that the price of products in the'
//        ' "Popular Products" section is indicated in accordance '
//        'with the installed currency in the header of the site (USD, EUR, UAH). '
    @Test
    public void testCurrencyComparison() {
        /**
         *  Perform verification that the price of products in the
         *  "Popular Products" section is indicated in accordance
         *  with the installed currency in the header of the site (USD, EUR, UAH).
         */
        page.openPage(page.URL);
        page.changeCurrency("UAH");
        page.assertPageCurrency();
    }

    @Test
    public void testTotalSearchProducts(){
        /**
         *  Take the check that the page "Search results" contains the inscription
         *  "Goods: X", where X is the number of truly found items.
         */
        page.openPage(page.URL);
        page.search("dress");
        page.countElements(page.goods);
        page.aseertTotalResultSearch();

    }

    @AfterClass
    public static void tearDown() {

      //  driver.quit();
    }

}
