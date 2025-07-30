package com.example.postservice.repository;

import com.example.postservice.domain.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity , Long> {

    @Query("SELECT p FROM PostEntity p WHERE (:cursorId IS NULL OR p.id < :cursorId) ORDER BY p.createdAt DESC")
    List<PostEntity> findNextPage(@Param("cursorId") Long cursorId, Pageable pageable);

    // 일반적으로 좋아요 수를 증가시킬때 객체를 get->update 하는 방식으로 하면
    // 두개의 트랜잭션이 거의 동시에 실행되며, 둘 다 기존 likeCount값을 읽고 각각 +1 처리시
    // 최종 결과가 11이 된다.(좋아요 두번 눌렀는데 한번만 올라감)
    // 이것이 대표적인 Race Condition(경쟁 조건) 문제이다.
    // 따라서 DB레벨에서 한줄의 SQL 쿼리로 처리해 중간 상태를 애플리케이션에서 읽고 수정하지 않도록 한다.
    // 여러 트랜잭션이 동시에 실행되도 DB가 자동으로 안전하게 처리하며
    // JPA에서는 @Modifying과 JPQL 사용으로 JVM 간 공유 없이 동시성 제어가 가능하다.
    // 내부적으로 DB의 row-level lock을 사용하여 트랜잭션마자 해당 row의 lock을 획득하고 안전하게 증가/감소 시킴
    @Modifying
    @Query("UPDATE PostEntity p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
    void incrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE PostEntity p SET p.likeCount = p.likeCount - 1 WHERE p.id = :postId AND p.likeCount > 0")
    void decrementLikeCount(@Param("postId") Long postId);

    @Query("""
    SELECT p FROM PostEntity p 
    WHERE p.userId = :userId 
      AND (:cursorId IS NULL OR p.id < :cursorId) 
    ORDER BY p.createdAt DESC
""")
    List<PostEntity> findNextPageByUserId(
            @Param("userId") String userId,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    // 사용자 ID로 작성자 정보를 업데이트하는 메서드
    // @Modifying, @Query는 update문에 필수적
    // clearAutomatically = true는 트랜잭션이 끝난 후 영속성 컨텍스트를 초기화하여
    // 변경된 엔티티가 영속성 컨텍스트에 남아있지 않도록 한다.
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PostEntity p SET p.authorName = :authorName, p.authorProfileImageUrl = :authorProfileImageUrl " +
            "WHERE p.userId = :userId")
    void updateAuthorInfoByUserId(
            @Param("userId") String userId,
            @Param("authorName") String authorName,
            @Param("authorProfileImageUrl") String authorProfileImageUrl
    );

    @Query("""
        SELECT p FROM PostEntity p
        WHERE p.id IN (
            SELECT pl.postId FROM PostLikeEntity pl WHERE pl.userId = :userId
        )
        AND (:cursorId IS NULL OR p.id < :cursorId)
        ORDER BY p.createdAt DESC
    """)
    List<PostEntity> findLikedPostsByUserId(
            @Param("userId") String userId,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    void deleteByUserId(String userId);
}
