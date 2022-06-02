package com.funderse.api.web.rest;

import com.funderse.api.domain.FunderseWallet;
import com.funderse.api.repository.FunderseWalletRepository;
import com.funderse.api.service.FunderseWalletService;
import com.funderse.api.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.funderse.api.domain.FunderseWallet}.
 */
@RestController
@RequestMapping("/api")
public class FunderseWalletResource {

    private final Logger log = LoggerFactory.getLogger(FunderseWalletResource.class);

    private static final String ENTITY_NAME = "funderseWallet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FunderseWalletService funderseWalletService;

    private final FunderseWalletRepository funderseWalletRepository;

    public FunderseWalletResource(FunderseWalletService funderseWalletService, FunderseWalletRepository funderseWalletRepository) {
        this.funderseWalletService = funderseWalletService;
        this.funderseWalletRepository = funderseWalletRepository;
    }

    /**
     * {@code POST  /funderse-wallets} : Create a new funderseWallet.
     *
     * @param funderseWallet the funderseWallet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new funderseWallet, or with status {@code 400 (Bad Request)} if the funderseWallet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/funderse-wallets")
    public ResponseEntity<FunderseWallet> createFunderseWallet(@Valid @RequestBody FunderseWallet funderseWallet)
        throws URISyntaxException {
        log.debug("REST request to save FunderseWallet : {}", funderseWallet);
        if (funderseWallet.getId() != null) {
            throw new BadRequestAlertException("A new funderseWallet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FunderseWallet result = funderseWalletService.save(funderseWallet);
        return ResponseEntity
            .created(new URI("/api/funderse-wallets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /funderse-wallets/:id} : Updates an existing funderseWallet.
     *
     * @param id the id of the funderseWallet to save.
     * @param funderseWallet the funderseWallet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funderseWallet,
     * or with status {@code 400 (Bad Request)} if the funderseWallet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the funderseWallet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/funderse-wallets/{id}")
    public ResponseEntity<FunderseWallet> updateFunderseWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FunderseWallet funderseWallet
    ) throws URISyntaxException {
        log.debug("REST request to update FunderseWallet : {}, {}", id, funderseWallet);
        if (funderseWallet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funderseWallet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funderseWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FunderseWallet result = funderseWalletService.update(funderseWallet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, funderseWallet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /funderse-wallets/:id} : Partial updates given fields of an existing funderseWallet, field will ignore if it is null
     *
     * @param id the id of the funderseWallet to save.
     * @param funderseWallet the funderseWallet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funderseWallet,
     * or with status {@code 400 (Bad Request)} if the funderseWallet is not valid,
     * or with status {@code 404 (Not Found)} if the funderseWallet is not found,
     * or with status {@code 500 (Internal Server Error)} if the funderseWallet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/funderse-wallets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FunderseWallet> partialUpdateFunderseWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FunderseWallet funderseWallet
    ) throws URISyntaxException {
        log.debug("REST request to partial update FunderseWallet partially : {}, {}", id, funderseWallet);
        if (funderseWallet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funderseWallet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funderseWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FunderseWallet> result = funderseWalletService.partialUpdate(funderseWallet);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, funderseWallet.getId().toString())
        );
    }

    /**
     * {@code GET  /funderse-wallets} : get all the funderseWallets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of funderseWallets in body.
     */
    @GetMapping("/funderse-wallets")
    public ResponseEntity<List<FunderseWallet>> getAllFunderseWallets(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FunderseWallets");
        Page<FunderseWallet> page = funderseWalletService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /funderse-wallets/:id} : get the "id" funderseWallet.
     *
     * @param id the id of the funderseWallet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the funderseWallet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/funderse-wallets/{id}")
    public ResponseEntity<FunderseWallet> getFunderseWallet(@PathVariable Long id) {
        log.debug("REST request to get FunderseWallet : {}", id);
        Optional<FunderseWallet> funderseWallet = funderseWalletService.findOne(id);
        return ResponseUtil.wrapOrNotFound(funderseWallet);
    }

    /**
     * {@code DELETE  /funderse-wallets/:id} : delete the "id" funderseWallet.
     *
     * @param id the id of the funderseWallet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/funderse-wallets/{id}")
    public ResponseEntity<Void> deleteFunderseWallet(@PathVariable Long id) {
        log.debug("REST request to delete FunderseWallet : {}", id);
        funderseWalletService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
