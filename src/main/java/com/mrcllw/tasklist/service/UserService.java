package com.mrcllw.tasklist.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrcllw.tasklist.model.User;
import com.mrcllw.tasklist.repository.UserRepository;
import com.mrcllw.tasklist.utils.EmailUtils;
import com.mrcllw.tasklist.utils.S3Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private S3Utils s3Utils;

    public User save (User user){
        return userRepository.save(user);
    }

    public User edit (User user){
        return userRepository.save(user);
    }

    public User findById(Long id){
        return userRepository.findUserById(id);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User parseUser(String json){
        User user = null;
        try {
            user = new ObjectMapper().readValue(json, User.class);
        } catch (IOException e){
            e.printStackTrace();
        }
        return user;
    }

    public String uploadAvatar(MultipartFile file){
        return s3Utils.uploadFile(file);
    }

    public void deleteAvatar(String url){
        s3Utils.deleteFileFromS3Bucket(url);
    }

    public String generatePassword(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public boolean sendEmail(String subject, String message, String emailTo){
        if(emailUtils.sendEmail(subject, message, emailTo)) return true;
        return false;
    }

}
