package ru.govorukhin.firstTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.govorukhin.firstTask.entity.Country;
import ru.govorukhin.firstTask.entity.PhoneNumber;

public class OnlinesimFreePhoneParser {

    private final String FREE_COUNTRY_LIST_URL = "https://onlinesim.ru/api/getFreeCountryList";
    private final String FREE_PHONE_LIST_URL = "https://onlinesim.ru/api/getFreePhoneList?country=";

    public OnlinesimFreePhoneParser() {
    }

    public HashMap<String,ArrayList<PhoneNumber>> getAllPhoneNumbers() throws JsonProcessingException {
        HashMap<String,ArrayList<PhoneNumber>> result = new HashMap<>();

        ArrayList<Country> countryList = getCountryList();
        countryList.forEach(country -> {
            result.put(country.getCountry_text(), getCountryPhoneNumbers(country.getCountry()));
        });
        return result;
    }

    private ArrayList<PhoneNumber> getCountryPhoneNumbers(int country) {
        String content = getJsonFromURL(FREE_PHONE_LIST_URL + country);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrayNode = null;
        ArrayList<PhoneNumber> result = null;
        try {
            arrayNode = mapper.readTree(content);
            result = mapper.readValue(arrayNode.get("numbers").toString(), new TypeReference<ArrayList<PhoneNumber>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private ArrayList<Country> getCountryList() throws JsonProcessingException {
        String content = getJsonFromURL(FREE_COUNTRY_LIST_URL);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrayNode = mapper.readTree(content);

        return mapper.readValue(arrayNode.get("countries").toString(), new TypeReference<>() {});
    }

    private String getJsonFromURL (String URL) {
        StringBuilder result = null;
        try {
            URL url = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            int status = con.getResponseCode();

            Reader streamReader = null;

            if (status > 299) {
                throw new IOException("Bad response(");
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            result = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            in.close();
            con.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }
}
