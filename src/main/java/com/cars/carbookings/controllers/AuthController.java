package com.cars.carbookings.controllers;

import com.cars.carbookings.dto.*;
import com.cars.carbookings.entities.Organizer;
import com.cars.carbookings.entities.User;
import com.cars.carbookings.repositories.UserRepository;
import com.cars.carbookings.services.auth.AuthService;
import com.cars.carbookings.services.jwt.UserService;
import com.cars.carbookings.services.organizer.OrganizerService;
import com.cars.carbookings.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    private final JWTUtil jwtUtil;

    private final UserService userService;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final OrganizerService organizerService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@ModelAttribute SignupRequest signupRequest) throws IOException {
        if(authService.hasUserWithEmail(signupRequest.getEmail()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User Alredy Exists");
        UserDTO userDTO = authService.signup(signupRequest);
        if(userDTO == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        }catch (BadCredentialsException badCredentialsException){
            System.out.println("Authentication failed: " + badCredentialsException.getMessage());
            throw new BadCredentialsException("Invalid username or password");
        }

//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
//        } catch (BadCredentialsException ex) {
//            System.out.println("Authentication failed: " + ex.getMessage());
//            throw new BadCredentialsException("Invalid username or password");
//        }

        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        AuthenticationResponse response = new AuthenticationResponse();

        if(optionalUser.isPresent()){
            response.setJwt(jwt);
            response.setUserRole(optionalUser.get().getUserRole());
            response.setUserId(optionalUser.get().getId());
        }
        return response;
    }


    @PostMapping("/organizer/register")
    public ResponseEntity<?> registerOrganizer(@ModelAttribute OrganizerRequest request) {
        Organizer savedOrganizer = organizerService.createOrganizer(request);
        Map<String, Object> response = new HashMap<>();

        if (savedOrganizer != null) {
            response.put("success", true);
            response.put("orgName", request.getOrgName());
            response.put("message", "Organizer registered successfully with ID: " + savedOrganizer.getId());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to register the organizer. Please try again.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



}
