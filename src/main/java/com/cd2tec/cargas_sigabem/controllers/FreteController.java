package com.cd2tec.cargas_sigabem.controllers;

import com.cd2tec.cargas_sigabem.dtos.FreteInputDTO;
import com.cd2tec.cargas_sigabem.dtos.FreteOutputDTO;
import com.cd2tec.cargas_sigabem.models.Frete;
import com.cd2tec.cargas_sigabem.services.FreteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/v1/frete")
public class FreteController {

    @Autowired
    private FreteService service;

    @ApiOperation(value = "Retorna uma lista de consultas de frete")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Erro do servidor"),
    })
    @GetMapping( produces = "application/json")
    public ResponseEntity<List<Frete>> listar(){
        return new ResponseEntity<>(service.listar(), HttpStatus.OK);
    }


    @ApiOperation(value = "Retorna uma consulta de frete especificado pelo ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Nada encontrado"),
            @ApiResponse(code = 500, message = "Erro do servidor"),
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Frete> listarPorId(@PathVariable Long id){
        return service.listarPorId(id);
    }

    @ApiOperation(value = "Insere dados necessários para cálculo do frete e retorna o resultado")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Cálculo realizado e resultado persistido"),
            @ApiResponse(code = 400, message = "Algum erro na requisição"),
            @ApiResponse(code = 500, message = "Erro do servidor"),
    })
    @PostMapping(produces = "application/json",  consumes = "application/json")
    public ResponseEntity<FreteOutputDTO> postar(@RequestBody @Valid FreteInputDTO inputDTO){
        return service.iniciarCalculoDeFreteAPartirDoInputDTO(inputDTO);
    }

}
