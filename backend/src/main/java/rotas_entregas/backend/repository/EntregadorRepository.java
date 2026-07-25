package rotas_entregas.backend.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rotas_entregas.backend.model.Entrega;
import rotas_entregas.backend.model.Entregador;

import java.util.List;

@Repository
public interface EntregadorRepository extends JpaRepository<Entregador, Long> {


}
