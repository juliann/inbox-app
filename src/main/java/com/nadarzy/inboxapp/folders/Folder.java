package com.nadarzy.inboxapp.folders;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import static org.springframework.data.cassandra.core.mapping.CassandraType.Name;

/** @author Julian Nadarzy on 10/01/2022 */
@Table(value = "folders_by_user")
public class Folder {
  @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
  private String userId;

  @PrimaryKeyColumn(name = "label", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
  private String label;

  @CassandraType(type = Name.TEXT)
  private String color;
}
