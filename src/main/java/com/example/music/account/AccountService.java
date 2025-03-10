package com.example.music.account;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.example.music.common.Constant;
import com.example.music.common.Message;
import com.example.music.common.Result;
import com.example.music.security.JwtService;
import com.example.music.user.User;
import com.example.music.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final Cloudinary cloudinary;

    private final PasswordEncoder passwordEncoder;

    public Map<Object, Object> verifyAccount(NewAccountDto request) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        if (request.getLogin() == null || request.getLogin().isEmpty()) {
            result = new Result(Message.INVALID_EMAIL.getCode(), false, Message.INVALID_EMAIL.getMessage());
        }
        if (request.getPass() == null || request.getPass().isEmpty() || request.getPass().length() < 6) {
            result = new Result(Message.INVALID_PASSWORD.getCode(), false, Message.INVALID_PASSWORD.getMessage());
        }
        Account account = accountRepository.findAccountByLogin(request.getLogin()).orElse(null);
        if (account != null) {
            result = new Result(Message.ACCOUNT_ALREADY_EXISTS.getCode(), false, Message.ACCOUNT_ALREADY_EXISTS.getMessage());
        }
        finalResult.put("result", result);
        return finalResult;
    }

    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public Map<Object, Object> createAccountUser(NewAccountDto request) throws Exception {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();

        try {
            Date create = new Date(System.currentTimeMillis());

            Account account = Account.builder()
                    .login(request.getLogin())
                    .pass(passwordEncoder.encode(request.getPass()))
                    .role(Constant.Role.USER)
                    .create_date(create)
                    .create_by(Constant.Create.HVH)
                    .status(Constant.Status.Activate)
                    .build();
            this.accountRepository.save(account);

            ApiResponse apiResponse = cloudinary.api().resourceByAssetID("6089f07ca3500cc8c9362a3edb3be8d7", ObjectUtils.emptyMap());

            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .name(request.getName())
                    .avatar(apiResponse.get("url").toString())
                    .create_date(create)
                    .account(account)
                    .create_by(Constant.Create.NTH)
                    .status(Constant.Status.Activate)
                    .build();
            this.userRepository.save(user);
            finalResult.put(Constant.RESPONSE_KEY.DATA, jwtService.generateToken(user));
        } catch (Exception e) {
            System.out.println("Xảy ra lỗi khi tạo mới người dùng {} " + e.getMessage());
            result = new Result(Message.UNABLE_TO_CREATE_ACCOUNT.getCode(), false, Message.UNABLE_TO_CREATE_ACCOUNT.getMessage());
            throw e;
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public Map<Object, Object> createAccountArtis(NewAccountDto request) throws Exception {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Account account = Account.builder()
                    .login(request.getLogin())
                    .pass(passwordEncoder.encode(request.getPass()))
                    .role(Constant.Role.ARTIS)
                    .create_date(new Date(new java.util.Date().getTime()))
                    .status(Constant.Status.Activate)
                    .build();
            this.accountRepository.save(account);

            ApiResponse apiResponse = cloudinary.api().resourceByAssetID("6089f07ca3500cc8c9362a3edb3be8d7", ObjectUtils.emptyMap());

            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .name(request.getName())
                    .avatar(apiResponse.get("url").toString())
                    .create_date(new Date(new java.util.Date().getTime()))
                    .account(account)
                    .status(Constant.Status.Activate)
                    .build();
            this.userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Xảy ra lỗi khi tạo mới tài khoản nghệ sĩ {} " + e.getMessage());
            result = new Result(Message.UNABLE_TO_CREATE_ACCOUNT.getCode(), false, Message.UNABLE_TO_CREATE_ACCOUNT.getMessage());
            throw e;
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> login(LoginDto login) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getLogin(), login.getPass()));
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                Account account = accountRepository.findById(login.getLogin()).orElse(null);
                if (account == null) {
                    throw new UsernameNotFoundException("User not found");
                }
                User user = userRepository.getUserByEmail(account.getLogin());
                String token = jwtService.generateToken(user);
                AccountResponse response = new AccountResponse(token, login.getLogin());
                finalResult.put(Constant.RESPONSE_KEY.DATA, response);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result = new Result(Message.CANNOT_LOG_IN.getCode(), false, Message.CANNOT_LOG_IN.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> getUserResponse(String login) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            DetailAccount userResponse = userRepository.getUserResponse(login);
            finalResult.put(Constant.RESPONSE_KEY.DATA, userResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result = new Result(Message.ACCOUNT_NOT_EXISTS.getCode(), false, Message.ACCOUNT_NOT_EXISTS.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

}
