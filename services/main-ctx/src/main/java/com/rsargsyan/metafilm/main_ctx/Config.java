package com.rsargsyan.metafilm.main_ctx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class Config {

  @Value("${s3.access-key-id}")
  private String s3AccessKeyId;

  @Value("${s3.secret-access-key}")
  private String s3SecretAccessKey;

  @Value("${s3.region}")
  private String s3Region;

  @Value("${s3.endpoint}")
  private String s3Endpoint;

  @Value("${s3.bucket}")
  public String s3Bucket;

  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
        .endpointOverride(URI.create(s3Endpoint))
        .region(Region.of(s3Region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(s3AccessKeyId, s3SecretAccessKey)))
        .build();
  }
}
