package com.caolei.system.controller;

import com.caolei.system.api.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/error")
@Controller
public class ErrorController
        implements BaseController {

    /**
     * 跳转404页面
     *
     * @return
     */
    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String error_404() {
        return "404";
    }

}
