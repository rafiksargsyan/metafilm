package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers.UserContext;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.ApiKey;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Principal;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.UserProfile;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.ApiKeyRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.PrincipalRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.UserProfileRepository;
import com.rsargsyan.metafilm.main_ctx.core.Util;
import io.hypersistence.tsid.TSID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
  private final UserProfileRepository userProfileRepository;
  private final PrincipalRepository principalRepository;
  private final ApiKeyRepository apiKeyRepository;

  @Autowired
  public AuthService(UserProfileRepository userProfileRepository,
                     PrincipalRepository principalRepository,
                     ApiKeyRepository apiKeyRepository) {
    this.userProfileRepository = userProfileRepository;
    this.principalRepository = principalRepository;
    this.apiKeyRepository = apiKeyRepository;
  }

  @Transactional(readOnly = true)
  public String getUserProfileId(String externalId, String accountId) {
    List<Principal> principals = principalRepository.findByExternalId(externalId);
    if (principals.isEmpty()) return null;
    var principal = principals.get(0);
    try {
      Long accountIdLong = Util.validateTSID(accountId);
      List<UserProfile> userProfiles = userProfileRepository.findByPrincipalIdAndAccountId(
          principal.getId(), accountIdLong);
      if (userProfiles.isEmpty()) return null;
      return userProfiles.get(0).getStrId();
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  public UserContext getUserContextByApiKey(String apiKeyId) {
    if (!TSID.isValid(apiKeyId)) return null;
    Optional<ApiKey> apiKeyOpt = apiKeyRepository.findById(TSID.from(apiKeyId).toLong());
    if (apiKeyOpt.isEmpty()) return null;
    ApiKey apiKey = apiKeyOpt.get();
    UserProfile userProfile = apiKey.getUserProfile();
    apiKey.accessed();
    apiKeyRepository.save(apiKey);
    return UserContext.builder()
        .userProfileId(userProfile.getStrId())
        .accountId(userProfile.getAccount().getStrId())
        .externalId(userProfile.getPrincipal().getExternalId())
        .build();
  }

  @Transactional(readOnly = true)
  public boolean validateApiKey(String apiKeyId, String apiKey) {
    if (!TSID.isValid(apiKeyId)) return false;
    Optional<ApiKey> apiKeyFromDB = apiKeyRepository.findById(TSID.from(apiKeyId).toLong());
    return apiKeyFromDB.isPresent() && !apiKeyFromDB.get().isDisabled() && apiKeyFromDB.get().check(apiKey);
  }
}
