package by.it_academy.jd2.user_service.controller;


import by.it_academy.jd2.user_service.controller.utils.JwtTokenHandler;
import by.it_academy.jd2.user_service.dto.*;
import by.it_academy.jd2.user_service.service.api.IAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {

    @Autowired
    IAuthenticationService authenticationService;
    @Autowired
    JwtTokenHandler jwtTokenHandler;


    @PostMapping("/registration")
    public ResponseEntity<Void> registration(@Valid @RequestBody UserRegistrationDTO registrationDTO) {

        authenticationService.registerUser(registrationDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/verification")
    public ResponseEntity<Void> verification(@Valid VerificationDTO verificationDTO) {

        authenticationService.verifyUser(verificationDTO.getCode(), verificationDTO.getMail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {

        TokenInfoDTO tokenInfoDTO = authenticationService.login(userLoginDTO);

        String token = jwtTokenHandler.generateToken(tokenInfoDTO);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me() {

        UserDTO userDTO = authenticationService.getMe();

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
