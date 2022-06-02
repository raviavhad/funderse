package com.funderse.api.web.rest;

import com.funderse.api.domain.AccountType;
import com.funderse.api.repository.AccountTypeRepository;
import com.funderse.api.service.AccountTypeService;
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
 * REST controller for managing {@link com.funderse.api.domain.AccountType}.
 */
@RestController
@RequestMapping("/api")
public class AccountTypeResource {

    private final Logger log = LoggerFactory.getLogger(AccountTypeResource.class);

    private static final String ENTITY_NAME = "accountType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountTypeService accountTypeService;

    private final AccountTypeRepository accountTypeRepository;

    public AccountTypeResource(AccountTypeService accountTypeService, AccountTypeRepository accountTypeRepository) {
        this.accountTypeService = accountTypeService;
        this.accountTypeRepository = accountTypeRepository;
    }

    /**
     * {@code POST  /account-types} : Create a new accountType.
     *
     * @param accountType the accountType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountType, or with status {@code 400 (Bad Request)} if the accountType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-types")
    public ResponseEntity<AccountType> createAccountType(@Valid @RequestBody AccountType accountType) throws URISyntaxException {
        log.debug("REST request to save AccountType : {}", accountType);
        if (accountType.getId() != null) {
            throw new BadRequestAlertException("A new accountType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountType result = accountTypeService.save(accountType);
        return ResponseEntity
            .created(new URI("/api/account-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-types/:id} : Updates an existing accountType.
     *
     * @param id the id of the accountType to save.
     * @param accountType the accountType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountType,
     * or with status {@code 400 (Bad Request)} if the accountType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-types/{id}")
    public ResponseEntity<AccountType> updateAccountType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountType accountType
    ) throws URISyntaxException {
        log.debug("REST request to update AccountType : {}, {}", id, accountType);
        if (accountType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountType result = accountTypeService.update(accountType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-types/:id} : Partial updates given fields of an existing accountType, field will ignore if it is null
     *
     * @param id the id of the accountType to save.
     * @param accountType the accountType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountType,
     * or with status {@code 400 (Bad Request)} if the accountType is not valid,
     * or with status {@code 404 (Not Found)} if the accountType is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountType> partialUpdateAccountType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountType accountType
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountType partially : {}, {}", id, accountType);
        if (accountType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountType> result = accountTypeService.partialUpdate(accountType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountType.getId().toString())
        );
    }

    /**
     * {@code GET  /account-types} : get all the accountTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountTypes in body.
     */
    @GetMapping("/account-types")
    public ResponseEntity<List<AccountType>> getAllAccountTypes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AccountTypes");
        Page<AccountType> page = accountTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-types/:id} : get the "id" accountType.
     *
     * @param id the id of the accountType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-types/{id}")
    public ResponseEntity<AccountType> getAccountType(@PathVariable Long id) {
        log.debug("REST request to get AccountType : {}", id);
        Optional<AccountType> accountType = accountTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountType);
    }

    /**
     * {@code DELETE  /account-types/:id} : delete the "id" accountType.
     *
     * @param id the id of the accountType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-types/{id}")
    public ResponseEntity<Void> deleteAccountType(@PathVariable Long id) {
        log.debug("REST request to delete AccountType : {}", id);
        accountTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
