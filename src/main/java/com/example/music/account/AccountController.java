package com.example.music.account;

import com.example.music.common.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/register")
    public CompletableFuture<ResponseData> register(@RequestBody NewAccountDto accountRequest) throws Exception {
        return CompletableFuture.completedFuture(ResponseData.createResponse(this.accountService.createAccountUser(accountRequest)));
    }

    @PostMapping(value = "/login")
    public CompletableFuture<ResponseData> login(@RequestBody LoginDto login) {
        return CompletableFuture.completedFuture(ResponseData.createResponse(this.accountService.login(login)));
    }

    @GetMapping(value = "/get-account-by-user-name")
    public CompletableFuture<ResponseData> getUserResponse(@RequestParam(name = "login") String login) {
        return CompletableFuture.completedFuture(ResponseData.createResponse(this.accountService.getUserResponse(login)));
    }

}
