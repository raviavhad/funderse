package com.funderse.api.service;

import com.funderse.api.domain.FunderseWallet;
import com.funderse.api.repository.FunderseWalletRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FunderseWallet}.
 */
@Service
@Transactional
public class FunderseWalletService {

    private final Logger log = LoggerFactory.getLogger(FunderseWalletService.class);

    private final FunderseWalletRepository funderseWalletRepository;

    public FunderseWalletService(FunderseWalletRepository funderseWalletRepository) {
        this.funderseWalletRepository = funderseWalletRepository;
    }

    /**
     * Save a funderseWallet.
     *
     * @param funderseWallet the entity to save.
     * @return the persisted entity.
     */
    public FunderseWallet save(FunderseWallet funderseWallet) {
        log.debug("Request to save FunderseWallet : {}", funderseWallet);
        return funderseWalletRepository.save(funderseWallet);
    }

    /**
     * Update a funderseWallet.
     *
     * @param funderseWallet the entity to save.
     * @return the persisted entity.
     */
    public FunderseWallet update(FunderseWallet funderseWallet) {
        log.debug("Request to save FunderseWallet : {}", funderseWallet);
        return funderseWalletRepository.save(funderseWallet);
    }

    /**
     * Partially update a funderseWallet.
     *
     * @param funderseWallet the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FunderseWallet> partialUpdate(FunderseWallet funderseWallet) {
        log.debug("Request to partially update FunderseWallet : {}", funderseWallet);

        return funderseWalletRepository
            .findById(funderseWallet.getId())
            .map(existingFunderseWallet -> {
                if (funderseWallet.getLimit() != null) {
                    existingFunderseWallet.setLimit(funderseWallet.getLimit());
                }
                if (funderseWallet.getBalance() != null) {
                    existingFunderseWallet.setBalance(funderseWallet.getBalance());
                }
                if (funderseWallet.getSpent() != null) {
                    existingFunderseWallet.setSpent(funderseWallet.getSpent());
                }
                if (funderseWallet.getDescription() != null) {
                    existingFunderseWallet.setDescription(funderseWallet.getDescription());
                }
                if (funderseWallet.getSortOrder() != null) {
                    existingFunderseWallet.setSortOrder(funderseWallet.getSortOrder());
                }
                if (funderseWallet.getDateAdded() != null) {
                    existingFunderseWallet.setDateAdded(funderseWallet.getDateAdded());
                }
                if (funderseWallet.getDateModified() != null) {
                    existingFunderseWallet.setDateModified(funderseWallet.getDateModified());
                }
                if (funderseWallet.getStatus() != null) {
                    existingFunderseWallet.setStatus(funderseWallet.getStatus());
                }

                return existingFunderseWallet;
            })
            .map(funderseWalletRepository::save);
    }

    /**
     * Get all the funderseWallets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FunderseWallet> findAll(Pageable pageable) {
        log.debug("Request to get all FunderseWallets");
        return funderseWalletRepository.findAll(pageable);
    }

    /**
     * Get one funderseWallet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FunderseWallet> findOne(Long id) {
        log.debug("Request to get FunderseWallet : {}", id);
        return funderseWalletRepository.findById(id);
    }

    /**
     * Delete the funderseWallet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FunderseWallet : {}", id);
        funderseWalletRepository.deleteById(id);
    }
}
