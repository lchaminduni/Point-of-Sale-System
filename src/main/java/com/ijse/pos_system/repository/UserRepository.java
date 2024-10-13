package com.ijse.pos_system.repository;

import java.util.Optional;
import com.ijse.pos_system.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username); //for loging or fetching by user name

    
}
