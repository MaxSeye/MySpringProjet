package com.dev.MySpringProject.service;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dev.MySpringProject.exception.OurException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

// Ce code est un service Spring Boot qui permet de téléverser (uploader) une image vers un bucket Amazon S3
@Service
public class Aws3Service {

    private final String bucketName = "pape-project";

    @Value("AKIATG6MGUZVO6JPODMV")
    private String awsS3AccessKey = System.getenv("AWS_ACCESS_KEY_ID");


    @Value("aA17WWJmgQ3MW4XVxQk1nr6boq+rUbv9I7Lq/4Xt")
    private String awsS3SecretKey = System.getenv("AWS_SECRET_ACCESS_KEY");

    public String saveImageToS3(MultipartFile photo ) {
        String s3LocationImage = null;

        try {
            // Étape 1 : Récupérer le nom du fichier

            String s3FileName = photo.getOriginalFilename();

            // Étape 2 : Créer les credentials AWS

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);

            // Étape 3 : Créer un client S3

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.EU_NORTH_1)
                    .build();

            // Étape 4 : Récupérer le flux d'entrée du fichier

            InputStream inputStream = photo.getInputStream();

            // Étape 5 : Définir les métadonnées du fichier

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");

            // Étape 6 : Créer une requête pour téléverser le fichier

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, inputStream, metadata);

            // Étape 7 : Téléverser le fichier vers S3

            s3Client.putObject(putObjectRequest);

            // Étape 8 : Retourner l'URL du fichier téléversé

            return  "https://"+bucketName+ ".s3.amazonaws.com/"+s3FileName;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw  new OurException("Unable to upload image to s3 bucket" +e.getMessage());
        }
    }
}

