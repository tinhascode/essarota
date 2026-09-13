package io.github.tinhascode.essarota.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Essarota API - Sistema de Alertas de Transporte Público")
                        .description("""
                                API REST do projeto **Essarota**.
                                
                                ### Funcionalidades disponíveis:
                                - **Autenticação**: Login com geração de Token JWT.
                                - **Usuários**: CRUD completo de usuários com senhas criptografadas (BCrypt).
                                
                                ### Instruções de Autenticação no Swagger:
                                1. Crie um usuário no endpoint `POST /api/v1/usuarios` (rota pública).
                                2. Faça login no endpoint `POST /api/v1/auth/login` (rota pública).
                                3. Copie o token retornado.
                                4. Clique no botão **Authorize** (cadeado verde no topo) e informe o token (não precisa digitar `Bearer `, o Swagger adiciona automaticamente).
                                5. Agora todas as rotas protegidas poderão ser testadas diretamente por aqui.
                                """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("TinhasCode")
                                .url("https://github.com/tinhascode/essarota"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Insira o token JWT gerado no endpoint de login para acessar as rotas protegidas.")));
    }
}
