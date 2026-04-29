package com.github.sonjaemark.ntc_erquest_system.service.auth;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.dto.AuthResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.LoginRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.LogoutRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RefreshTokenDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RegisterRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RegisterResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.exception.EmailAlreadyExistException;
import com.github.sonjaemark.ntc_erquest_system.exception.EmailNotFoundException;
import com.github.sonjaemark.ntc_erquest_system.exception.InvalidRefreshTokenException;
import com.github.sonjaemark.ntc_erquest_system.exception.PasswordInvalidException;
import com.github.sonjaemark.ntc_erquest_system.exception.UnauthorizedUserException;
import com.github.sonjaemark.ntc_erquest_system.model.RefreshToken;
import com.github.sonjaemark.ntc_erquest_system.model.UserModel;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.RefreshTokenRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;
import com.github.sonjaemark.ntc_erquest_system.service.token.RefreshTokenService;

@Service
public class AuthService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserModelRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private LoggedUser loggedUser;

    public AuthResponseDTO login(LoginRequestDTO request) {

        // Authenticate the user using the provided email and password
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
            )
        );

        // If authentication is successful, retrieve the user details
        UserModel user = userRepository
            .findByEmail(request.email())
        // If the user is not found, throw an exception that will be handled by GlobalExceptionHandler and return a 404 Not Found response to the client    
        .orElseThrow(() -> new EmailNotFoundException("Email not found: " + request.email()));

        // Generate JWT access token and refresh token for the authenticated user
        String accessToken =
            jwtService.generateToken(user.getId(), user.getEmail());

        // Create a new refresh token for the user and return it in the response
        String refreshToken =
            refreshTokenService.createRefreshToken(user.getId()).getToken();

        // Return the authentication response containing the access token, refresh token, and user details
        return new AuthResponseDTO(
            accessToken,
            refreshToken,
            user.getId(),
            user.getEmail(),
            user.getRole()
        );
    }

    public RegisterResponseDTO register(RegisterRequestDTO request) {

        // Check if the email already exists in the database
        userRepository.findByEmail(request.email()).ifPresent(user -> {

            // If the email already exists, throw an exception that will be handled by GlobalExceptionHandler and return a 409 Conflict response to the client
            throw new EmailAlreadyExistException("Email " + request.email() + " already exists");
        });

        if (!request.password().equals(request.confirmPassword())) {
            throw new PasswordInvalidException("Password and confirm password do not match");
            
        }

        // If the email does not exist, create a new user with the provided details and save it to the database
        UserModel user = UserModel.builder()
            .firstName(request.firstName())
            .lastName(request.lastName())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(request.role())
            .build();
        userRepository.save(user);

        // Generate JWT access token and refresh token for the newly registered user
        return new RegisterResponseDTO(
            user.getId(),
            user.getEmail(),
            user.getRole()
        );
    }


    public AuthResponseDTO refreshToken(RefreshTokenDTO requestToken) {

        // Validate the provided refresh token and retrieve the associated user
        RefreshToken refreshToken =
            refreshTokenRepository.findByToken(requestToken.refreshToken())
            // If the refresh token is not found, throw an exception that will be handled by GlobalExceptionHandler and return a 401 Unauthorized response to the client
            .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));
        refreshTokenService.verifyExpiration(refreshToken);

        // If the refresh token is valid, generate a new access token and refresh token for the associated user
        UserModel user = refreshToken.getUser();

        // Generate a new access token for the user
        String accessToken =
            jwtService.generateToken(user.getId(), user.getEmail());

        // Create a new refresh token for the user and return it in the response
        String refreshTokenStr =
            refreshTokenService.createRefreshToken(user.getId()).getToken();

        // Return the authentication response containing the new access token, refresh token, and user details
        return new AuthResponseDTO(
            accessToken,
            refreshTokenStr,
            user.getId(),
            user.getEmail(),
            user.getRole()
        );
    }

    public AuthResponseDTO logout(LogoutRequestDTO token) {

        // Validate the provided refresh token and delete it from the database to log the user out
        RefreshToken refreshToken = refreshTokenRepository
            .findByToken(token.refreshToken())
        // If the refresh token is not found, throw an exception that will be handled by GlobalExceptionHandler and return a 400 Bad Request response to the client
        .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        // Delete the refresh token from the database to log the user out
        refreshTokenRepository.delete(refreshToken);

        jwtService.blockAccessToken(token.accessToken());

        return new AuthResponseDTO(
            null,
            null,
            null,
            null,
            null
        );
    }

    public UserModel getAuthorizedUser(List<UserRole> roles) {
        UserModel user = loggedUser.getLoggedUser();

        if (roles.contains(user.getRole())) {
            return user;
        }
        
        throw new UnauthorizedUserException("Unauthorized access: role not allowed");
    }
}
