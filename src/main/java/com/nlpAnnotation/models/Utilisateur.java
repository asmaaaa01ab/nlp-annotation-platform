package com.nlpAnnotation.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "UTILISATEUR")
public class Utilisateur {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nom;
	private String prenom;
	private String login;
	private String password;
	private boolean actif = true;
	@ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
	public Utilisateur() {}
	public Utilisateur(String nom, String prenom, String login, String password) {
		this.nom = nom;
		this.prenom = prenom;
		this.login = login;
		this.password = password;
	}
    public Integer getId() {
    	return this.id;
    }
    public String getNom() {
    	return this.nom;
    }
    public String getPrenom() {
    	return this.prenom;
    }
    public String getLogin() {
    	return this.login;
    }
    public String getPassword() {
    	return this.password;
    }
    public boolean getActif() {
    	return this.actif;
    }
    public Role getRole() {
    	return this.role;
    }
    public void setRole(Role role) {
    	this.role = role;
    }
    public void setPassword(String password) {
    	this.password = password;
    }
    public void setLogin(String login) {
    	this.login = login;
    }
    public void setNom(String nom) {
    	this.nom = nom;
    }
    public void setPrenom(String prenom) {
    	this.prenom = prenom;
    }
    public void setActif(boolean actif) {
    	this.actif = actif;
    }
    public boolean isAdmin() {
        return role != null && "ADMIN_ROLE".equals(role.getNomRole());
    }
    public boolean isAnnotateur() {
        return role != null && "ANNOTATEUR_ROLE".equals(role.getNomRole());
    }
}
