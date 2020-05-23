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

@Entity(name = "permission")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PermissionEntity extends AbstractDomainClass {

    public PermissionEntity(String permission) {
        this.permission = permission;
    }

    private String permission;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    private Set<UserEntity> users = new HashSet<>();

    public void addUser(UserEntity user) {
        this.users.add(user);
        user.getPermissions().add(this);
    }

    public void removeUser(UserEntity user) {
        this.users.remove(user);
        user.getPermissions().remove(this);
    }

}