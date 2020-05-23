package com.aga.security.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "role")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RoleEntity extends AbstractDomainClass{

    public RoleEntity(String role) {
        this.role = role;
    }

    private String role;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,  mappedBy = "roles")
    private Set<UserEntity> users = new HashSet<>();

    public void addUser(UserEntity user){
            this.users.add(user);
            user.getRoles().add(this);
    }

    public void removeUser(UserEntity user){
        this.users.remove(user);
        user.getRoles().remove(this);
    }

}