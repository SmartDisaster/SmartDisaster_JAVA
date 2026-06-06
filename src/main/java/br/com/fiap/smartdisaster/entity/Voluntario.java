package br.com.fiap.smartdisaster.entity;

import br.com.fiap.smartdisaster.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("VOLUNTARIO")
@Getter
@Setter
@NoArgsConstructor
public class Voluntario extends Usuario {

    @Column(name = "telefone")
    private String telefone;

    @Override
    public Role getRole() {
        return Role.VOLUNTARIO;
    }
}
