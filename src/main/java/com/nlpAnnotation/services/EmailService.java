package com.nlpAnnotation.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void envoyerMotDePasse(String email, String motDePasse) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Votre compte NLP Annotation");
        message.setText("Bonjour,\n\nVotre compte a été créé.\nMot de passe : " + motDePasse + "\n\nMerci.");
        mailSender.send(message);
    }

    public void notifierNouvelleTache(String email, String nomDataset) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Nouvelle tâche assignée");
        message.setText("Une nouvelle tâche vous a été assignée pour le dataset : " + nomDataset);
        mailSender.send(message);
    }
}
