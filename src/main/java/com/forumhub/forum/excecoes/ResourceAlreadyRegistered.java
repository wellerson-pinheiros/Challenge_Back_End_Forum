package com.forumhub.forum.excecoes;

public class ResourceAlreadyRegistered extends RuntimeException {
    public ResourceAlreadyRegistered(String message) {
        super(message);
    }
}
