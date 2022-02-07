package com.cd2tec.cargas_sigabem.services;

import java.net.HttpURLConnection;

public interface CepService {

    HttpURLConnection consultarCep(String cep);
}
