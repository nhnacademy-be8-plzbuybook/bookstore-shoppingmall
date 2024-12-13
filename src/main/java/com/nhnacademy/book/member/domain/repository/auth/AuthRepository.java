package com.nhnacademy.book.member.domain.repository.auth;

import com.nhnacademy.book.member.domain.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
}
