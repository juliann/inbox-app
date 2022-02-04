package com.nadarzy.inboxapp.controllers;

import com.nadarzy.inboxapp.emailList.EmailListItem;
import com.nadarzy.inboxapp.emailList.EmailListItemRepository;
import com.nadarzy.inboxapp.folders.Folder;
import com.nadarzy.inboxapp.folders.FolderRepository;
import com.nadarzy.inboxapp.folders.FolderService;
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
  private final FolderService folderService;
  private final EmailListItemRepository emailListItemRepository;

  public InboxController(
      FolderRepository folderRepository,
      FolderService folderService,
      EmailListItemRepository emailListItemRepository) {
    this.folderRepository = folderRepository;
    this.folderService = folderService;
    this.emailListItemRepository = emailListItemRepository;
  }

  @GetMapping("/")
  public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
    if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
      return "index";
    } else {
      String userName = principal.getAttribute("name");

      // fetch folders
      List<Folder> userFolders = folderRepository.findAllById(userName);
      model.addAttribute("userFolders", userFolders);
      List<Folder> defaultFolders = folderService.fetchDefaultFolders(userName);
      model.addAttribute("defaultFolders", defaultFolders);

      // fetch messages

      String folderLabel = "Inbox";
      List<EmailListItem> emailList =
          emailListItemRepository.findAllById_UserIdAndId_Label(userName, folderLabel);

      model.addAttribute("emailList", emailList);
      return "inbox-page";
    }
  }
}
