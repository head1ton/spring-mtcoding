package ai.springmtcoding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringMtcodingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMtcodingApplication.class, args);
//        ConfigurableApplicationContext context = SpringApplication.run(SpringMtcodingApplication.class, args);
//        String[] iocNames = context.getBeanDefinitionNames();
//        for (String iocName : iocNames) {
//            System.out.println(iocName);
//        }
    }

}
