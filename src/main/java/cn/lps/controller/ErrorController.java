package cn.lps.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/management/error")
public class ErrorController {
    @RequestMapping(value = "/illegal", method = {RequestMethod.POST, RequestMethod.GET})
    public String illegal() {
        return "error/illegal";
    }
}
