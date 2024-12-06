package by.it_academy.jd2.user_service.controller;

import by.it_academy.jd2.user_service.dto.UserCreateDTO;
import by.it_academy.jd2.user_service.dto.UserDTO;
import by.it_academy.jd2.user_service.service.api.IUserService;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.dto.PaginationDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {


    private final IUserService userService;

    public UsersController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {

        userService.create(userCreateDTO);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageDTO<UserDTO> getUserPage(@Valid PaginationDTO paginationDTO) {

        return userService.getUsersPage(paginationDTO.getPage(), paginationDTO.getSize());
    }

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(@PathVariable("uuid") UUID uuid) {

        return userService.getById(uuid);
    }

    @PutMapping("/{uuid}/dt_update/{dt_update}")
    @ResponseStatus(HttpStatus.OK)
    public void editUserInfo(@PathVariable("uuid") UUID uuid,
                             @PathVariable("dt_update") long dtUpdate,
                             @Valid
                             @RequestBody UserCreateDTO userCreateDTO) {

        userService.update(uuid, dtUpdate, userCreateDTO);

    }


}
