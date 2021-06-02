package org.lobo.census.graph;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class QueryCensus {
    private static final String CENSUS_API = "https://api.census.gov/data/timeseries/idb/5year";
    private static final String API_KEY = "fee00b0d52c7cd8f170a09ce4785218a70523396";

    public static CountryData[] getData(int startYear, String[] countryCodes) {
        // Generate the countries parameter string
        StringBuilder countriesStringBuilder = new StringBuilder();
        for (String countryCode : countryCodes)
            countriesStringBuilder.append("GENC=" + countryCode + "&");
        countriesStringBuilder.setLength(countriesStringBuilder.length() - 1);
        System.out.println("Countries string: " + countriesStringBuilder);

        // Initialize the country names
        Map<String,CountryData> countryDataMap = getCountryNames(countriesStringBuilder.toString());

        // Generate the countries parameter string
        StringBuilder yearsStringBuilder = new StringBuilder();
        for (int i = startYear; i <= 2021; i++)
            yearsStringBuilder.append("YR=" + i + "&");
        yearsStringBuilder.setLength(yearsStringBuilder.length() - 1);
        System.out.println("Years string: " + yearsStringBuilder);

        // Fill in the population data for countries & years
        getPopulation(countryDataMap, countriesStringBuilder.toString(), yearsStringBuilder.toString());

        return countryDataMap.values().toArray(new CountryData[0]);
    }

    private static Map<String,CountryData> getCountryNames(String countriesURLString) {
        HashMap<String,CountryData> countryDataMap = new HashMap<>();

        try {
            URL url = new URL(CENSUS_API + "?get=NAME&YR=2021&key=" + API_KEY + "&" +
                    countriesURLString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = conn.getInputStream();

            // Jackson JsonParser
            JsonParser parser = new JsonFactory().createParser(inputStream);

            ObjectMapper objectMapper = new ObjectMapper();

            boolean firstArray = true;
            while (!parser.isClosed()) {
                JsonToken token = parser.nextToken();

                if (token == JsonToken.END_ARRAY) {
                    firstArray = false;
                    continue;
                }

                if (firstArray || token != JsonToken.START_ARRAY)
                    continue;

                ArrayNode arrayNode = objectMapper.readTree(parser);
                JsonNode nameNode = arrayNode.get(0);
                JsonNode codeNode = arrayNode.get(2);
                System.out.println(codeNode.asText() + " == " + nameNode.asText());

                countryDataMap.put(codeNode.asText(), new CountryData(nameNode.asText()));
            }

            inputStream.close();
            conn.disconnect();
//            StringBuilder textBuilder = new StringBuilder();
//            try (Reader reader = new BufferedReader(new InputStreamReader
//                    (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
//                int c = 0;
//                while ((c = reader.read()) != -1) {
//                    textBuilder.append((char) c);
//                }
//            }
//            System.out.println(textBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countryDataMap;
    }

    private static void getPopulation(Map<String,CountryData> countryDataMap, String countriesURLString,
                                      String yearsURLString) {
        try {
            URL url = new URL(CENSUS_API + "?get=POP&key=" + API_KEY + "&" + yearsURLString +
                    "&" + countriesURLString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = conn.getInputStream();

            // Jackson JsonParser
            JsonParser parser = new JsonFactory().createParser(inputStream);

            ObjectMapper objectMapper = new ObjectMapper();

            boolean firstArray = true;
            while (!parser.isClosed()) {
                JsonToken token = parser.nextToken();

                if (token == JsonToken.END_ARRAY) {
                    firstArray = false;
                    continue;
                }

                if (firstArray || token != JsonToken.START_ARRAY)
                    continue;

                ArrayNode arrayNode = objectMapper.readTree(parser);
                JsonNode populationNode = arrayNode.get(0);
                JsonNode yearNode = arrayNode.get(1);
                JsonNode codeNode = arrayNode.get(2);
                //System.out.println(codeNode.asText() + " / " + yearNode.asText() + " = " +
                //        populationNode.asText());

                CountryData countryData = countryDataMap.get(codeNode.asText());
                assert countryData != null;
                countryData.add(yearNode.asText(), Integer.parseInt(populationNode.asText()));
            }

            inputStream.close();
            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
