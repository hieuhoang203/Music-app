package com.example.music.security;

import com.example.music.account.Account;
import com.example.music.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByLogin(login).orElse(null);
        if(Objects.isNull(account)) {
            System.out.println("User not available");
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(account);
    }

}
