package com.lingshanel.studymoim.user.repository;

import com.lingshanel.studymoim.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmailAndNickname(String email, String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query("""
            select member
            from User member
            where (:keyword is null
                or lower(member.nickname) like lower(concat('%', :keyword, '%'))
                or lower(member.email) like lower(concat('%', :keyword, '%')))
            order by member.createdAt desc
            """)
    List<User> searchForAdmin(@Param("keyword") String keyword);
}
