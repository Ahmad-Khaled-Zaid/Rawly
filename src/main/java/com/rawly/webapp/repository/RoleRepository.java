package com.rawly.webapp.repository;

import java.util.Set;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rawly.webapp.model.Role;;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByNameIn(List<String> roles);

    Role getRoleByName(String name);
}
