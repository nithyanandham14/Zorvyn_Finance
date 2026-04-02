package com.bjsn.finance.repo;

import com.bjsn.finance.Module.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Userrepo extends JpaRepository<Users,Integer>{
    Users findByUsername(String username);
}
