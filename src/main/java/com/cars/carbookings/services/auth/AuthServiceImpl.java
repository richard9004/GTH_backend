package com.cars.carbookings.services.auth;


import com.cars.carbookings.dto.SignupRequest;
import com.cars.carbookings.dto.UserDTO;
import com.cars.carbookings.entities.User;
import com.cars.carbookings.enums.UserRole;
import com.cars.carbookings.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "/uploads/";

    @PostConstruct
    public void createAdminAccount(){
        Optional<User> optionalUser = userRepository.findByUserRole(UserRole.ADMIN);

        if(optionalUser.isEmpty()){
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin_richard@test.com");
            admin.setUserRole(UserRole.ADMIN);
            admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
            admin.setPhone("1234567890");
            admin.setStreetAddress("123 Admin Street");
            admin.setSuburb("Admin Suburb");
            admin.setCity("Admin City");
            admin.setPostcode("12345");
            admin.setCountry("Admin Country");
            admin.setBirthDate("01-01-1980");
            admin.setProfileImage("default_image.jpg");
            admin.setTermsAccepted(true);

            userRepository.save(admin);
            System.out.println("ADMIN USER CREATED");

            userRepository.save(admin);
            System.out.println("ADMIN USER CREATED");
        }else{
            System.out.println("ADMIN USER ALREADY EXIST");
        }
    }

    @Override
    public UserDTO signup(SignupRequest signupRequest) throws IOException {
        User user = new User();
        // Mapping fields from SignupRequest to User entity
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setPreferredName(signupRequest.getPreferredName());
        user.setEmail(signupRequest.getEmail());

        // Encrypt the password before saving
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));

        // Default role for new users
        user.setUserRole(UserRole.USER);

        // Save files and store paths
        user.setProfileImage(saveFile(signupRequest.getProfileImage()));

        // Optional fields - set them if they are provided in the signup request
        user.setPhone(signupRequest.getPhone());
        user.setStreetAddress(signupRequest.getStreetAddress());
        user.setSuburb(signupRequest.getSuburb());
        user.setCity(signupRequest.getCity());
        user.setPostcode(signupRequest.getPostcode());
        user.setCountry(signupRequest.getCountry());
        user.setBirthDate(signupRequest.getBirthDate());
     //   user.setProfileImage(signupRequest.getProfileImage());
        user.setTermsAccepted(signupRequest.isTermsAccepted());

        // Save the user to the repository and return the UserDTO
        return userRepository.save(user).getUserDTO();
    }


    private String saveFile(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            Path uploadDir = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadDir); // Ensure the directory exists
            Path filePath = uploadDir.resolve(file.getOriginalFilename());
            Files.write(filePath, file.getBytes());
            return filePath.toString(); // Return the file path to be saved in the database
        }
        return null;
    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
