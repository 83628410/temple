package dev.cj.temple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@SpringBootApplication
public class TempleSystemApplication {
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    @Value("${server.servlet.context-path:/}")
    private String contextPath;
    
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TempleSystemApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void printKnife4jUrl() {
        String knife4jUrl = "http://localhost:" + serverPort + contextPath + "/doc.html";
        log.info("=================================================");
        log.info("应用启动成功!");
        log.info("Knife4j API 文档地址：{}", knife4jUrl);
        log.info("=================================================");
    }

}
