package com.forumhub.forum.enums;

public enum PerfilEnum {
    ADMIN(1L),
    USER(2L),
    MODERATOR(3L);

    private Long id;

    // O construtor do Enum é sempre privado
    PerfilEnum(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}