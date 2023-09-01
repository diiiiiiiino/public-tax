package com.nos.tax.login.command.application.presentation;

import com.nos.tax.common.http.Response;
import com.nos.tax.login.command.application.service.LoginRequest;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public Response<Void> login(@RequestBody LoginRequest loginRequest){
        System.out.println("login success!!");
        return Response.ok();
    }
}
