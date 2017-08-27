package com.fangminx.controller;

import com.fangminx.properties.GirlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Value("${cupSize}")
    private String cupSize;

    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping(value = "/say", method = RequestMethod.GET)
    public String say0(@Value("${age}") Integer age) {
        return cupSize + " " + age + " " + girlProperties.getAge() + " " + girlProperties.getCupSize();
    }

//    @RequestMapping(value = "/{id}/say", method = RequestMethod.GET)
    @GetMapping(value = "/{id}/say")
    public String say1(@PathVariable(value = "id") Integer id,
                       @RequestParam(value = "name",required = false,defaultValue = "defaultName") String name) {
        return id + " " + name;
    }
}
