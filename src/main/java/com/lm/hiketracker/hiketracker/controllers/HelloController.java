package com.lm.hiketracker.hiketracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @GetMapping("/")
    public String hello(Model model) {
        model.addAttribute("message", "Welcome to Hike Tracker!");
        return "home";
    }

    @GetMapping("/second")
    public String second() {
        return "second";
    }
}
