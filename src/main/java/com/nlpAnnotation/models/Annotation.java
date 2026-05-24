package com.nlpAnnotation.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ANNOTATION")
public class Annotation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "classe_choisie")
	private String classe;
	private LocalDateTime dateAnnotation;
	@ManyToOne
	@JoinColumn(name = "annotateur_id")
	private Utilisateur annotateur;
	@ManyToOne
	@JoinColumn(name = "couple_id")
	private CoupleTexte coupleTexte;
	public Annotation() {}
	public Annotation(String classe, Utilisateur annotateur, CoupleTexte coupleTexte) {
		this.classe = classe;
		this.annotateur = annotateur;
		this.coupleTexte = coupleTexte;
	}
	public Integer getId() {
    	return this.id;
    }
	public String getClasse() {
    	return this.classe;
    }
	public LocalDateTime getDateAnnotation() {
    	return this.dateAnnotation;
    }
	public Utilisateur getAnnotateur() {
    	return this.annotateur;
    }
	public CoupleTexte getCoupleTexte() {
    	return this.coupleTexte;
    }
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public void setDateAnnotation(LocalDateTime dateAnnotation) {
		this.dateAnnotation = dateAnnotation;
	}
	public void setAnnotateur(Utilisateur annotateur) {
		this.annotateur = annotateur;
	}
	public void setCoupleTexte(CoupleTexte couple) {
		this.coupleTexte = couple;
	}
	
}
