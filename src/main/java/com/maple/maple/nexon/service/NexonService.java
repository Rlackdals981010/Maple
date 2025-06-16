package com.maple.maple.nexon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.maple.config.NexonApiConfig;
import com.maple.maple.nexon.dto.response.OcidListResponse;
import com.maple.maple.nexon.dto.response.OcidResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NexonService {

    private final NexonApiConfig config;
    private final String[] teamNames = {"김초면", "김측면"};
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper 인스턴스

    private String callApi(String mainApi, String queryParamName, String characterName) {
        try {
            String encodedName = URLEncoder.encode(characterName, StandardCharsets.UTF_8);
            String urlString = config.getBaseUrl() + mainApi + "?" + queryParamName + "=" + encodedName;

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-nxopen-api-key", config.getApiKey());

            int responseCode = connection.getResponseCode();

            BufferedReader in;
            if (responseCode == 200) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonNode jsonResponse = objectMapper.readTree(response.toString());
            return jsonResponse.get("ocid").asText(); // ocid 값만 반환

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public OcidListResponse getTeamOcid() {
        List<OcidResponse> ocidResponses = new ArrayList<>();
        for (String name : teamNames) {
            String ocid = callApi("/maplestory/v1/id", "character_name", name);
            OcidResponse newOcidResponse = new OcidResponse(name, ocid);
            ocidResponses.add(newOcidResponse);
        }
        return new OcidListResponse(ocidResponses);
    }
}