package com.corp;

import io.qameta.allure.Description;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.corp.EnvironmentDetector.JNKENV;
import static com.corp.MainPage.PageCurrency;
import static com.corp.MainPage.PageCurrency.*;
import static com.corp.MainPage.PageSortBy;
import static com.corp.MainPage.PageSortBy.*;

@Test(description = "Shop test")
public class ShopTest {

    public  WebDriver driver;
    public  MainPage page;



    @BeforeTest
    public  void setProp(){
        System.setProperty("webdriver.chrome.driver", "/home/mknysh/drivers/chromedriver");
    }

    @BeforeMethod()
    public  void setup() throws MalformedURLException {


        if(JNKENV) {

            // if env = jenkins run test in remote browser, use selenoid

            // Selenoid options
            String SelenoidIP = "10.8.0.46";
            int SelenoidPort = 4444;
            Map<String, Object> SelenoidOptions = new HashMap<>();
            SelenoidOptions.put("enableVNC", true);
            SelenoidOptions.put("enableVideo", true);

            // Desired capabilities
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("browserName", "chrome");
            capabilities.setCapability("browserVersion", "95.0");
            capabilities.setCapability("selenoid:options", SelenoidOptions);

            driver = new EventFiringWebDriver(new RemoteWebDriver(
                    URI.create(String.format("http://%s:%d/wd/hub", SelenoidIP, SelenoidPort)).toURL(), capabilities))
                    .register(new Listener());
        }
        else{
            driver = new ChromeDriver();
        }

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
                {USD},
                {EUR},
                {UAH},
        };
    }

    /**
     *  Perform verification that the price of products in the
     *  "Popular Products" section is indicated in accordance
     *  with the installed currency in the header of the site (USD, EUR, UAH).
     */

    @Test(dataProvider = "currencies",description = "Test currency conformity")
    public void currencyConformity(PageCurrency currency) {

        page.changeCurrency(currency);
        page.assertPageCurrency();

    }

    /**
     *  Take the check that the page "Search results" contains the inscription
     *  "Goods: X", where X is the number of truly found items.
     */

    @Test(description = "Total search products")
    public void totalSearchProducts(){

        page.search("dress");
        page.countElements(page.GC.goodsCards);
        page.assertTotalResultSearch();

    }

    /**
     * Check that the price of all the results shown is displayed in dollars.
     */
    @Test(dataProvider = "currencies", description = "Check product cards currency")
    public void checkProductCardsCurrency(PageCurrency currency){

        page.changeCurrency(currency);
        page.search("dress");
        page.assertGoodsCardsPriceMatchPageCurrency();

    }

    @Test(dataProvider = "sortByValues", description = "Check sort by")
    public void checkSortBy(PageSortBy sortByValue)  {

        page.search("dress");
        page.sortBy(sortByValue);
        page.assertSort();

    }

    /**
     *  For discount products, a percentage discount is indicated along with the price before and '
     *  after the discount.
     */

    @Test(description = "Check goods with discount contains values")
    public void CheckGoodsWithDiscountContainsValues(){
        page.search("dress");
        page.assertDiscountedGoodsForContainsValues();

    }

    /**
     * Check that the price before and after the discount coincides with the specified discount size.
     */

    @Test(description = "Check goods discount matching")
    public void checkGoodsDiscountMatching(){

        page.search("dress");
        page.assertGoodsDiscountMatches();

    }



    @AfterMethod(description = "browser teardown")
    public void tearDown() {
        driver.quit();
    }

}
