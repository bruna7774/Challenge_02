package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class Main {

    private static final String API_KEY = "d7cce04e459c880588412bef"; // Insira sua chave de API aqui
    private static final String[] CURRENCIES = {"USD", "EUR", "GBP", "JPY", "BRL", "AUD"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Conversor de Moedas");
            System.out.println("Escolha a moeda de origem:");
            for (int i = 0; i < CURRENCIES.length; i++) {
                System.out.println((i + 1) + ". " + CURRENCIES[i]);
            }
            int fromChoice = scanner.nextInt();

            System.out.println("Escolha a moeda de destino:");
            for (int i = 0; i < CURRENCIES.length; i++) {
                System.out.println((i + 1) + ". " + CURRENCIES[i]);
            }
            int toChoice = scanner.nextInt();

            System.out.println("Digite o valor a ser convertido:");
            double amount = scanner.nextDouble();

            if (fromChoice < 1 || fromChoice > CURRENCIES.length || toChoice < 1 || toChoice > CURRENCIES.length) {
                System.out.println("Escolha inválida de moeda. Tente novamente.");
                continue;
            }

            String fromCurrency = CURRENCIES[fromChoice - 1];
            String toCurrency = CURRENCIES[toChoice - 1];

            try {
                double rate = getConversionRate(fromCurrency, toCurrency);
                double convertedAmount = amount * rate;
                System.out.printf("%.2f %s é equivalente a %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);
            } catch (Exception e) {
                System.out.println("Erro ao obter taxa de conversão: " + e.getMessage());
            }
        }
    }

    private static double getConversionRate(String fromCurrency, String toCurrency) throws Exception {
        String urlStr = String.format("https://v6.exchangerate-api.com/v6/%s/latest/%s", API_KEY, fromCurrency);
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        if (!jsonResponse.getString("result").equals("success")) {
            throw new Exception("Erro ao buscar taxa de câmbio");
        }

        JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");
        return conversionRates.getDouble(toCurrency);
    }
}
