package com.cd2tec.cargas_sigabem.utils;

import java.io.BufferedReader;

public class Util {

    public static String converteJsonParaString (BufferedReader br) {
        String saida = "";
        String linha;
        try {
            while ((linha = br.readLine()) != null) {
                saida += linha;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saida;
    }
}
