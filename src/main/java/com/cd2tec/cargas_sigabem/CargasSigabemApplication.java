package com.cd2tec.cargas_sigabem;

import com.cd2tec.cargas_sigabem.models.Endereco;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@SpringBootApplication
public class CargasSigabemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CargasSigabemApplication.class, args);
	}
//
//	@Override
//	public void run(String... args) throws Exception {
////		System.out.println("DIGITE O CEP: ");
////		String cep = scanner.nextLine();
////		URL url = new URL("https://viacep.com.br/ws/" + cep + "/json");
////		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////
////		if (connection.getResponseCode() != 200) {
////		 throw new RuntimeException("HTTP error code : " + connection.getResponseCode());
////		}
////
////		BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
////
////		String output = "";
////		String line;
////		while ((line = br.readLine()) != null) {
////			output += line;
////		}
////		GsonJsonParser gsonJsonParser = new GsonJsonParser();
////		Gson gson = new Gson();
////		Endereco endereco = gson.fromJson(output, Endereco.class);
////		System.out.println(endereco);
//
//	}
}
