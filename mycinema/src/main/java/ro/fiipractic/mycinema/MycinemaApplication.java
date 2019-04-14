package ro.fiipractic.mycinema;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class MycinemaApplication {

    static Logger log = Logger.getLogger("D://myCinema_log4j.log");
    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(MycinemaApplication.class, args);
    }
}
