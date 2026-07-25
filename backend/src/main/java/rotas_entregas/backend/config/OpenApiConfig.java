package rotas_entregas.backend.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Otimização de Rotas de Entrega")
                        .version("1.0")
                        .description("Sistema para gerenciar entregas," +
                                " entregadores e calcular tempo estimado de rota usando TomTom.")
                        .contact(new Contact()
                                .name("Matheus Cardoso")
                                .email("theuscardosodacosta@gmail.com")
                                .url("https://github.com/DevCardoso1910"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}
