package com.help.service;

import com.help.model.AddressDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class GeoService {
    private final RestTemplate restTemplate = new RestTemplate();

    public AddressDetails getAddressFromLatLng(double lat, double lng) {
        String url = "https://nominatim.openstreetmap.org/reverse?lat=" + lat + "&lon=" + lng + "&format=json";
        Map<String, Object> body = restTemplate.getForEntity(url, Map.class).getBody();
        if (body == null || body.get("address") == null) return null;
        Map<String, String> address = (Map<String, String>) body.get("address");
        AddressDetails addressDetails = new AddressDetails();
        addressDetails.setStreet(address.getOrDefault("road", ""));
        addressDetails.setCity(address.getOrDefault("city", address.getOrDefault("town", "")));
        addressDetails.setState(address.getOrDefault("state", ""));
        addressDetails.setZip(address.getOrDefault("postcode", ""));
        return addressDetails;
    }
}
