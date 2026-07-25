package rotas_entregas.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


import java.math.BigDecimal;



@Entity
@Table(name = "entregas")
public class Entrega {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Endereço obrigatório")
    private String endereco;

    @NotBlank(message = "Cliente é obrigatório")
    private String cliente;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private Long tempoEstimado;


    @NotBlank(message = "Status é obrigatório")
    @Pattern(regexp = "PENDENTE|EM_ROTA|ENTREGUE",
            message = "Status deve ser PENDENTE, EM_ROTA ou ENTREGUE ")
    private String status;



    public Entrega() {}


    public long getId() {return id;}
    public void setId(long id){this.id = id;}

    public String getEndereco() {return endereco;}
    public void setEndereco(String endereco)    {this.endereco = endereco;}

    public String getCliente()  {return cliente;}
    public void setCliente(String cliente) {this.cliente = cliente;}

    public BigDecimal getLatitude() {return latitude;}
    public void setLatitude(BigDecimal latitude) {this.latitude = latitude;}

    public BigDecimal getLongitude() {return longitude;}
    public void setLongitude(BigDecimal longitude) {this.longitude = longitude;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public Long getTempoEstimado() {return tempoEstimado;}
    public void setTempoEstimado(Long tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    @ManyToOne
    @JoinColumn(name = "entregador_id")
    private Entregador entregador;

    public Entregador getEntregador() { return entregador;}
    public void setEntregador(Entregador entregador) { this.entregador = entregador;}

}
