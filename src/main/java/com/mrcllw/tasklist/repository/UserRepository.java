package com.mrcllw.tasklist.repository;

import com.mrcllw.tasklist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findUserById(Long id);
}
