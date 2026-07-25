package rotas_entregas.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rotas_entregas.backend.model.Entrega;
import rotas_entregas.backend.model.Entregador;
import rotas_entregas.backend.repository.EntregaRepository;
import rotas_entregas.backend.repository.EntregadorRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/entregadores")
public class EntregadorController {
    @Autowired
    private EntregadorRepository repository;

    @Autowired
    private EntregaRepository entregaRepository;

    @GetMapping
    public List<Entregador> listar(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entregador> buscarPorId(@PathVariable Long id){
        return  repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Entregador criar(@Valid @RequestBody Entregador entregador){
        return repository.save(entregador);
    }

    @PutMapping("/{id}")
    public Optional<ResponseEntity<Entregador>> atualizar(@PathVariable Long id, @Valid @RequestBody
                                                Entregador entregadorAtualizado){
        return Optional.of(repository.findById(id)
                .map(entregador -> {
                    entregador.setNome(entregadorAtualizado.getNome());
                    entregador.setVeiculo(entregadorAtualizado.getVeiculo());
                    entregador.setTelefone(entregadorAtualizado.getTelefone());
                    entregador.setDisponivel(entregadorAtualizado.getDisponivel());
                    entregador.setLatitude(entregadorAtualizado.getLatitude());
                    entregador.setLongitude(entregadorAtualizado.getLongitude());
                    return ResponseEntity.ok(repository.save(entregador));
                })
                .orElse(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        return (ResponseEntity<String>) repository.findById(id)
                .map(entregador -> {

                    List<Entrega> entregas = entregaRepository.findByEntregador(entregador);

                    if (!entregas.isEmpty()) {
                        return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body("Entregador não pode ser deletado pois possui " + entregas.size() + " entregas associadas.");
                    }

                    repository.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }



    }

