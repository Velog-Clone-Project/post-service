package com.example.postservice.repository;

import com.example.postservice.domain.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {

    boolean existsByPostIdAndUserId(Long postId, String userId);

    @Modifying
    @Query("DELETE FROM PostLikeEntity pl WHERE pl.postId = :postId")
    void deleteByPostId(Long postId);

    void deleteByPostIdAndUserId(Long postId, String userId);

    void deleteByUserId(String userId);
}
