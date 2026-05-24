package com.nlpAnnotation.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(UtilisateurNotFoundException.class)
    public String handleUtilisateurNotFound(
            UtilisateurNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(DatasetNotFoundException.class)
    public String handleDatasetNotFound(
            DatasetNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(TacheNotFoundException.class)
    public String handleTacheNotFound(
            TacheNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(SpamDetectedException.class)
    public String handleSpam(
            SpamDetectedException ex, Model model) {
        model.addAttribute("erreur", ex.getMessage());
        return "annotateur/annotate";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(
            NoHandlerFoundException ex, Model model) {
        model.addAttribute("message",
            "Page introuvable : " + ex.getRequestURL());
        return "errors/404";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        if (ex.getClass().getName().contains("security")) {
            throw new RuntimeException(ex);
        }
        model.addAttribute("message", ex.getMessage());
        return "errors/500";
    }
}
