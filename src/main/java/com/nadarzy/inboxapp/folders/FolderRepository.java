package com.nadarzy.inboxapp.folders;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

/** @author Julian Nadarzy on 10/01/2022 */
@Repository
public interface FolderRepository extends CassandraRepository<Folder, String> {}
