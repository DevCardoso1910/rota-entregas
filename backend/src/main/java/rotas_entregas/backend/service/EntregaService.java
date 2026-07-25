package rotas_entregas.backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rotas_entregas.backend.model.Entrega;
import rotas_entregas.backend.model.Entregador;
import rotas_entregas.backend.repository.EntregaRepository;
import rotas_entregas.backend.repository.EntregadorRepository;
import java.util.Optional;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private EntregadorRepository entregadorRepository;


    @Autowired
    private RoteirizadorService roteirizadorService;

    public Entrega atribuirEntregador(Long entregaId, Entregador entregador) {
        System.out.println("🔍 Buscando entrega ID: " + entregaId);
        Entrega entrega = entregaRepository.findById(entregaId)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));
        System.out.println("📦 Entrega encontrada. Status: " + entrega.getStatus());

        if ("ENTREGUE".equals(entrega.getStatus())) {
            throw new RuntimeException("Entrega já foi entregue, não pode alterar o entregador");
        }
        System.out.println("🧑‍💼 Entregador ID recebido: " + entregador.getId());

        Entregador entregadorCompleto = entregadorRepository.findById(entregador.getId())
                .orElseThrow(() -> new RuntimeException("Entregador não encontrado"));

        System.out.println("📍 Coordenadas do entregador: " + entregadorCompleto.getLatitude() + ", " + entregadorCompleto.getLongitude());
        System.out.println("📍 Coordenadas da entrega: " + entrega.getLatitude() + ", " + entrega.getLongitude());

        if (entregadorCompleto.getLatitude() == null || entregadorCompleto.getLongitude() == null) {
            throw new RuntimeException("Entregador não possui coordenadas de localização");
        }


        String origem = entregadorCompleto.getLatitude() + "," + entregadorCompleto.getLongitude();
        String destino = entrega.getLatitude() + "," + entrega.getLongitude();
        System.out.println("🔹 Origem: " + origem);
        System.out.println("🔹 Destino: " + destino);
        System.out.println("🛣️ Calculando tempo estimado...");
        Long tempo = roteirizadorService.calcularTempoEstimado(origem, destino);
        System.out.println("⏱️ Tempo retornado: " + tempo);

        entrega.setTempoEstimado(tempo != null ? tempo : 0L);
        System.out.println("💾 Salvando entrega...");
        Entrega saved = entregaRepository.save(entrega);
        System.out.println("✅ Entrega salva com sucesso!");

        entrega.setEntregador(entregador);
        entrega.setStatus("EM_ROTA");

        return entregaRepository.save(entrega);


    }

    public Entrega removerEntregador(Long entregaId) {
        Entrega entrega = entregaRepository.findById(entregaId)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));

        if ("ENTREGUE".equals(entrega.getStatus())) {
            throw new RuntimeException("Entrega já foi entregue, não pode remover o entregador");
        }

        entrega.setEntregador(null);
        entrega.setStatus("PENDENTE");

        return entregaRepository.save(entrega);
    }
}


