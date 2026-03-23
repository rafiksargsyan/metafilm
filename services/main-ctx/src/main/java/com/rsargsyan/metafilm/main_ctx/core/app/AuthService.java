package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers.UserContext;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Principal;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.UserProfile;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.PrincipalRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.UserProfileRepository;
import io.hypersistence.tsid.TSID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {
  private final UserProfileRepository userProfileRepository;
  private final PrincipalRepository principalRepository;

  @Autowired
  public AuthService(UserProfileRepository userProfileRepository,
                     PrincipalRepository principalRepository) {
    this.userProfileRepository = userProfileRepository;
    this.principalRepository = principalRepository;
  }

  @Transactional(readOnly = true)
  public String getUserProfileId(String externalId, String accountId) {
    List<Principal> principals = principalRepository.findByExternalId(externalId);
    if (principals.isEmpty()) return null;
    var principal = principals.get(0);
    if (!TSID.isValid(accountId)) return null;
    List<UserProfile> userProfiles = userProfileRepository.findByPrincipalIdAndAccountId(
        principal.getId(), TSID.from(accountId).toLong());
    if (userProfiles.isEmpty()) return null;
    return userProfiles.get(0).getStrId();
  }
}
