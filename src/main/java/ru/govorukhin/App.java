package ru.govorukhin;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.govorukhin.firstTask.OnlinesimFreePhoneParser;
import ru.govorukhin.firstTask.entity.PhoneNumber;
import ru.govorukhin.secondTask.OnlinesimPriceParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));

        System.out.println("Задание №1\n");

        OnlinesimFreePhoneParser phoneParser = new OnlinesimFreePhoneParser();
        HashMap<String, ArrayList<PhoneNumber>> allPhoneNumbers = phoneParser.getAllPhoneNumbers();
        for (String country : allPhoneNumbers.keySet()){
            System.out.println(country);
            allPhoneNumbers.get(country).forEach(phoneNumber -> System.out.println("\t" + phoneNumber.getFull_number()));
        }

        System.out.println("\n\nЗадание №2\n");

        OnlinesimPriceParser priceParser = new OnlinesimPriceParser();
        HashMap<String,HashMap<String ,Double>> price = priceParser.getAllPriceList();

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(price);
        System.out.println(json);
        //Третье задание
        //см ru.govorukhin.thirdTask Simplify и SimplifySource

    }
}
