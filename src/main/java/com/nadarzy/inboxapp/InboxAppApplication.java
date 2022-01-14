package com.nadarzy.inboxapp;

import com.nadarzy.inboxapp.connection.DatastaxAstraProperties;
import com.nadarzy.inboxapp.folders.Folder;
import com.nadarzy.inboxapp.folders.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.nio.file.Path;

@SpringBootApplication
@EnableConfigurationProperties(DatastaxAstraProperties.class)
public class InboxAppApplication {
  @Autowired FolderRepository folderRepository;

  public static void main(String[] args) {
    ConfigurableApplicationContext applicationContext =
        SpringApplication.run(InboxAppApplication.class, args);
    //    System.out.println("########### hi");
    //    folderRepository = applicationContext.getBean(FolderRepository.class);
    //    System.out.println("####################" + folderRepository);
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

  @PostConstruct
  public void initData() {

    //
    folderRepository.save(new Folder("JulianN", "Inbox", "blue"));
    folderRepository.save(new Folder("JulianN", "Sent", "green"));
    folderRepository.save(new Folder("JulianN", "Important", "yellow"));
    folderRepository.findAll().forEach(System.out::println);
  }
}
