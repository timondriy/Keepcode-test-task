package ru.govorukhin.secondTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.HashMap;

public class OnlinesimPriceParser {
    private final String PRICE_LIST_URL = "http://onlinesim.ru/price-list";
    public HashMap<String, HashMap<String, Double>> getAllPriceList() {
        WebDriverManager.chromedriver().setup();
        HashMap<String, HashMap<String, Double>> result = new HashMap<>();

        HashMap<String, String> countryNumbersAndNames = getCountryNumbersAndNames();

        countryNumbersAndNames.forEach((countryNumber, countryName) -> {
            result.put(countryName, getCountryPrice(countryNumber));
        });

        return result;
    }

    private HashMap<String, Double> getCountryPrice(String countryNumber) {
        HashMap<String,Double> result = new HashMap<>();

        //TODO: set driver to silent mode
        WebDriver driver = new ChromeDriver();

        driver.get(PRICE_LIST_URL+"?country="+countryNumber+"&type=receive");
        Document doc = Jsoup.parse(driver.getPageSource());
        driver.quit();

        Elements priceNames = doc.select("span[class=price-name]");
        for (Element name : priceNames) {
            String serviceName = name.text();
            String priceText = name.nextElementSibling().text();
            Double priceInt = Double.parseDouble(priceText.substring(0,priceText.length()-1));
            result.put(serviceName, priceInt);
        }
        return result;
    }

    private HashMap<String, String> getCountryNumbersAndNames() {
        HashMap<String,String> result = new HashMap<>();

        //TODO: set driver to silent mode
        WebDriver driver = new ChromeDriver();

        driver.get(PRICE_LIST_URL);
        Document doc = Jsoup.parse(driver.getPageSource());
        driver.quit();

        Elements countryNumbers = doc.select("a[id~=country-*\\d+]");
        for (Element number : countryNumbers) {
            String id = number.attr("id");
            String countryName = number.select("span[class=country-name]").text();
            result.put(id.substring(8), countryName);
        }
        return result;
    }
}
