package com.elastic.book.sherlock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author liguolin
 * @create 2018-03-27 17:18
 **/
@Controller
public class ViewController {

    @GetMapping(value = "/index")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("/index");
        return mv;
    }

    @GetMapping(value = "/shh")
    public ModelAndView ssh(ModelAndView mv) {
        mv.setViewName("/shh");
        return mv;
    }
}
