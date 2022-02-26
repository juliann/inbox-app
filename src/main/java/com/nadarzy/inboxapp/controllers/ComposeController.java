package com.nadarzy.inboxapp.controllers;

import com.nadarzy.inboxapp.folders.Folder;
import com.nadarzy.inboxapp.folders.FolderRepository;
import com.nadarzy.inboxapp.folders.FolderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

/** @author Julian Nadarzy on 26/02/2022 */
@Controller
public class ComposeController {

  private final FolderRepository folderRepository;
  private final FolderService folderService;

  public ComposeController(FolderRepository folderRepository, FolderService folderService) {
    this.folderRepository = folderRepository;
    this.folderService = folderService;
  }

  @GetMapping("/compose")
  public String getComposePage(
      @AuthenticationPrincipal OAuth2User principal,
      Model model,
      @RequestParam(required = false) String to) {
    if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
      return "index";
    } else {
      String userId = principal.getAttribute("name");

      // fetch folders
      List<Folder> userFolders = folderRepository.findAllById(userId);
      model.addAttribute("userFolders", userFolders);
      List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
      model.addAttribute("defaultFolders", defaultFolders);

      if (StringUtils.hasText(to)) {
        String[] splitIds = to.split(",");
        List<String> uniqueIds =
            Arrays.stream(splitIds)
                .map(StringUtils::trimWhitespace)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();
        model.addAttribute("toIds", String.join(", ", uniqueIds));
      }

      return "compose-page";
    }
  }
}
