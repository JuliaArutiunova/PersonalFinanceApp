package by.it_academy.jd2.user_service.controller;


import by.it_academy.jd2.user_service.dto.UserDTO;
import by.it_academy.jd2.user_service.dto.UserLoginDTO;
import by.it_academy.jd2.user_service.dto.UserRegistrationDTO;
import by.it_academy.jd2.user_service.dto.VerificationDTO;
import by.it_academy.jd2.user_service.service.api.IAuthenticationService;
import by.it_academy.jd2.user_service.service.api.IUserService;
import jakarta.validation.Valid;
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


    private final IAuthenticationService authenticationService;

    private final IUserService userService;

    public CabinetController(IAuthenticationService authenticationService, IUserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Void> registration(@Valid @RequestBody UserRegistrationDTO registrationDTO) {

        authenticationService.registerUser(registrationDTO);

        return new ResponseEntity<>(HttpStatus.CREATED); //ResponseStatus аннотация
    }

    @GetMapping("/verification")
    public ResponseEntity<Void> verification(@Valid VerificationDTO verificationDTO) {

        userService.verify(verificationDTO.getCode(), verificationDTO.getMail());


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {

        String token = authenticationService.getToken(userLoginDTO);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me() {

        UserDTO userDTO = authenticationService.getMe();

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
