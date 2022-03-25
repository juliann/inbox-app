package com.nadarzy.inboxapp.email;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.nadarzy.inboxapp.emailList.EmailListItem;
import com.nadarzy.inboxapp.emailList.EmailListItemPKey;
import com.nadarzy.inboxapp.emailList.EmailListItemRepository;
import com.nadarzy.inboxapp.folders.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Julian Nadarzy on 28/02/2022
 */
@Service
public class EmailService {
  @Autowired private EmailRepository emailRepository;
  @Autowired private EmailListItemRepository emailListItemRepository;
  @Autowired private UnreadEmailStatsRepository unreadEmailStatsRepository;

  public void sendEmail(String from, List<String> to, String subject, String body) {
    Email email = new Email();
    email.setSubject(subject);
    email.setBody(body);
    email.setTo(to);
    email.setFrom(from);
    email.setId(Uuids.timeBased());
    emailRepository.save(email);
    System.out.println("this is the sender of the email: " + from);
    to.forEach(
        toId -> {
          EmailListItem item = createEmailListItem(to, email, toId, "Inbox", from);
          item.setRead(false);
          emailListItemRepository.save(item);
          unreadEmailStatsRepository.incrementUnreadCount(toId, "Inbox");
        });
    EmailListItem sentItemsEntry = createEmailListItem(to, email, from, "Sent", from);
    sentItemsEntry.setRead(true);
    emailListItemRepository.save(sentItemsEntry);
  }

  private EmailListItem createEmailListItem(
      List<String> to, Email email, String toId, String folder, String from) {
    EmailListItemPKey key = new EmailListItemPKey();
    key.setTimeId(email.getId());
    key.setUserId(toId);
    key.setLabel(folder);
    EmailListItem item = new EmailListItem();
    item.setId(key);
    item.setFrom(from);
    item.setSubject(email.getSubject());
    item.setTo(to);

    return item;
  }
}
