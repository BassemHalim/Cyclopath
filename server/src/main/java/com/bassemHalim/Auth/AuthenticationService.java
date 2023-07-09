package com.bassemHalim.Auth;

import com.bassemHalim.Config.JwtService;
import com.bassemHalim.Repositoy.SingleTableDB;
import com.bassemHalim.User.Role;
import com.bassemHalim.User.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log
public class AuthenticationService {
    private final SingleTableDB repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        if (repository.getUserByEmail(request.getEmail()) == null) {
            User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), Role.USER);
            repository.saveUser(user);

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
        return login(new AuthenticationRequest(request.getEmail(), request.getPassword()));
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        // authenticated
        User user = repository.getUserByEmail(request.getEmail());
        if (user == null) return null;
        String jwtToken = jwtService.generateToken(user);

        log.warning(user.getId() + " logged in");
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
