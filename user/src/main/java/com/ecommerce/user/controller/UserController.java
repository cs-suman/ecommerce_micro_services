package com.ecommerce.user.controller;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponce;
import com.ecommerce.user.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
//    public static Logger logger= LoggerFactory.getLogger(UserController.class);

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
        log.info("Request recived for Usern: {}",id);

        log.trace("This is trase level-Very detailed logs");
        log.debug("This is debug level-used for development debugging");
        log.info("This is info level-general system info");
        log.warn("This is warn level-something might be wrong");
        log.error("This is error level-something failed");

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







