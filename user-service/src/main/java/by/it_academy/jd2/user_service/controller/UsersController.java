package by.it_academy.jd2.user_service.controller;

import by.it_academy.jd2.user_service.dto.PageDTO;
import by.it_academy.jd2.user_service.dto.PaginationDTO;
import by.it_academy.jd2.user_service.dto.UserCreateDTO;
import by.it_academy.jd2.user_service.dto.UserDTO;
import by.it_academy.jd2.user_service.service.api.IUserManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private IUserManagerService userManagerService;

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {

        userManagerService.createUser(userCreateDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<PageDTO<UserDTO>> getUserPage(@Valid PaginationDTO paginationDTO) {

        PageDTO<UserDTO> usersPageDTO = userManagerService.getUsersPage(paginationDTO);

        return new ResponseEntity<>(usersPageDTO, HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("uuid") UUID uuid) {

        UserDTO userDTO = userManagerService.getById(uuid);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/{uuid}/dt_update/{dt_update}")
    public ResponseEntity<Void> editUserInfo(@PathVariable("uuid") UUID uuid,
                                             @PathVariable("dt_update") long dtUpdate,
                                             @Valid
                                             @RequestBody UserCreateDTO userCreateDTO) {

        userManagerService.updateUser(uuid, dtUpdate, userCreateDTO);
        return new ResponseEntity<>(HttpStatus.OK);

    }


}
