package com.mballester.minesweeper.repository;

import com.mballester.minesweeper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
}
