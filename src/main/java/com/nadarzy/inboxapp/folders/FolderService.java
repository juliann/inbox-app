package com.nadarzy.inboxapp.folders;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/** @author Julian Nadarzy on 14/01/2022 */
@Service
public class FolderService {
  public List<Folder> fetchDefaultFolders(String userId) {
    return Arrays.asList(
        new Folder(userId, "Inbox", "white"),
        new Folder(userId, "Sent Items", "green"),
        new Folder(userId, "Important", "red"));
  }
}
