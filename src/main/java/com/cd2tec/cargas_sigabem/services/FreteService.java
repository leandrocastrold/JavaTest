package com.cd2tec.cargas_sigabem.services;

import com.cd2tec.cargas_sigabem.dtos.FreteInputDTO;
import com.cd2tec.cargas_sigabem.dtos.FreteOutputDTO;
import com.cd2tec.cargas_sigabem.models.Endereco;
import com.cd2tec.cargas_sigabem.models.Frete;
import com.cd2tec.cargas_sigabem.repositories.FreteRepository;
import com.cd2tec.cargas_sigabem.utils.Util;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class FreteService {

    @Autowired
    private FreteRepository repository;

    public List<Frete> listar(){
        List<Frete> fretes = repository.findAll();
        return fretes;
    }

    public Frete listarPorId(Long id) {
        Optional<Frete> optional = repository.findById(id);
        if (optional.isPresent()) {
            Frete frete = optional.get();
            return frete;
        }
        return new Frete();
    }

    public FreteOutputDTO calcularFrete(FreteInputDTO inputDTO){
        BigDecimal valorFrete = new BigDecimal(inputDTO.getPeso());
        Endereco endereco1 = localizarEnderecoPeloCEP(inputDTO.getCepOrigem());
        Endereco endereco2 = localizarEnderecoPeloCEP(inputDTO.getCepDestino());
        Boolean dddIguais = vericarSeDDDsSaoIguais(endereco1.getDdd(), endereco2.getDdd());
        Boolean estadosIguais = verificarSeEstadosSaoIguais(endereco1.getUf(), endereco2.getUf());
        BigDecimal valorComDesconto = valorFrete;
        LocalDate date;

        if (dddIguais) {
            valorComDesconto  = valorFrete.multiply(new BigDecimal(0.5));
            date = LocalDate.now().plusDays(1);
        }
        else if (estadosIguais) {
            valorComDesconto  = valorFrete.multiply(new BigDecimal(0.75));
            date = LocalDate.now().plusDays(3);
        } else {
            date = LocalDate.now().plusDays(10);
        }

        System.out.println("São do mesmo DDD? " + dddIguais);
        System.out.println("São do mesmo Estado?  " + estadosIguais);
        System.out.println("Valor original: " + valorFrete);
        System.out.println("Valor com desconto: " + valorComDesconto);
        System.out.println("Data da entrega: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        FreteOutputDTO outputDTO = new FreteOutputDTO(valorComDesconto, date, inputDTO.getCepOrigem(), inputDTO.getCepDestino());

        salvar(inputDTO, outputDTO);
        return outputDTO;
    }

    public void salvar(FreteInputDTO inputDTO, FreteOutputDTO outputDTO){
    Frete frete = preencherDadosDoFreteComDtos(inputDTO, outputDTO);
    repository.save(frete);
    }

    private Frete preencherDadosDoFreteComDtos(FreteInputDTO inputDTO, FreteOutputDTO outputDTO){
        Frete frete = new Frete();
        //Dados do INPUTDTO
        frete.setPeso(inputDTO.getPeso());
        frete.setCepOrigem(inputDTO.getCepOrigem());
        frete.setCepDestino(inputDTO.getCepDestino());
        frete.setNomeDestinatario(inputDTO.getNomeDestinatario());

        //Dados do OUTPUTDTO
        frete.setVlTotalFrete(outputDTO.getVlTotalFrete());
        frete.setDataPrevistaEntrega(outputDTO.getDataPrevistaEntrega());
        frete.setDataConsulta(LocalDateTime.now());

        return frete;
    }


    private Endereco localizarEnderecoPeloCEP(String cep) {
        Endereco endereco = null;
        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code : " + connection.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String resposta = Util.converteJsonParaString(br);
            Gson gson = new Gson();
            endereco = gson.fromJson(resposta, Endereco.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return endereco;
    }

    private Boolean verificarSeEstadosSaoIguais(String uf1, String uf2) {
        if (uf1.compareTo(uf2) == 0) {
            return true;
        }
        return false;
    }

    private Boolean vericarSeDDDsSaoIguais(String ddd1, String ddd2) {
        //São iguais?
        if (ddd1.compareTo(ddd2) == 0) {
            return true;
        }
            return false;
    }
}
