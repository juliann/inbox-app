package com.nadarzy.inboxapp.email;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/** @author Julian Nadarzy on 05/02/2022 */
@Repository
public interface EmailRepository extends CassandraRepository<Email, UUID> {}
