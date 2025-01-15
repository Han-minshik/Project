package com.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Controller
public class BestsellerController {

    @GetMapping("/bestseller/index")
    public ResponseEntity<?> getBestsellers(@RequestParam(defaultValue = "Book") String category,
                                            @RequestParam(defaultValue = "10") int maxResults) {
        String apiUrl = "https://www.aladin.co.kr/ttb/api/ItemList.aspx";
        String ttbKey = "ttbhanminshik02330001";

        String fullUrl = apiUrl + "?ttbkey=" + ttbKey
                + "&QueryType=Bestseller"
                + "&MaxResults=" + maxResults
                + "&SearchTarget=" + category
                + "&Output=JS"
                + "&Version=20131101";

        RestTemplate restTemplate = new RestTemplate();
        Object response = restTemplate.getForObject(fullUrl, Object.class);
        return ResponseEntity.ok(response);
    }
}
