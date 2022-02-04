package com.nadarzy.inboxapp.emailList;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

import static org.springframework.data.cassandra.core.mapping.CassandraType.Name;

/** @author Julian Nadarzy on 03/02/2022 */
@Table(value = "messages_by_user_folder")
public class EmailListItem {

  @PrimaryKey private EmailListItemPKey id;

  @CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
  private List<String> to;

  @CassandraType(type = Name.TEXT)
  private String subject;

  @CassandraType(type = Name.BOOLEAN)
  private boolean isUnread;

  @Transient private String timeSent;

  public String getTimeSent() {
    return timeSent;
  }

  public void setTimeSent(String timeSent) {
    this.timeSent = timeSent;
  }

  public EmailListItemPKey getId() {
    return id;
  }

  public void setId(EmailListItemPKey id) {
    this.id = id;
  }

  public List<String> getTo() {
    return to;
  }

  public void setTo(List<String> to) {
    this.to = to;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public boolean isUnread() {
    return isUnread;
  }

  public void setUnread(boolean unread) {
    isUnread = unread;
  }
}
