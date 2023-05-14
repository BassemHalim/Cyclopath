package com.bassemHalim.cyclopath.Auth;

import com.bassemHalim.cyclopath.Config.JwtService;
import com.bassemHalim.cyclopath.Repositoy.SingleTableDB;
import com.bassemHalim.cyclopath.User.Role;
import com.bassemHalim.cyclopath.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final SingleTableDB repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
//        User user = User.builder()
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.USER)
//                .build();
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
        User user = repository.getUserByEmail(request.getEmail()); //@TODO handle error
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
