package com.help.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.model.AddressDetails;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class GeoService {
    private final RestTemplate restTemplate = new RestTemplate();

    public AddressDetails getAddressFromLatLng(double lat, double lng) {
        String url = "https://nominatim.openstreetmap.org/reverse?lat=" + lat + "&lon=" + lng + "&format=json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "MyApp/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map>() {}
        );

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("address")) {
            System.out.println("No address data found.");
            return null;
        }

        Map<String, Object> address = (Map<String, Object>) body.get("address");
        System.out.println(address.toString());
        AddressDetails addressDetails = new AddressDetails();
        addressDetails.setStreet(getStringFromMap(address, "road", "pedestrian", "footway", "residential", "neighbourhood", "suburb", "quarter"));
        addressDetails.setCity(getStringFromMap(address, "city", "town", "village", "hamlet", "county", "state_district"));
        addressDetails.setState(getStringFromMap(address, "state", "region"));
        addressDetails.setZip(getStringFromMap(address, "postcode"));
        return addressDetails;
    }

    private String getStringFromMap(Map<String, Object> map, String... keys) {
        for (String key : keys) {
            Object value = map.get(key);
            if (value != null && value instanceof String && !((String) value).isEmpty()) {
                return (String) value;
            }
        }
        return "";
    }

}
