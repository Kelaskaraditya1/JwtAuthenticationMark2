package com.StarkIndustries.JwtAuthenticationMark2.Repository;

import com.StarkIndustries.JwtAuthenticationMark2.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    public Users findByUsername(String username);

}
