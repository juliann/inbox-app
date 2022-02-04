package com.nadarzy.inboxapp.emailList;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** @author Julian Nadarzy on 03/02/2022 */
@Repository
public interface EmailListItemRepository
    extends CassandraRepository<EmailListItem, EmailListItemPKey> {
  //  List<EmailListItem> findAllById_UserIdAndId_Label(String id, String label);
  //  List<EmailListItem> findAllByKey_IdAndKey_Label(String UserId, String label);
  List<EmailListItem> findAllById_UserIdAndId_Label(String userId, String label);
}
