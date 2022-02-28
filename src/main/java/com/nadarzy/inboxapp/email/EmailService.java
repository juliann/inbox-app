package com.nadarzy.inboxapp.email;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.nadarzy.inboxapp.emailList.EmailListItem;
import com.nadarzy.inboxapp.emailList.EmailListItemPKey;
import com.nadarzy.inboxapp.emailList.EmailListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** @author Julian Nadarzy on 28/02/2022 */
@Service
public class EmailService {
  @Autowired private EmailRepository emailRepository;
  @Autowired private EmailListItemRepository emailListItemRepository;

  public void sendEmail(String from, List<String> to, String subject, String body) {
    Email email = new Email();
    email.setSubject(subject);
    email.setBody(body);
    email.setTo(to);
    email.setFrom(from);
    email.setId(Uuids.timeBased());
    emailRepository.save(email);

    to.forEach(
        toId -> {
          EmailListItem item = createEmailListItem(to, email, toId, "Inbox");
          emailListItemRepository.save(item);
        });
    createEmailListItem(to, email, from, "Sent");
  }

  private EmailListItem createEmailListItem(
      List<String> to, Email email, String toId, String folder) {
    EmailListItemPKey key = new EmailListItemPKey();
    key.setTimeId(email.getId());
    key.setUserId(toId);
    key.setLabel(folder);
    EmailListItem item = new EmailListItem();
    item.setId(key);
    item.setSubject(email.getSubject());
    item.setTo(to);
    item.setUnread(true);
    return item;
  }
}
