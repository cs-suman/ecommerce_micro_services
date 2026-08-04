package com.ecommerce.user.services;


import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponce;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponce> fetchAllUser() {
        List<User> userList = userRepository.findAll();
        List<UserResponce> list = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            UserResponce temp = mapUserResponce(userList.get(i));
            list.add(temp);
        }

        return list;
    }

    public void addUser(UserRequest userRequest) {

        User user = new User();
        updateUserFromRequest(user, userRequest);
        userRepository.save(user);
    }


    public UserResponce fetchUser(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return null;
        }
        return mapUserResponce(user.get());
    }

    public UserResponce updateUser(String id, UserRequest updateUserRequest) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return null;
        }

        User user1 = user.get();
        updateUserFromRequest(user1, updateUserRequest);

        User updateUser = userRepository.save(user1);
        return mapUserResponce(updateUser);
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if (userRequest.getAddress() != null) {


            Address address = new Address();

            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setZipcode(userRequest.getAddress().getZipcode());


            user.setAddress(address);
        }
    }

    private UserResponce mapUserResponce(User user) {
        UserResponce userResponceresponse = new UserResponce();
        userResponceresponse.setId(String.valueOf(user.getId()));
        userResponceresponse.setLastName(user.getLastName());
        userResponceresponse.setFirstName(user.getFirstName());
        userResponceresponse.setEmail(user.getEmail());
        userResponceresponse.setPhone(user.getPhone());
        userResponceresponse.setRole(user.getUserRole());


        if (user.getAddress() != null) {

            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            userResponceresponse.setAddress(addressDTO);
        }
        return userResponceresponse;
    }
}
