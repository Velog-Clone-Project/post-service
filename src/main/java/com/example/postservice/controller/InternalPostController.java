package com.example.postservice.controller;

import com.example.postservice.dto.PostListResponse;
import com.example.postservice.dto.UpdateAuthorInfoRequest;
import com.example.postservice.exception.InvalidCursorIdException;
import com.example.postservice.service.InternalPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/posts/")
public class InternalPostController {

    private final InternalPostService internalPostService;

    @GetMapping
    public ResponseEntity<PostListResponse> getPostsByUserId(
            @RequestParam String userId,
            @RequestParam(required = false) Long cursorId) {

        if (cursorId != null && cursorId <= 0) {
            throw new InvalidCursorIdException();
        }

        PostListResponse response = internalPostService.getPostsByUserId(userId, cursorId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/author-info")
    public ResponseEntity<Void> updateAuthorInfo(@RequestBody UpdateAuthorInfoRequest request) {
        internalPostService.updateAuthorInfo(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/liked")
    public ResponseEntity<PostListResponse> getLikedPosts(
            @RequestParam String userId,
            @RequestParam(required = false) Long cursorId) {

        PostListResponse response = internalPostService.getLikedPostsByUserId(userId, cursorId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/by-user")
    public ResponseEntity<Void> deleteAllPostsByUser(@RequestParam String userId) {
        internalPostService.deleteAllPostsByUserId(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists/{postId}")
    public ResponseEntity<Void> checkPostExists(@PathVariable Long postId) {
        if (internalPostService.postExists(postId)) {
            return ResponseEntity.ok().build(); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

}
