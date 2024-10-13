package com.ijse.pos_system.services;
//import java.util.List;
//import com.ijse.pos_system.dto.UserDto;
import com.ijse.pos_system.entities.User;

public interface UserService {
    /*UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto updateUser(Long id,UserDto userDto);
    void deleteUser(Long id);
    UserDto getUserByUsername(String username);*/

    User createUser(User user);
}
