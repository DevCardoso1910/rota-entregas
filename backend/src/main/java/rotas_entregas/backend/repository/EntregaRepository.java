package rotas_entregas.backend.repository;

import rotas_entregas.backend.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rotas_entregas.backend.model.Entregador;

import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long>{
    List<Entrega> findByEntregador(Entregador entregador);
}
