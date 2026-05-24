package com.nlpAnnotation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nlpAnnotation.models.Role;
import com.nlpAnnotation.models.Utilisateur;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer>{
	Role findByNomRole(String nomRole);
}
