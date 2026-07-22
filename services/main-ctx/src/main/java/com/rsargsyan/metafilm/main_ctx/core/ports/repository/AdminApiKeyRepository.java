package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.AdminApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminApiKeyRepository extends JpaRepository<AdminApiKey, Long> {
}
