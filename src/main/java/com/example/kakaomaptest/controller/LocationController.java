package com.example.kakaomaptest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LocationController {

    @GetMapping("/location/{placeName}")
    public String showLocation() {
        return "location";
    }

    @PostMapping("/location/{placeName}")
    public String mapLocation(@PathVariable String placeName, Model model) {
        model.addAttribute("placeName", placeName);
        System.out.println("model: " + model);
        return "location";
    }
}
