package com.nlpAnnotation.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ROLE")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "nom_role")
	private String nomRole;
	public Role() {}
	public Role(String nom) {
		this.nomRole = nom;
	}
	public Integer getId() {
		return this.id;
	}
	public String getNomRole() {
		return this.nomRole;
	}
	public void setNomRole(String nom) {
		this.nomRole = nom;
	}
}
