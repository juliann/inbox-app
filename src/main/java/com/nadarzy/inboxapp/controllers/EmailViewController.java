package com.nadarzy.inboxapp.controllers;

import com.nadarzy.inboxapp.email.Email;
import com.nadarzy.inboxapp.email.EmailRepository;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** @author Julian Nadarzy on 05/02/2022 */
@Controller
public class EmailViewController {

  private final FolderRepository folderRepository;
  private final FolderService folderService;
  private final EmailListItemRepository emailListItemRepository;
  private final EmailRepository emailRepository;

  public EmailViewController(
      FolderRepository folderRepository,
      FolderService folderService,
      EmailListItemRepository emailListItemRepository,
      EmailRepository emailRepository) {
    this.folderRepository = folderRepository;
    this.folderService = folderService;
    this.emailListItemRepository = emailListItemRepository;
    this.emailRepository = emailRepository;
  }

  @GetMapping("/emails/{id}")
  public String emailView(
      @AuthenticationPrincipal OAuth2User principal, Model model, @PathVariable UUID id) {
    if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
      return "index";
    } else {
      String userId = principal.getAttribute("name");

      // fetch folders
      List<Folder> userFolders = folderRepository.findAllById(userId);
      model.addAttribute("userFolders", userFolders);
      List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
      model.addAttribute("defaultFolders", defaultFolders);

      Optional<Email> optionalEmail = emailRepository.findById(id);
      if (!optionalEmail.isPresent()) {
        return "inbox-page";
      }
      model.addAttribute("email", optionalEmail.get());

      return "email-page";
    }
  }
}
