package com.nadarzy.inboxapp.controllers;

import com.nadarzy.inboxapp.folders.Folder;
import com.nadarzy.inboxapp.folders.FolderRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/** @author Julian Nadarzy on 10/01/2022 */
@Controller
public class InboxController {
  private final FolderRepository folderRepository;

  public InboxController(FolderRepository folderRepository) {
    this.folderRepository = folderRepository;
  }

  @GetMapping("/")
  public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
    if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
      return "index";
    } else {
      String userName = principal.getAttribute("name");
      List<Folder> userFolders = folderRepository.findAllById(userName);
      model.addAttribute("userFolders", userFolders);

      return "inbox-page";
    }
  }
}
