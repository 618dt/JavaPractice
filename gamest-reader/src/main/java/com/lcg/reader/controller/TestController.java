package com.lcg.reader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
    @GetMapping("/test/t1")
    public ModelAndView showTest() {
        return new ModelAndView("/test");
    }
}
