package com.authentication.demo.controller;

import com.authentication.demo.dto.AuthResponse;
import com.authentication.demo.dto.LoginRequest;
import com.authentication.demo.dto.SignupRequest;
import com.authentication.demo.entity.User;
import com.authentication.demo.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository) {

        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    // 1. Get CSRF token
    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken csrfToken) {
        return csrfToken;
    }

    // 2. Signup
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody SignupRequest request) {

        User user = authService.signup(request);

        AuthResponse response = new AuthResponse(
                "Registration successful",
                user.getName(),
                user.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 3. Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(),
                                    request.getPassword()
                            )
                    );

            // Create or obtain a session and rotate its ID
            httpRequest.getSession(true);
            httpRequest.changeSessionId();

            // Create authenticated SecurityContext
            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);

            // Persist SecurityContext in HTTP Session
            securityContextRepository.saveContext(
                    context,
                    httpRequest,
                    httpResponse
            );

            User user = (User) authentication.getPrincipal();

            AuthResponse response = new AuthResponse(
                    "Login successful",
                    user.getName(),
                    user.getEmail()
            );

            return ResponseEntity.ok(response);

        } catch (AuthenticationException exception) {

            SecurityContextHolder.clearContext();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(
                            "Invalid email or password",
                            null,
                            null
                    ));
        }
    }

    // 4. Current logged-in user
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                new AuthResponse(
                        "User authenticated",
                        user.getName(),
                        user.getEmail()
                )
        );
    }

    // 5. Logout
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(
            HttpServletRequest request) {

        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok(
                new AuthResponse(
                        "Logout successful",
                        null,
                        null
                )
        );
    }
}