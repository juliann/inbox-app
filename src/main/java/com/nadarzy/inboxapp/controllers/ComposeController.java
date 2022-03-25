package com.nadarzy.inboxapp.controllers;

import com.nadarzy.inboxapp.email.EmailService;
import com.nadarzy.inboxapp.folders.Folder;
import com.nadarzy.inboxapp.folders.FolderRepository;
import com.nadarzy.inboxapp.folders.FolderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Julian Nadarzy on 26/02/2022
 */
@Controller
public class ComposeController {

  private final FolderRepository folderRepository;
  private final FolderService folderService;
  private final EmailService emailService;

  public ComposeController(
      FolderRepository folderRepository, FolderService folderService, EmailService emailService) {
    this.folderRepository = folderRepository;
    this.folderService = folderService;
    this.emailService = emailService;
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
      model.addAttribute("folderToUnreadCounts", folderService.getMapFolderUnreadCounts(userId));
      List<String> uniqueIds = splitIds(to);
      model.addAttribute("toIds", String.join(", ", uniqueIds));

      return "compose-page";
    }
  }

  private List<String> splitIds(String to) {
    if (!StringUtils.hasText(to)) {
      return new ArrayList<String>();
    }

    String[] splitIds = to.split(",");
    List<String> uniqueIds =
        Arrays.stream(splitIds)
            .map(StringUtils::trimWhitespace)
            .filter(StringUtils::hasText)
            .distinct()
            .toList();
    return uniqueIds;
  }

  @PostMapping("/sendEmail")
  public ModelAndView sendEmail(
      @RequestBody MultiValueMap<String, String> formData,
      @AuthenticationPrincipal OAuth2User principal) {

    if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
      return new ModelAndView("redirect:/");
    } else {

      //      String from = principal.getAttribute("login");
      String from = principal.getAttribute("name");
      List<String> toIds = splitIds(formData.getFirst("toIds"));
      String subject = formData.getFirst("subject");
      String body = formData.getFirst("body");
      System.out.println("############## body = " + body);
      emailService.sendEmail(from, toIds, subject, body);
      return new ModelAndView("redirect:/");
    }
  }
}
