# NLP Annotation Platform

Application web Java Spring MVC  permettant l’annotation manuelle de données textuelles dans le cadre d’un projet de classification de texte en Traitement Automatique du Langage Naturel (NLP). Les annotations collectées seront utilisées pour entraîner et évaluer des modèles Deep Learning de classification supervisée.

## Fonctionnalités

- Authentification sécurisée avec Spring Security (rôles Admin / Annotateur)
- Gestion des annotateurs : création, modification, désactivation
- Envoi automatique du mot de passe par email à la création du compte
- Gestion des datasets : import CSV/JSON, export CSV/JSON
- Assignation de tâches d'annotation aux annotateurs
- Interface d'annotation de couples de textes
- Détection anti-spam (vitesse et répétition) via Chain of Responsibility
- Statistiques d'avancement via pattern Observer

## Stack technique

| Couche | Technologie |
|--------|------------|
| Langage | Java 17 |
| Framework | Spring MVC, Spring Security |
| Persistance | Hibernate / JPA |
| Base de données | MySQL |
| Email | JavaMailSender (SMTP Gmail) |
| Vue | Thymeleaf / HTML |
| Build | Maven |

## Design Patterns utilisés

- **Factory** — création des utilisateurs (Annotateur, Administrateur)
- **Builder** — construction des Datasets
- **Decorator** — encodage du mot de passe (BCrypt)
- **Facade** — AdminFacade pour simplifier les opérations admin
- **Proxy** — contrôle d'accès au service d'annotation
- **Singleton** — configuration applicative
- **Chain of Responsibility** — détection de spam (vitesse, répétition)
- **Strategy** — import de données (CSV, JSON)
- **Template Method** — export de données (CSV, JSON)
- **Observer** — suivi de l'avancement des annotations

## Installation

### Prérequis
- Java 17+
- Maven 3.8+
- MySQL 8+

### Configuration

1. Cloner le repository :
```bash
   git clone https://github.com/asmaaaa01ab/nlp-annotation-platform.git
   cd nlp-annotation-platform
```

2. Créer la base de données MySQL :
```sql
   CREATE DATABASE db_nlp_annotation;
```

3. Configurer la connexion dans `src/main/java/com/nlpAnnotation/config/PersistenceConfig.java` :
   Les champs `url`, `username` et `password` ont été retirés du code source pour des raisons de sécurité.
   Renseignez vos propres valeurs avant de lancer l'application :
   \```
   URL      : jdbc:mysql://localhost:3306/db_nlp_annotation
   Username : ton_user
   Password : ton_password
   \```

4. Configurer l'envoi d'email dans `src/main/java/com/nlpAnnotation/config/MailConfig.java`. 
   De même, les credentials email ont été retirés du code source.
   Renseignez vos propres valeurs :
   \```
   Username : ton_email@gmail.com
   Password : ton_app_password_gmail
   \```
   > ⚠️ Pour Gmail, utilisez un **App Password** et non votre mot de passe habituel.
   > Compte Google → Sécurité → Validation en 2 étapes → Mots de passe des applications

5. Lancer l'application :
   \```bash
   mvn clean install
   \```
   Puis déployer le `.war` sur Tomcat.

## Structure du projet

\```
src/main/java/com/nlpAnnotation/
├── config/          # Spring, Security, Mail, Persistence
├── controllers/     # AdminController, AnnotateurController
├── models/          # Utilisateur, Dataset, Tache, Annotation...
├── repository/      # Accès base de données (JPA)
├── services/        # Logique métier + Email
├── patterns/        # Design patterns (factory, builder, proxy...)
└── exceptions/      # Gestion globale des erreurs
\```

