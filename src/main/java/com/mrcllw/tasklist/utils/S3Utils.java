package com.mrcllw.tasklist.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Date;

@Service
public class S3Utils {

    private AmazonS3 s3Client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon(){
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = new AmazonS3Client(credentials);
    }

    private String generateFileName(MultipartFile multipartFile){
        return new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
        fileOutputStream.write(file.getBytes());
        return convertFile;
    }

    private void uploadFileToS3Bucket(String fileName, File file){
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String uploadFile(MultipartFile multipartFile){
        String fileUrl = "";
        try {
            File file = convertMultipartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileToS3Bucket(fileName, file);
            file.delete();
        } catch (Exception e){
            e.printStackTrace();
        }
        return fileUrl;
    }

    public String deleteFileFromS3Bucket(String fileUrl){
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return "Successfully deleted";
    }
}
