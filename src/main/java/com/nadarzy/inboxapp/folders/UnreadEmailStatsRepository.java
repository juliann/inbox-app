package com.nadarzy.inboxapp.folders;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

/** @author Julian Nadarzy on 28/02/2022 */
@Repository
public interface UnreadEmailStatsRepository extends CassandraRepository<UnreadEmailStats, String> {

  @Query(
      "update unread_email_stats set unreadcount=unreadcount+1 where user_id = ?0 and label =?1;")
  public void incrementUnreadCount(String userId, String label);

  @Query(
      "update unread_email_stats set unreadcount=unreadcount-1 where user_id = ?0 and label =?1;")
  public void decrementUnreadCount(String userId, String label);
}
