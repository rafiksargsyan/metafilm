package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.core.app.dto.UserDTO;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Account;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Principal;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.UserProfile;
import com.rsargsyan.metafilm.main_ctx.core.exception.ResourceNotFoundException;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.AccountRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.PrincipalRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
  private final UserProfileRepository userProfileRepository;
  private final AccountRepository accountRepository;
  private final PrincipalRepository principalRepository;

  @Autowired
  public UserService(UserProfileRepository userProfileRepository,
                     AccountRepository accountRepository,
                     PrincipalRepository principalRepository) {
    this.userProfileRepository = userProfileRepository;
    this.accountRepository = accountRepository;
    this.principalRepository = principalRepository;
  }

  @Transactional
  public UserDTO signUpWithExternal(String externalId, String name) {
    List<Principal> principalList = principalRepository.findByExternalId(externalId);
    if (!principalList.isEmpty()) {
      var existing = userProfileRepository.findByPrincipalId(principalList.get(0).getId());
      if (existing.isEmpty()) throw new ResourceNotFoundException();
      return UserDTO.from(existing.get(0));
    }
    if (name == null || name.isBlank()) name = "Your full name here";
    Principal principal = new Principal(externalId, name);
    Account account = new Account();
    UserProfile userProfile = new UserProfile(account, principal, name);
    principalRepository.save(principal);
    accountRepository.save(account);
    userProfileRepository.save(userProfile);
    return UserDTO.from(userProfile);
  }
}
