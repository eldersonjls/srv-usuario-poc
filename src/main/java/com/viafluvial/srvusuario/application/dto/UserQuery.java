package com.viafluvial.srvusuario.application.dto;

import com.viafluvial.srvusuario.domain.model.UserType;

/**
 * Query DTO para consultas de usuário.
 * Representa critérios de busca.
 */
public class UserQuery {
    
    private String email;
    private UserType userType;
    
    public UserQuery() {}
    
    public UserQuery(String email, UserType userType) {
        this.email = email;
        this.userType = userType;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
