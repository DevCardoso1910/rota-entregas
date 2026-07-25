package rotas_entregas.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

@Service
public class RoteirizadorService {

    @Value("${tomtom.api-key}")
    private String apiKey;

    private final String TOMTOM_URL = "https://api.tomtom.com/routing/1/calculateRoute/{origem}:{destino}/json";

    private static final String API_KEY = "S1EeUS8X76aJ06C7X4eUtIRdw2kItZgE";

    private static final String TOMTOM_URL_TEMPLATE =
            "https://api.tomtom.com/routing/1/calculateRoute/{origem}:{destino}/json?key=" + API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();

    public Long calcularTempoEstimado(String origemLatLon, String destinoLatLon) {
        System.out.println("🔹 Calculando rota via TomTom: " + origemLatLon + " -> " + destinoLatLon);

        // Monta a URL substituindo os placeholders
        String url = TOMTOM_URL_TEMPLATE
                .replace("{origem}", origemLatLon)
                .replace("{destino}", destinoLatLon);

        System.out.println("🔗 URL gerada: " + url); // 👈 Útil para debug

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();

                // Extrai o travelTimeInSeconds da resposta
                List<Map<String, Object>> routes = (List<Map<String, Object>>) body.get("routes");
                if (routes != null && !routes.isEmpty()) {
                    Map<String, Object> route = routes.get(0);
                    Map<String, Object> summary = (Map<String, Object>) route.get("summary");
                    if (summary != null) {
                        Integer travelTime = (Integer) summary.get("travelTimeInSeconds");
                        if (travelTime != null) {
                            System.out.println("✅ Tempo calculado (TomTom): " + travelTime + " segundos");
                            return travelTime.longValue();
                        }
                    }
                }
            }
        } catch (HttpClientErrorException e) {
            System.err.println("❌ Erro na chamada ao TomTom: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }

        // Fallback para simulação baseada em distância
        System.out.println("⚠️ Fallback para simulação baseada em distância");
        return calcularTempoPorDistancia(origemLatLon, destinoLatLon);
    }

    // 🔹 SIMULAÇÃO AVANÇADA (fallback)
    private Long calcularTempoPorDistancia(String origemLatLon, String destinoLatLon) {
        String[] origem = origemLatLon.split(",");
        String[] destino = destinoLatLon.split(",");

        double lat1 = Double.parseDouble(origem[0]);
        double lon1 = Double.parseDouble(origem[1]);
        double lat2 = Double.parseDouble(destino[0]);
        double lon2 = Double.parseDouble(destino[1]);

        double distancia = calcularDistancia(lat1, lon1, lat2, lon2);
        long tempoSegundos = (long) ((distancia / 40) * 3600);
        return Math.max(tempoSegundos, 60L);
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}