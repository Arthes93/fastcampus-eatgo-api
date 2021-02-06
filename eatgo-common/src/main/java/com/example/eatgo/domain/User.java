package com.example.eatgo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotEmpty
    private String email;

    @NotEmpty
    private String name;

    @NotNull
    private Long level;

    @NotEmpty
    private String password;


    public boolean isAdmin() {
        return level > 2L;
    }

    public void deactivate(){
        level = 0L;
    }

    public boolean isActive() {
        return level > 0L;
    }

    @JsonIgnore
    public String getAccessToken() {
        if(password == null){
            return "";
        }

        return password.substring(0, 10);
    }
}
