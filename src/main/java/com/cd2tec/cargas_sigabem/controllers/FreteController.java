package com.cd2tec.cargas_sigabem.controllers;

import com.cd2tec.cargas_sigabem.dtos.FreteInputDTO;
import com.cd2tec.cargas_sigabem.dtos.FreteOutputDTO;
import com.cd2tec.cargas_sigabem.models.Frete;
import com.cd2tec.cargas_sigabem.services.FreteService;
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

    @GetMapping()
    public ResponseEntity<List<Frete>> listar(){
        return new ResponseEntity<>(service.listar(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Frete> listarPorId(@PathVariable Long id){
        return new ResponseEntity<>(service.listarPorId(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<FreteOutputDTO> postar(@RequestBody @Valid FreteInputDTO dto){
        return new ResponseEntity<>(service.calcularFrete(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@RequestBody Long id){

    }


}
