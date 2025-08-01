package com.example.postservice.client;

import com.example.postservice.dto.CommentDto;
import com.example.postservice.dto.InternalUserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "comment-service", url = "http://comment-service:8004")
public interface CommentServiceClient {

    @GetMapping("/internal/comments")
    List<CommentDto> getCommentsByPostId(@RequestParam Long postId);
}
