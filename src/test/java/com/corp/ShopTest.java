package com.corp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URI;

import static com.corp.MainPage.PageCurrency;
import static com.corp.MainPage.PageCurrency.*;
import static com.corp.MainPage.PageSortBy;
import static com.corp.MainPage.PageSortBy.*;

public class ShopTest {

    public  EventFiringWebDriver driver;
    public  MainPage page;



    @BeforeTest
    public  void setProp(){
        System.setProperty("webdriver.chrome.driver", "/home/mknysh/drivers/chromedriver");
    }

    @BeforeMethod
    public  void setup() throws MalformedURLException {



        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("browserVersion", "95.0");


        driver = new EventFiringWebDriver(
                new RemoteWebDriver(URI.create("http://0.0.0.0:4444/wd/hub")
                .toURL(), capabilities))
                .register(new Listener());

        driver.manage().window().maximize();
        page = new MainPage(driver);
        page.openPage(page.URL);

    }

    @DataProvider
    public Object[][] sortByValues() {
        return new Object[][] {
                {NameAtoZ},
                {NameZtoA},
                {PriceLtoH},
                {PriceHtoL}
        };
    }

    @DataProvider
    public Object[][] currencies() {
        return new Object[][] {
                {$},
                {€},
                {₴},
        };
    }

    /**
     *  Perform verification that the price of products in the
     *  "Popular Products" section is indicated in accordance
     *  with the installed currency in the header of the site (USD, EUR, UAH).
     */

    @Test(dataProvider = "currencies")
    public void currencyComparison(PageCurrency currency) {

        page.changeCurrency(currency);
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
    @Test(dataProvider = "currencies")
    public void checkProductCardsCurrency(PageCurrency currency){

        page.changeCurrency(currency);
        page.search("dress");
        page.assertGoodsCardsPriceMatchPageCurrency();

    }

    @Test(dataProvider = "sortByValues")
    public void checkSortBy(PageSortBy sortByValue)  {

        page.search("dress");
        page.sortBy(sortByValue);
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
