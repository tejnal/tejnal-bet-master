package com.tejnalbetmaster.controller;

import com.tejnalbetmaster.data.entity.RefreshToken;
import com.tejnalbetmaster.data.entity.Role;
import com.tejnalbetmaster.data.entity.User;
import com.tejnalbetmaster.data.entity.models.ERole;
import com.tejnalbetmaster.data.repository.RoleRepository;
import com.tejnalbetmaster.data.repository.UserRepository;
import com.tejnalbetmaster.payload.request.LoginRequest;
import com.tejnalbetmaster.payload.request.SignupRequest;
import com.tejnalbetmaster.payload.request.TokenRefreshRequest;
import com.tejnalbetmaster.payload.response.JwtResponse;
import com.tejnalbetmaster.payload.response.MessageResponse;
import com.tejnalbetmaster.payload.response.TokenRefreshResponse;
import com.tejnalbetmaster.security.exception.TokenRefreshException;
import com.tejnalbetmaster.security.jwt.JwtUtils;
import com.tejnalbetmaster.security.services.RefreshTokenService;
import com.tejnalbetmaster.security.services.UserDetailsImpl;
import com.tejnalbetmaster.service.UserBetDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// other perons access the url
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired AuthenticationManager authenticationManager;

  @Autowired UserRepository userRepository;

  @Autowired RoleRepository roleRepository;

  @Autowired UserBetDetailsService userBetDetailsService;

  @Autowired PasswordEncoder passwordEncoder;

  @Autowired JwtUtils jwtUtils;

  @Autowired RefreshTokenService refreshTokenService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String jwt = jwtUtils.generateJwtToken(userDetails);
    List<String> roles =
        userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
    return ResponseEntity.ok(
        new JwtResponse(
            jwt,
            refreshToken.getToken(),
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();
    return refreshTokenService
        .findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(
            user -> {
              String token = jwtUtils.generateTokenFromUsername(user.getUsername());
              return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            })
        .orElseThrow(
            () ->
                new TokenRefreshException(
                    requestRefreshToken, "Refresh token is not in database!"));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user =
        new User(
            signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            passwordEncoder.encode(signUpRequest.getPassword()));

    Set<Role> roles = new HashSet<>();

    // Always User role for user sign up
    Role userRole =
        roleRepository
            .findByName(ERole.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    roles.add(userRole);

    user.setRoles(roles);
    User savedUser = userRepository.save(user);

    // Create User Bet Details
    userBetDetailsService.saveInitialBetDetails(savedUser.getId());

    return ResponseEntity.ok(new MessageResponse("User registered successfully"));
  }

  @PostMapping("/admin/signup")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> registerUserByAdmin(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user =
        new User(
            signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            passwordEncoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole =
          roleRepository
              .findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(
          role -> {
            switch (role) {
              case "admin":
                Role adminRole =
                    roleRepository
                        .findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(adminRole);

                break;
              default:
                Role userRole =
                    roleRepository
                        .findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            }
          });
    }

    user.setRoles(roles);
    User savedUser = userRepository.save(user);

    // Create User Bet Details
    userBetDetailsService.saveInitialBetDetails(savedUser.getId());

    return ResponseEntity.ok(new MessageResponse("User registered successfully"));
  }
}
