package com.bjsn.finance.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class MetalRateService {

    private static final String GOLD_API_KEY = "goldapi-19hdsmd9uxur3-io";
    private static final String GOLD_URL = "https://www.goldapi.io/api/XAU/INR";
    private static final String SILVER_URL = "https://www.goldapi.io/api/XAG/INR";

    public Map<String, Double> fetchRates() {
        Map<String, Double> rates = new HashMap<>();

        try {
            // Fetch gold rate
            double goldRate = fetchRateFromGoldAPI(GOLD_URL);
            double silverRate = fetchRateFromGoldAPI(SILVER_URL);

            // Convert from per ounce to per gram
            goldRate = goldRate / 31.1035;
            silverRate = silverRate / 31.1035;

            rates.put("gold", goldRate);
            rates.put("silver", silverRate);
        } catch (Exception e) {
            rates.put("gold", 0.0);
            rates.put("silver", 0.0);
        }

        return rates;
    }

    private double fetchRateFromGoldAPI(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // 🔑 Set required headers
        conn.setRequestProperty("x-access-token", GOLD_API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.toString());

        return root.path("price").asDouble(); // GoldAPI returns the rate as "price"
    }
}