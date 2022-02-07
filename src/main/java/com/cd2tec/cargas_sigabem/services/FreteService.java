package com.cd2tec.cargas_sigabem.services;

import com.cd2tec.cargas_sigabem.dtos.FreteInputDTO;
import com.cd2tec.cargas_sigabem.dtos.FreteOutputDTO;
import com.cd2tec.cargas_sigabem.models.Endereco;
import com.cd2tec.cargas_sigabem.models.Frete;
import com.cd2tec.cargas_sigabem.repositories.FreteRepository;
import com.cd2tec.cargas_sigabem.utils.Util;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FreteService {

    @Autowired
    private FreteRepository repository;

    private final String apiUrl = "https://viacep.com.br/ws/";

    public List<Frete> listar() {
        List<Frete> fretes = repository.findAll();
        return fretes;
    }

    public ResponseEntity<Frete> listarPorId(Long id) {
        Optional<Frete> optional = repository.findById(id);
        if (optional.isPresent()) {
            Frete frete = optional.get();
            return new ResponseEntity<>(frete, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public void salvar(FreteInputDTO inputDTO, FreteOutputDTO outputDTO) {
        Frete frete = preencherDadosDoFreteComDtos(inputDTO, outputDTO);
        repository.save(frete);
    }

    public ResponseEntity<FreteOutputDTO> iniciarCalculoDeFreteAPartirDoInputDTO(FreteInputDTO inputDTO) {

        ResponseEntity<Endereco> endereco1 = null;
        ResponseEntity<Endereco> endereco2 = null;

        try {
            endereco1 = localizarEnderecoPeloCEP(inputDTO.getCepOrigem());
            endereco2 = localizarEnderecoPeloCEP(inputDTO.getCepDestino());
        }   catch (Exception e) {
            e.printStackTrace();
        }

        if (endereco1.getStatusCode() != HttpStatus.OK || endereco2.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Boolean dddsIguais = vericarSeDDDsSaoIguais(endereco1.getBody().getDdd(), endereco2.getBody().getDdd());
        Boolean estadosIguais = verificarSeEstadosSaoIguais(endereco1.getBody().getUf(), endereco2.getBody().getUf());

        FreteOutputDTO outputDTO = calcularFreteEDataDeEntrega(inputDTO, dddsIguais, estadosIguais);
        salvar(inputDTO, outputDTO);

        return new ResponseEntity<>(outputDTO, HttpStatus.CREATED);
    }


    private Frete preencherDadosDoFreteComDtos(FreteInputDTO inputDTO, FreteOutputDTO outputDTO) {
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


    private ResponseEntity<Endereco> localizarEnderecoPeloCEP(String cep) throws Exception {
        Endereco endereco = null;
        HttpURLConnection connection = verificarConexao(cep);

        BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        String resposta = Util.converteJsonParaString(br);

        // VIACEP API retorna um json com o atributo "erro" quando o CEP está no formato correto, mas não existe no sistema
        if (resposta.contains("erro")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Gson gson = new Gson();
        endereco = gson.fromJson(resposta, Endereco.class);
        System.out.println(endereco);
        return new ResponseEntity<>(endereco, HttpStatus.OK);
    }

    private FreteOutputDTO calcularFreteEDataDeEntrega(FreteInputDTO inputDTO, Boolean dddsIguais, Boolean estadosIguais) {
        BigDecimal valorFreteSemDesconto = new BigDecimal(inputDTO.getPeso());
        BigDecimal valorDesconto = new BigDecimal(0);
        LocalDate dataDeEntrega;

        if (dddsIguais) {
            valorDesconto = new BigDecimal(0.5);
            dataDeEntrega = LocalDate.now().plusDays(1);
        } else if (estadosIguais) {
            valorDesconto = new BigDecimal(0.75);
            dataDeEntrega = LocalDate.now().plusDays(3);
        } else {
            dataDeEntrega = LocalDate.now().plusDays(10);
        }
        BigDecimal valorFinal = valorFreteSemDesconto.multiply(valorDesconto);
        return new FreteOutputDTO(valorFinal, dataDeEntrega, inputDTO.getCepOrigem(), inputDTO.getCepDestino());
    }

    private HttpURLConnection verificarConexao(String cep) throws Exception {
        try {
            URL url = new URL(apiUrl + cep + "/json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code : " + connection.getResponseCode());
            }
            return connection;
        } catch (Exception e) {
            throw new Exception("ERRO: " + e.getMessage());
        }
    }

    private Boolean verificarSeEstadosSaoIguais(String uf1, String uf2) {
        if (uf1.compareTo(uf2) == 0) {
            return true;
        }
        return false;
    }

    private Boolean vericarSeDDDsSaoIguais(String ddd1, String ddd2) {
        if (ddd1.compareTo(ddd2) == 0) {
            return true;
        }
        return false;
    }
}
