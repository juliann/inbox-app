package com.nadarzy.inboxapp.folders;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** @author Julian Nadarzy on 14/01/2022 */
@Service
public class FolderService {

  private final UnreadEmailStatsRepository unreadEmailStatsRepository;

  public FolderService(UnreadEmailStatsRepository unreadEmailStatsRepository) {
    this.unreadEmailStatsRepository = unreadEmailStatsRepository;
  }

  public List<Folder> fetchDefaultFolders(String userId) {
    return Arrays.asList(
        new Folder(userId, "Inbox", "blue"),
        new Folder(userId, "Sent", "purple"),
        new Folder(userId, "Important", "red"));
  }

  public Map<String, Integer> getMapFolderUnreadCounts(String userId) {
    List<UnreadEmailStats> stats = unreadEmailStatsRepository.findAllById(userId);
    Map<String, Integer> labelUnreadCounts =
        stats.stream()
            .collect(
                Collectors.toMap(UnreadEmailStats::getLabel, UnreadEmailStats::getUnreadCount));
    return labelUnreadCounts;
  }
}
