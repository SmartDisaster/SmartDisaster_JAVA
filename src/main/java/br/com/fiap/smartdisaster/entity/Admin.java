package br.com.fiap.smartdisaster.entity;

import br.com.fiap.smartdisaster.enums.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ADMIN")
@NoArgsConstructor
public class Admin extends Usuario {

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }
}
