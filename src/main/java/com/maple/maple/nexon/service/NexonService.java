package com.maple.maple.nexon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.maple.config.NexonApiConfig;

import com.maple.maple.nexon.dto.response.StatListResponse;
import com.maple.maple.nexon.dto.response.StatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class NexonService {

    private final NexonApiConfig config;
    private final String[] teamNames = {"김초면", "반휘람"};
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper 인스턴스
    private static String mainApi = "/maplestory/v1";

    private String callApi(String subApi, String query) {
        try {
            String urlString = config.getBaseUrl() + mainApi+subApi + "?" + query;
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

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> getTeamOcid() {
        List<String> ocids = new ArrayList<>();
        for (String name : teamNames) {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
            String query = "character_name=" + encodedName;
            String response = callApi("/id", query);
            if (response != null) {
                try {
                    JsonNode jsonResponse = objectMapper.readTree(response);
                    String ocid = jsonResponse.get("ocid").asText();
                    log.info(ocid.toString());
                    ocids.add(ocid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ocids;
    }

    public StatListResponse getTeamStat() {
        List<StatResponse> statResponses = new ArrayList<>();
        List<String> ocids = getTeamOcid();

        // teamNames와 ocids의 인덱스를 매핑하여 이름과 ocid를 함께 사용
        for (int i = 0; i < ocids.size() && i < teamNames.length; i++) {
            String ocid = ocids.get(i);
            String name = teamNames[i];
            String encodedOcid = URLEncoder.encode(ocid, StandardCharsets.UTF_8);
            String query = "ocid=" + encodedOcid;
            String response = callApi("/character/stat", query);
            if (response != null) {
                try {
                    JsonNode jsonResponse = objectMapper.readTree(response);
                    JsonNode finalStat = jsonResponse.get("final_stat");
                    if (finalStat != null && finalStat.isArray()) {
                        StatResponse statResponse = new StatResponse(
                                name,
                                finalStat.get(42).get("stat_value").asText(),
                                finalStat.get(1).get("stat_value").asText(),
                                finalStat.get(2).get("stat_value").asText(),
                                finalStat.get(3).get("stat_value").asText(),
                                finalStat.get(4).get("stat_value").asText(),
                                finalStat.get(5).get("stat_value").asText(),
                                finalStat.get(7).get("stat_value").asText(),
                                finalStat.get(14).get("stat_value").asText(),
                                finalStat.get(15).get("stat_value").asText()
                        );
                        statResponses.add(statResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new StatListResponse(statResponses);
    }


}