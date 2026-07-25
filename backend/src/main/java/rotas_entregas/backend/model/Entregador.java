package rotas_entregas.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entregadores")
public class Entregador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Não é obrigatório")
    private String nome;
    private String veiculo;
    private String telefone;
    private Boolean disponivel = true;
    private BigDecimal latitude;
    private BigDecimal longitude;

    @OneToMany(mappedBy = "entregador")
    @JsonIgnore
    private List<Entrega> entregas = new ArrayList<>();

    public Entregador(){

    }
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getVeiculo() {return veiculo;}
    public void setVeiculo(String veiculo) {this.veiculo = veiculo;}

    public String getTelefone() {return telefone;}
    public void setTelefone(String telefone) {this.telefone = telefone;}

    public Boolean getDisponivel() {return disponivel;}
    public void setDisponivel(Boolean disponivel) {this.disponivel = disponivel;}

    public List<Entrega> getEntregas() {return entregas;}
    public void setEntregas(List<Entrega> entregas) {this.entregas = entregas;}

    public BigDecimal getLatitude() {return latitude;}
    public void setLatitude(BigDecimal latitude) {this.latitude = latitude;}

    public BigDecimal getLongitude() {return longitude;}
    public void setLongitude(BigDecimal longitude) {this.longitude = longitude;}
}
