package com.corp;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BasePage {

    public WebDriver driver;

    public BasePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver; }

    @Step("Open page: {url}")
    public void openPage(String url){
        driver.get(url);
    }
    public int countElements(List<WebElement> elements){
        return elements.size();
    }

    /**
     * ddvalues is list with dd options:
     *  <ul class="dropdown-menu hidden-sm-down" ...>
     *      <li>...</li>
     *      <li>
     *          <a> value </a>
     *      </li>
     *      <li>...</li>
     *  </ul>
     *
     *  We search at this element needed option for click:
     *      ddvalues.findElement(By.xpath("//a[contains(text(),'" + value + "')]")).click();
     *
     *  value = WebElement text
     */
    public void changeDDValue(WebElement dd,WebElement ddvalues, String value){

        dd.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'" + value + "')]")));
        ddvalues.findElement(By.xpath("//a[contains(text(),'" + value + "')]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(dd));
    }



}
