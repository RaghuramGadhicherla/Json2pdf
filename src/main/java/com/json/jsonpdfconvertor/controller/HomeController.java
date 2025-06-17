package com.json.jsonpdfconvertor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/api/convert")
    @ResponseBody
    public String convertInfo() {
        return "This endpoint requires a POST request to start the conversion process.";
    }
}