package com.example.UsageAppBackend.service;

import com.example.UsageAppBackend.entity.UserInfo;
import com.example.UsageAppBackend.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository repository;
    private final PasswordEncoder encoder;

    @Autowired
    UserInfoService(UserInfoRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));       //BCrypt Hashing
        repository.save(userInfo);
        return "User added successfully!";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = repository.findByEmail(username);
        if(userInfo.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email" + username);
        }
        UserInfo user = userInfo.get();
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(user.getRoles());
        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
