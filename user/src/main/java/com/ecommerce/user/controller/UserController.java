package com.ecommerce.user.controller;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponce;
import com.ecommerce.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponce>> getAllusers() {
        return new ResponseEntity<>(userService.fetchAllUser(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createusers(@RequestBody UserRequest userRequest) {
        userService.addUser(userRequest);
        return new ResponseEntity<>("user added Successfully",HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponce> getUser(@PathVariable String id){
        UserResponce userResponse = userService.fetchUser(id);
        if (userResponse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userResponse,HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserRequest updateUserRequest){
        UserResponce updated=userService.updateUser(id,updateUserRequest);
        if(updated==null){
            return new ResponseEntity<>("user is not present",HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>("updated successfully",HttpStatus.OK);
    }

}







