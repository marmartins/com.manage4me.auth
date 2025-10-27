package com.manage4me.route.auth;

import com.manage4me.route.account.AccountRepository;
import com.manage4me.route.entities.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class AuthorizeUserService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = accountRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", email));
        }
        return new AuthorizedUser(user);
    }


}
