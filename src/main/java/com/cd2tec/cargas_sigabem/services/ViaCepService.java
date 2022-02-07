package com.cd2tec.cargas_sigabem.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Qualifier("principal")
public class ViaCepService implements CepService {

    private final String APIURL = "https://viacep.com.br/ws/";

    @Override
    public HttpURLConnection consultarCep(String cep) {
                try {
                    URL url = new URL( APIURL+ cep + "/json");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    if (connection.getResponseCode() != 200) {
                        throw new RuntimeException("HTTP error code : " + connection.getResponseCode());
                    }
                    return connection;
                } catch (Exception e) {
                  throw new RuntimeException("ERRO: " + e.getMessage());
            }
    }
}
