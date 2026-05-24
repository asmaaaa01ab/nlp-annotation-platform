package com.nlpAnnotation.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nlpAnnotation.exceptions.UtilisateurNotFoundException;
import com.nlpAnnotation.models.Role;
import com.nlpAnnotation.models.Utilisateur;
import com.nlpAnnotation.patterns.decorator.BcryptPasswordDecorator;
import com.nlpAnnotation.patterns.decorator.SimplePasswordEncoder;
import com.nlpAnnotation.patterns.factory.AdministrateurFactory;
import com.nlpAnnotation.patterns.factory.AnnotateurFactory;
import com.nlpAnnotation.patterns.factory.UtilisateurFactory;
import com.nlpAnnotation.repository.RoleRepository;
import com.nlpAnnotation.repository.UtilisateurRepository;

@Service
public class UtilisateurService {
	
	private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              RoleRepository roleRepository,
                              EmailService emailService) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
    }

    public Utilisateur creerAnnotateur(String nom, String prenom, String login, String email) {
        // Pattern Factory — création via la fabrique
        UtilisateurFactory factory = new AnnotateurFactory();
        Utilisateur annotateur = factory.creer(nom, prenom, login);

        Role role = roleRepository.findByNomRole("ANNOTATEUR_ROLE");
        if (role == null) {
            role = roleRepository.save(new Role("ANNOTATEUR_ROLE"));
        }
        annotateur.setRole(role);

        String motDePasseBrut = genererMotDePasse();
        BcryptPasswordDecorator encoder = new BcryptPasswordDecorator(new SimplePasswordEncoder());
        annotateur.setPassword(encoder.encoder(motDePasseBrut));

        emailService.envoyerMotDePasse(email, motDePasseBrut);

        return utilisateurRepository.save(annotateur);
    }

    public Utilisateur creerAdministrateur(String nom, String prenom, String login, String email) {
        UtilisateurFactory factory = new AdministrateurFactory();
        Utilisateur admin = factory.creer(nom, prenom, login);

        Role role = roleRepository.findByNomRole("ADMIN_ROLE");
        if (role == null) {
            role = roleRepository.save(new Role("ADMIN_ROLE"));
        }
        admin.setRole(role);

        String motDePasseBrut = genererMotDePasse();
        BcryptPasswordDecorator encoder = new BcryptPasswordDecorator(new SimplePasswordEncoder());
        admin.setPassword(encoder.encoder(motDePasseBrut));
        emailService.envoyerMotDePasse(email, motDePasseBrut);
        return utilisateurRepository.save(admin);
    }

    public List<Utilisateur> getAnnotateursActifs() {
        return utilisateurRepository.findByActifTrue()
            .stream()
            .filter(u -> u.isAnnotateur())
            .collect(Collectors.toList());
    }

    public Utilisateur modifierUtilisateur(int id, String nom, String prenom, String login) {
        Utilisateur u = utilisateurRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setLogin(login);
        return utilisateurRepository.save(u);
    }

    public void desactiverUtilisateur(int id) {
        Utilisateur u = utilisateurRepository.findById(id)
            .orElseThrow(() -> new UtilisateurNotFoundException(id));
        u.setActif(false);
        utilisateurRepository.save(u);
    }

    public Utilisateur findById(int id) {
        return utilisateurRepository.findById(id)
            .orElseThrow(() -> new UtilisateurNotFoundException(id));
    }

    public Utilisateur findByLogin(String login) {
        return utilisateurRepository.findByLogin(login);
    }

    public boolean loginExiste(String login) {
        return utilisateurRepository.existsByLogin(login);
    }

    private String genererMotDePasse() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
