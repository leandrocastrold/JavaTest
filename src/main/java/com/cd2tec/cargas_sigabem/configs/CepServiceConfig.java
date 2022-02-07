package com.cd2tec.cargas_sigabem.configs;

import com.cd2tec.cargas_sigabem.services.CepService;
import com.cd2tec.cargas_sigabem.services.ViaCepService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CepServiceConfig {

    @Qualifier("principal")
    @Bean
    public CepService viaCepService(){
        CepService cepService = new ViaCepService();
        return cepService;
    }

}
