package com.cd2tec.cargas_sigabem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class FreteInputDTO {

     @DecimalMin(value = "0.001", message = "O Valor de Peso deve ser acima de ZERO")
     private Double peso;
     @NotBlank(message = "Campo cepOrigem não pode ser vazio")
     @NotNull
     @Pattern(regexp = "[0-9]{5}-?[0-9]{3}", message = "CEP ORIGEM Inválido")
     private String cepOrigem;
     @NotBlank(message = "Campo cepDestino não pode ser vazio")
     @NotNull
     @Pattern(regexp = "[0-9]{5}-?[0-9]{3}", message = "CEP DESTINO Inválido")
     private String cepDestino;
     @NotBlank(message = "Campo nomeDestinatario não pode ser vazio")
     @NotNull
     private String nomeDestinatario;
}
