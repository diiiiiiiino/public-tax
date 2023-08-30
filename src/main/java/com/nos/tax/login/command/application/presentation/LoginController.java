package com.nos.tax.login.command.application.presentation;

import com.nos.tax.common.http.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping
    public Response<Void> login(){
        System.out.println("login success!!");
        return Response.ok();
    }
}
