package com.funderse.api.repository;

import com.funderse.api.domain.FunderseWallet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FunderseWallet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FunderseWalletRepository extends JpaRepository<FunderseWallet, Long> {}
