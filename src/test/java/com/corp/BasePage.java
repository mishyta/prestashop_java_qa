package com.corp;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
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


}
