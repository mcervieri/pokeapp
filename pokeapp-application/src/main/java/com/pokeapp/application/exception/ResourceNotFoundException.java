package com.pokeapp.application.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forPokemon(Integer id) {
        return new ResourceNotFoundException("Pokemon not found with id: " + id);
    }

    public static ResourceNotFoundException forPokemon(String name) {
        return new ResourceNotFoundException("Pokemon not found with name: " + name);
    }

    public static ResourceNotFoundException forType(Integer id) {
        return new ResourceNotFoundException("Type not found with id: " + id);
    }

    public static ResourceNotFoundException forType(String name) {
        return new ResourceNotFoundException("Type not found with name: " + name);
    }

    public static ResourceNotFoundException forAbility(Integer id) {
        return new ResourceNotFoundException("Ability not found with id: " + id);
    }

    public static ResourceNotFoundException forAbility(String name) {
        return new ResourceNotFoundException("Ability not found with name: " + name);
    }

    public static ResourceNotFoundException forItem(Integer id) {
        return new ResourceNotFoundException("Item not found with id: " + id);
    }

    public static ResourceNotFoundException forItem(String name) {
        return new ResourceNotFoundException("Item not found with name: " + name);
    }

    public static ResourceNotFoundException forMove(Integer id) {
        return new ResourceNotFoundException("Move not found with id: " + id);
    }

    public static ResourceNotFoundException forMove(String name) {
        return new ResourceNotFoundException("Move not found with name: " + name);
    }

    public static ResourceNotFoundException forNature(Integer id) {
        return new ResourceNotFoundException("Nature not found with id: " + id);
    }

    public static ResourceNotFoundException forNature(String name) {
        return new ResourceNotFoundException("Nature not found with name: " + name);
    }

    public static ResourceNotFoundException forTrainer(String username) {
        return new ResourceNotFoundException("Trainer not found with username: " + username);
    }

    public static ResourceNotFoundException forComment(Integer id) {
        return new ResourceNotFoundException("Comment not found with id: " + id);
    }
}