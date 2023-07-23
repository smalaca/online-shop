package com.smalaca.appone;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hi")
public class Controller {
    @GetMapping
    public String world() {
        return "Hello world form app ONE";
    }
}
