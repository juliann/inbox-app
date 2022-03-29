package com.nadarzy.inboxapp.controllers;

import com.nadarzy.inboxapp.email.Email;
import com.nadarzy.inboxapp.email.EmailRepository;
import com.nadarzy.inboxapp.email.EmailService;
import com.nadarzy.inboxapp.emailList.EmailListItem;
import com.nadarzy.inboxapp.emailList.EmailListItemPKey;
import com.nadarzy.inboxapp.emailList.EmailListItemRepository;
import com.nadarzy.inboxapp.folders.Folder;
import com.nadarzy.inboxapp.folders.FolderRepository;
import com.nadarzy.inboxapp.folders.FolderService;
import com.nadarzy.inboxapp.folders.UnreadEmailStatsRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Julian Nadarzy on 05/02/2022
 */
@Controller
public class EmailViewController {

  private final FolderRepository folderRepository;
  private final FolderService folderService;
  private final EmailListItemRepository emailListItemRepository;
  private final EmailRepository emailRepository;
  private final UnreadEmailStatsRepository unreadEmailStatsRepository;
  private final EmailService emailService;

  public EmailViewController(
      FolderRepository folderRepository,
      FolderService folderService,
      EmailListItemRepository emailListItemRepository,
      EmailRepository emailRepository,
      UnreadEmailStatsRepository unreadEmailStatsRepository,
      EmailService emailService) {
    this.folderRepository = folderRepository;
    this.folderService = folderService;
    this.emailListItemRepository = emailListItemRepository;
    this.emailRepository = emailRepository;
    this.unreadEmailStatsRepository = unreadEmailStatsRepository;
    this.emailService = emailService;
  }

  @GetMapping("/email/{id}")
  public String emailView(
      @AuthenticationPrincipal OAuth2User principal,
      Model model,
      @PathVariable UUID id,
      @RequestParam String folder) {
    if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
      return "index";
    } else {
      String userId = principal.getAttribute("name");
      model.addAttribute("userName", userId);
      // fetch folders
      List<Folder> userFolders = folderRepository.findAllById(userId);
      model.addAttribute("userFolders", userFolders);
      List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
      model.addAttribute("defaultFolders", defaultFolders);

      try {

        Optional<Email> optionalEmail = emailRepository.findById(id);
        if (!optionalEmail.isPresent()) {
          return "inbox-page";
        }
        Email email = optionalEmail.get();
        model.addAttribute("email", email);
        String toIds = String.join(", ", email.getTo());
        model.addAttribute("toIds", toIds);

        if (!emailService.doesHaveAccess(email, userId)) {
          return "redirect:/";
        }

        EmailListItemPKey emailListItemPKey = new EmailListItemPKey();
        emailListItemPKey.setUserId(userId);
        emailListItemPKey.setLabel(folder);
        emailListItemPKey.setTimeId(email.getId());
        Optional<EmailListItem> optionalEmailListItem =
            emailListItemRepository.findById(emailListItemPKey);
        if (!optionalEmailListItem.isPresent()) {
          throw new IllegalArgumentException();
        }

        EmailListItem emailListItem = optionalEmailListItem.get();
        System.out.println(
            "#################" + emailListItem.getId() + " " + emailListItem.isRead());
        if (!emailListItem.isRead()) {
          unreadEmailStatsRepository.decrementUnreadCount(userId, folder);
          System.out.println(
              "########### in !emailListItem.isRead()" + userId + " folder: " + folder);
        }
        emailListItem.setRead(true);
        emailListItemRepository.save(emailListItem);
        model.addAttribute("folderToUnreadCounts", folderService.getMapFolderUnreadCounts(userId));

        return "email-page";
      } catch (IllegalArgumentException e) {
        return "inbox-page";
      }
    }
  }
}
