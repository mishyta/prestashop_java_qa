package com.corp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class BasePage {

    public WebDriver driver;

    public BasePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver; }

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
        ddvalues.findElement(By.xpath("//a[contains(text(),'" + value + "')]")).click();
    }



}
