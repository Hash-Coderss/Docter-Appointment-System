package com.appoinment.auth_service.service;

import com.appoinment.auth_service.dto.DoctorListResponse;
import com.appoinment.auth_service.dto.UserProfileResponse;
import com.appoinment.auth_service.entity.DoctorProfile;
import com.appoinment.auth_service.entity.PatientProfile;
import com.appoinment.auth_service.entity.Role;
import com.appoinment.auth_service.entity.User;
import com.appoinment.auth_service.exception.ResourceNotFoundException;
import com.appoinment.auth_service.repository.DoctorProfileRepository;
import com.appoinment.auth_service.repository.PatientProfileRepository;
import com.appoinment.auth_service.repository.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PatientProfileRepository patientProfileRepository;

    public UserProfileResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserProfileResponse.UserProfileResponseBuilder builder = UserProfileResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name());

        if (user.getRole() == Role.DOCTOR) {
            DoctorProfile profile = doctorProfileRepository.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
            builder.specialization(profile.getSpecialization())
                    .experience(profile.getExperience())
                    .hospitalName(profile.getHospitalName());
        } else {
            PatientProfile profile = patientProfileRepository.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
            builder.age(profile.getAge())
                    .gender(profile.getGender());
        }

        return builder.build();
    }

    public List<DoctorListResponse> getDoctorsBySpecialization(String specialization) {
        List<DoctorProfile> profiles = doctorProfileRepository.findBySpecializationIgnoreCase(specialization);
        if (profiles.isEmpty()) {
            return List.of();
        }

        List<Long> userIds = profiles.stream().map(DoctorProfile::getUserId).toList();
        List<User> users = userRepository.findByIdIn(userIds);

        Map<Long, User> usersById = new HashMap<>();
        for (User user : users) {
            usersById.put(user.getId(), user);
        }

        List<DoctorListResponse> response = new ArrayList<>();
        for (DoctorProfile profile : profiles) {
            User user = usersById.get(profile.getUserId());
            if (user == null || user.getRole() != Role.DOCTOR) {
                continue;
            }

            response.add(DoctorListResponse.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .specialization(profile.getSpecialization())
                    .experience(profile.getExperience())
                    .hospitalName(profile.getHospitalName())
                    .build());
        }

        response.sort(Comparator.comparing(DoctorListResponse::getName, Comparator.nullsLast(String::compareToIgnoreCase)));
        return response;
    }
}
