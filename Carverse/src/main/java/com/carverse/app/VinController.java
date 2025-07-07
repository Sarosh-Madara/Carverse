package com.carverse.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;


@RestController
public class VinController {

    @GetMapping("/vin")
    public String getVin(@RequestParam String vin) {
        return "Vin is decoding..." + vin;
    }

    @GetMapping("/vin/decode")
    public Map<String, Object> decodeVin(@RequestParam String vin) {
        String url = "https://vpic.nhtsa.dot.gov/api/vehicles/decodevin/" + vin + "?format=json";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, Map.class);
    }
    
    @GetMapping("/vin/decode/v2")
    public Map<String, String> decodeVinV2(@RequestParam String vin) {
    String url = "https://vpic.nhtsa.dot.gov/api/vehicles/decodevin/" + vin + "?format=json";
    RestTemplate restTemplate = new RestTemplate();

    // fetch the full response
    @SuppressWarnings("unchecked")
    Map<String, Object> response =
            restTemplate.getForObject(url, Map.class);

    // prepare the pared-down result
    Map<String, String> vehicleInfo = new HashMap<>();

    // the NHTSA payload puts data in a list called "Results"
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("Results");

    if (results != null) {
        for (Map<String, Object> item : results) {
            String variable = String.valueOf(item.get("Variable"));
            String value    = (String) item.get("Value");

            switch (variable) {
                case "Make"       -> vehicleInfo.put("make",  value);
                case "Model"      -> vehicleInfo.put("model", value);
                case "Model Year" -> vehicleInfo.put("year",  value);
                default -> { /* ignore everything else */ }
            }
            // early exit once we have all three
            if (vehicleInfo.size() == 3) break;
        }
    }

    return vehicleInfo;   // e.g. { "make":"GMC", "model":"Savana", "year":"2004" }
}

}
