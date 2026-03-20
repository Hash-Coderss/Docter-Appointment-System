package com.appoinment.auth_service.service;

import com.appoinment.auth_service.dto.AuthResponse;
import com.appoinment.auth_service.dto.LoginRequest;
import com.appoinment.auth_service.dto.RegisterRequest;
import com.appoinment.auth_service.entity.DoctorProfile;
import com.appoinment.auth_service.entity.PatientProfile;
import com.appoinment.auth_service.entity.Role;
import com.appoinment.auth_service.entity.User;
import com.appoinment.auth_service.exception.BadRequestException;
import com.appoinment.auth_service.repository.DoctorProfileRepository;
import com.appoinment.auth_service.repository.PatientProfileRepository;
import com.appoinment.auth_service.repository.UserRepository;
import com.appoinment.auth_service.security.JwtUtil;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Pattern FIRST_NUMBER = Pattern.compile("-?\\d+");

    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public Map<String, Object> register(RegisterRequest req) {
        if (req.getEmail() == null || req.getEmail().isBlank() || req.getPassword() == null || req.getPassword().isBlank()) {
            throw new BadRequestException("Email and password are required");
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        Role role;
        try {
            role = Role.valueOf(req.getRole().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            throw new BadRequestException("Role must be DOCTOR or PATIENT");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .build();

        User saved = userRepository.save(user);

        if (role == Role.DOCTOR) {
            doctorProfileRepository.save(DoctorProfile.builder()
                    .userId(saved.getId())
                    .specialization(req.getSpecialization())
                    .experience(parseNumber(req.getExperience(), "experience"))
                    .hospitalName(req.getHospitalName())
                    .build());
        } else {
            patientProfileRepository.save(PatientProfile.builder()
                    .userId(saved.getId())
                    .age(parseNumber(req.getAge(), "age"))
                    .gender(req.getGender())
                    .build());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("userId", saved.getId());
        response.put("role", saved.getRole().name());
        return response;
    }

    public AuthResponse login(LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getRole().name());
    }

    private int parseNumber(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return 0;
        }

        Matcher matcher = FIRST_NUMBER.matcher(value);
        if (!matcher.find()) {
            throw new BadRequestException("Invalid " + fieldName + ": expected a number like 5 or text like '5 years'");
        }

        return Integer.parseInt(matcher.group());
    }
}
