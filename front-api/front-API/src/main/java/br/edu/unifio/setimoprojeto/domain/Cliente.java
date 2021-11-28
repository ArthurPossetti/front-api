package br.edu.unifio.setimoprojeto.domain;


import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Cliente {
    private Integer USR_ID;

    @NotBlank(message = "O campo e-mail é obrigatório!")
    private String USR_EMAIL;

    @NotBlank(message = "O campo senha é obrigatório!")
    private String USR_SENHA;



}
