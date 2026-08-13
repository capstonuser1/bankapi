//package com.example.bankapi.config;
//
//import io.github.bucket4j.Bandwidth;
//import io.github.bucket4j.BucketConfiguration;
//
//public class bucketConfig {
//    public BucketConfiguration createBucketConfig(){
//        Bandwidth limit =Bandwidth.builder()
//                .capacity(2)
//                .refillGreedy(2, java.time.Duration.ofMinutes(1))
//                .build();
//        return BucketConfiguration.builder().addLimit(limit).build();
//    }
//}
