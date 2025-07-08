package com.carverse.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class OilResetServicesController {

    @GetMapping("api/v1/oil-reset-services-by-vin")
    public String getOilResetServices(@RequestParam String vin) {
        return "Oil Reset Services" + vin + " is being reset";
    }

    @GetMapping("api/v1/oil-reset-services-by-make")
    public Car getOilResetServicesV2(@RequestParam String make, @RequestParam String model, @RequestParam String year) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        InputStream is = getClass().getResourceAsStream("/data/cars.json");
        List<Car> cars = mapper.readValue(is, new TypeReference<List<Car>>() {});
        return cars.stream()
            .filter(car -> car.getMake().equalsIgnoreCase(make)
                        && car.getModel().equalsIgnoreCase(model)
                        && car.getYear().equals(year))
            .findFirst()
            .orElse(null);
    }




}
