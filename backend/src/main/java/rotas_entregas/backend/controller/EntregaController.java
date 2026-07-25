package rotas_entregas.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rotas_entregas.backend.model.Entrega;
import rotas_entregas.backend.model.Entregador;
import rotas_entregas.backend.repository.EntregaRepository;
import rotas_entregas.backend.service.EntregaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;

@RestController
@RequestMapping("/entregas")
@Tag(name = "Entregas", description = "Endpoints para gerenciar entregas")
public class EntregaController {

    @Autowired
    private EntregaRepository repository;

    @Autowired
    private EntregaService service;

    @GetMapping
    @Operation(summary = "Listar todas as entregas",
            description = "Retorna uma lista com todas as entregas cadastradas")
    public List <Entrega> listar(){
        return repository.findAll();
    }

    @PostMapping
    @Operation(summary = "Criar uma nova entrega",
              description = "Cadastra uma nova entrega no sistema")
    @ApiResponse(responseCode = "200", description = "Entrega criada com sucesso")
    public Entrega criar(@Valid @RequestBody Entrega entrega) {
        return repository.save(entrega);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar entrega por ID",
               description = "Retorna os detalhes de uma entrega específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrega encontrada"),
            @ApiResponse(responseCode = "404", description = "Entrega não encontrada")
    })
    public ResponseEntity<Entrega> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/atribuir-entregador")
    @Operation(summary = "Atribuir entregador a uma entrega",
            description = "Associa um entregador a uma entrega, " +
                                 "muda status para EM_ROTA e calcula o tempo estimado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrega atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou entregador sem coordenadas"),
            @ApiResponse(responseCode = "404", description = "Entrega ou entregador não encontrado")
    })
    public ResponseEntity<?> atribuirEntregador(@PathVariable Long id, @RequestBody Entregador entregador) {

        System.out.println("🔹 ID da entrega: " + id);
        System.out.println("🔹 Entregador recebido: " + entregador);
        try {
            Entrega atualizada = service.atribuirEntregador(id, entregador);
            return ResponseEntity.ok(atualizada);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }

    }

    @PutMapping("/{id}/remover-entregador")
    @Operation(summary = "Remover entregador a uma entrega",
            description = "Remove um entregador a uma entrega, " +
                    "muda status de EM_ROTA para ENTREGUE OU PENDENTE(depende da situação)")
    public ResponseEntity<Entrega> removerEntregador(@PathVariable Long id) {
        try {
            Entrega atualizada = service.removerEntregador(id);
            return ResponseEntity.ok(atualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma entrega a partir do ID",
            description = "Atualiza uma  entrega existente no  sistema")
    @ApiResponse(responseCode = "200", description = "Entrega atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Entrega não encontrado com o ID fornecido")
    public ResponseEntity<Entrega> atualizar(@PathVariable Long id, @RequestBody Entrega entregaAtualizada){
        return repository.findById(id)
                .map(entrega -> {
                    entrega.setEndereco(entregaAtualizada.getEndereco());
                    entrega.setCliente(entregaAtualizada.getCliente());
                    entrega.setLatitude(entregaAtualizada.getLatitude());
                    entrega.setLongitude(entregaAtualizada.getLongitude());
                    entrega.setStatus(entregaAtualizada.getStatus());
                    return ResponseEntity.ok(repository.save(entrega));
                })

                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma entrega a partir do ID",
            description = "Remove uma  entrega existente no  sistema")
    @ApiResponse(responseCode = "200", description = "Entrega deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Entrega não encontrado com o ID fornecido")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}


