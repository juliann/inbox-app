package com.nadarzy.inboxapp;

import com.nadarzy.inboxapp.connection.DatastaxAstraProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@SpringBootApplication
@RestController
@EnableConfigurationProperties(DatastaxAstraProperties.class)
public class InboxAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(InboxAppApplication.class, args);
    }

//    @RequestMapping("/user")
//    public String user(@AuthenticationPrincipal OAuth2User principal) {
////        System.out.println(principal);
//        return principal.getAttribute("name");
//    }

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(
            DatastaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }
}
