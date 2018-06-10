package com.mrcllw.tasklist.controller;

import com.mrcllw.tasklist.model.User;
import com.mrcllw.tasklist.service.UserService;
import com.mrcllw.tasklist.utils.FacebookUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Optional;

import static com.mrcllw.tasklist.utils.FacebookUtils.GRAPH_API_URL;
import static com.mrcllw.tasklist.utils.FacebookUtils.PICTURE_PARAMS;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity signUp(@RequestParam("file") Optional<MultipartFile> file,
                                 @RequestParam("user") String userJson){

        User user = userService.parseUser(userJson);
        User userSave = userService.findByEmail(user.getEmail());
        if(userSave != null){
            return ResponseEntity.status(409).body("User already exists.");
        }

        userSave = new User();
        userSave.setName(user.getName());
        userSave.setEmail(user.getEmail());
        userSave.setPassword(user.getPassword());
        if(file.isPresent()){
            userSave.setAvatar(userService.uploadAvatar(file.get()));
        }

        return ResponseEntity.status(201).body(userService.save(userSave));
    }

    @PutMapping("/{id}")
    public ResponseEntity editUser(@PathVariable Long id,
                                   @RequestParam("file") Optional<MultipartFile> file,
                                   @RequestParam("user") String userJson){

        User userEdit = userService.parseUser(userJson);
        if(file.isPresent()){
            if(userEdit.getAvatar() != null){
                userService.deleteAvatar(userEdit.getAvatar());
            }
            userEdit.setAvatar(userService.uploadAvatar(file.get()));
        }

        return ResponseEntity.status(200).body(userService.edit(userEdit));
    }

    @PostMapping("/facebook-authenticate")
    public ResponseEntity signInWithFacebook(@RequestBody String facebookToken){

        HashMap<String, String> userDataMap = FacebookUtils.getUserDataFromFacebook(facebookToken);

        if(userDataMap == null){
            return ResponseEntity.status(401).body("Invalid token.");
        }

        User userFacebook = userService.findByEmail(userDataMap.get("email"));
        if(userFacebook == null){
            userFacebook = new User();
            userFacebook.setName(userDataMap.get("name"));
            userFacebook.setEmail(userDataMap.get("email"));
            userFacebook.setPassword(userService.generatePassword());
            userFacebook.setAvatar(GRAPH_API_URL + userDataMap.get("id") + PICTURE_PARAMS);
            userService.save(userFacebook);
        }

        return ResponseEntity.status(200).body(userFacebook);
    }

    @PostMapping("/authenticate")
    public ResponseEntity signIn(@RequestBody User user){

        User userAuth = userService.findByEmail(user.getEmail());
        if(userAuth == null){
            return ResponseEntity.status(404).body("User not exists.");
        } else if (!userAuth.getPassword().equals(user.getPassword())){
            return ResponseEntity.status(401).body("Username or password incorrect.");
        }

        return ResponseEntity.status(200).body(userAuth);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity forgotPassword(@RequestBody String email){

        User userRecovery = userService.findByEmail(email);

        if(userRecovery == null){
            return ResponseEntity.status(400).body("User not exists.");
        }

        String newPassword = userService.generatePassword();
        userRecovery.setPassword(newPassword);

        String subject = "Password Recovery";
        String message = "Hello " + userRecovery.getName() + "!\n" + "Your new password is " + newPassword;

        if(userService.sendEmail(subject, message, email)){
            userService.save(userRecovery);
        }

        return ResponseEntity.status(200).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable Long id){
        User user = userService.findById(id);
        if(user == null){
            return ResponseEntity.status(404).body("User not exists.");
        }
        return ResponseEntity.status(201).body(user);
    }
}
