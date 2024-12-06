package by.it_academy.jd2.user_service.controller;

import by.it_academy.jd2.user_service.service.api.IUserService;
import by.it_academy.lib.dto.UserInfoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/users_data")
public class DataController {
    private final IUserService userService;

    public DataController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDTO getUserInfo(@RequestParam("user_id") UUID uuid) {
        return userService.getUserInfo(uuid);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<UUID, UserInfoDTO> getUsersInfo(@RequestBody Set<UUID> uuids) {
        return userService.getUsersInfo(uuids);
    }
}
