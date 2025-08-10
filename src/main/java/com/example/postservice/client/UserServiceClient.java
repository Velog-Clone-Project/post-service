package com.example.postservice.client;

import com.example.postservice.dto.InternalUserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service",
//        url = "http://user-service:8002",
        path = "/internal")
public interface UserServiceClient {

    @GetMapping("/users/{userId}/profile")
    InternalUserProfileResponse getUserProfile(@PathVariable("userId") String userId);
}
