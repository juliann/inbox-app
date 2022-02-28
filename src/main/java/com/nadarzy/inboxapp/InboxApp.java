package com.nadarzy.inboxapp;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.nadarzy.inboxapp.connection.DatastaxAstraProperties;
import com.nadarzy.inboxapp.email.Email;
import com.nadarzy.inboxapp.email.EmailRepository;
import com.nadarzy.inboxapp.emailList.EmailListItem;
import com.nadarzy.inboxapp.emailList.EmailListItemPKey;
import com.nadarzy.inboxapp.emailList.EmailListItemRepository;
import com.nadarzy.inboxapp.folders.Folder;
import com.nadarzy.inboxapp.folders.FolderRepository;
import com.nadarzy.inboxapp.folders.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties(DatastaxAstraProperties.class)
public class InboxApp {
  @Autowired FolderRepository folderRepository;
  @Autowired EmailListItemRepository emailListItemRepository;
  @Autowired EmailRepository emailRepository;
  @Autowired UnreadEmailStatsRepository unreadEmailStatsRepository;

  public static void main(String[] args) {
    ConfigurableApplicationContext applicationContext = SpringApplication.run(InboxApp.class, args);
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
    //    folderRepository.findAll().forEach(System.out::println);

    unreadEmailStatsRepository.incrementUnreadCount("JulianN", "Inbox");

    for (int i = 0; i < 10; i++) {
      EmailListItemPKey key = new EmailListItemPKey();
      key.setUserId("JulianN");
      key.setLabel("Inbox");
      key.setTimeId(Uuids.timeBased());

      EmailListItem item = new EmailListItem();
      item.setId(key);
      item.setTo(Arrays.asList("JulianN", "abc", "def"));
      item.setSubject("Subject" + i);
      item.setUnread(true);

      emailListItemRepository.save(item);

      Email email = new Email();
      email.setId(key.getTimeId());
      email.setFrom("JulianN");
      email.setTo(item.getTo());
      email.setBody("Body" + i);
      email.setSubject(item.getSubject());

      emailRepository.save(email);
    }
  }
}
