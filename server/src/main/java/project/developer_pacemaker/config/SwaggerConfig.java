package project.developer_pacemaker.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    // http://localhost:8080/swagger-ui/index.html
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
            .version("v1.0") //버전
            .title("Developer-Pacemaker API") //이름
            .description("개발자 학습도우미 API 입니다."); //설명
        return new OpenAPI()
            .info(info);
    }
}
