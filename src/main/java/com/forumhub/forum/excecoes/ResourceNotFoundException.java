package com.forumhub.forum.excecoes;

    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(Object id) {
            super("Resource not Found. Id " + id);
        }
    }
