package com.caolei.base.controller;

import com.caolei.common.api.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/error")
@Controller
public class ErrorController
        implements BaseController {

    /**
     * 跳转404页面
     *
     * @return
     */
    @GetMapping(value = "/404")
    public String error_404() {
        return "404";
    }

}
