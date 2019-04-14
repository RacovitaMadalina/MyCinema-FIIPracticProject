package ro.fiipractic.mycinema;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.logging.Logger;

@SpringBootApplication
@EnableScheduling
public class MycinemaApplication {

    static Logger log = Logger.getLogger("D://myCinema_log4j.log");
    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(MycinemaApplication.class, args);
    }
}
