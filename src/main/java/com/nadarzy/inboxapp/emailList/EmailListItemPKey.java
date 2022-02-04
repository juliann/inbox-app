package com.nadarzy.inboxapp.emailList;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

/** @author Julian Nadarzy on 03/02/2022 */
@PrimaryKeyClass
public class EmailListItemPKey {
  @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
  private String userId;

  @PrimaryKeyColumn(name = "label", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
  private String label;

  @PrimaryKeyColumn(name = "crated_time_uuid", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
  private UUID timeId;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public UUID getTimeId() {
    return timeId;
  }

  public void setTimeId(UUID timeId) {
    this.timeId = timeId;
  }
}
