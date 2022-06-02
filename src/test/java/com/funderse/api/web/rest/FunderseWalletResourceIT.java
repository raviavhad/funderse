package com.funderse.api.web.rest;

import static com.funderse.api.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.funderse.api.IntegrationTest;
import com.funderse.api.domain.FunderseWallet;
import com.funderse.api.domain.enumeration.FunderseWalletStatus;
import com.funderse.api.repository.FunderseWalletRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FunderseWalletResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FunderseWalletResourceIT {

    private static final BigDecimal DEFAULT_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIMIT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SPENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SPENT = new BigDecimal(2);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final FunderseWalletStatus DEFAULT_STATUS = FunderseWalletStatus.AVAILABLE;
    private static final FunderseWalletStatus UPDATED_STATUS = FunderseWalletStatus.RESTRICTED;

    private static final String ENTITY_API_URL = "/api/funderse-wallets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FunderseWalletRepository funderseWalletRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFunderseWalletMockMvc;

    private FunderseWallet funderseWallet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FunderseWallet createEntity(EntityManager em) {
        FunderseWallet funderseWallet = new FunderseWallet()
            .limit(DEFAULT_LIMIT)
            .balance(DEFAULT_BALANCE)
            .spent(DEFAULT_SPENT)
            .description(DEFAULT_DESCRIPTION)
            .sortOrder(DEFAULT_SORT_ORDER)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED)
            .status(DEFAULT_STATUS);
        return funderseWallet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FunderseWallet createUpdatedEntity(EntityManager em) {
        FunderseWallet funderseWallet = new FunderseWallet()
            .limit(UPDATED_LIMIT)
            .balance(UPDATED_BALANCE)
            .spent(UPDATED_SPENT)
            .description(UPDATED_DESCRIPTION)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS);
        return funderseWallet;
    }

    @BeforeEach
    public void initTest() {
        funderseWallet = createEntity(em);
    }

    @Test
    @Transactional
    void createFunderseWallet() throws Exception {
        int databaseSizeBeforeCreate = funderseWalletRepository.findAll().size();
        // Create the FunderseWallet
        restFunderseWalletMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funderseWallet))
            )
            .andExpect(status().isCreated());

        // Validate the FunderseWallet in the database
        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeCreate + 1);
        FunderseWallet testFunderseWallet = funderseWalletList.get(funderseWalletList.size() - 1);
        assertThat(testFunderseWallet.getLimit()).isEqualByComparingTo(DEFAULT_LIMIT);
        assertThat(testFunderseWallet.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
        assertThat(testFunderseWallet.getSpent()).isEqualByComparingTo(DEFAULT_SPENT);
        assertThat(testFunderseWallet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFunderseWallet.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testFunderseWallet.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testFunderseWallet.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
        assertThat(testFunderseWallet.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createFunderseWalletWithExistingId() throws Exception {
        // Create the FunderseWallet with an existing ID
        funderseWallet.setId(1L);

        int databaseSizeBeforeCreate = funderseWalletRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFunderseWalletMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funderseWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunderseWallet in the database
        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = funderseWalletRepository.findAll().size();
        // set the field null
        funderseWallet.setDescription(null);

        // Create the FunderseWallet, which fails.

        restFunderseWalletMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funderseWallet))
            )
            .andExpect(status().isBadRequest());

        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFunderseWallets() throws Exception {
        // Initialize the database
        funderseWalletRepository.saveAndFlush(funderseWallet);

        // Get all the funderseWalletList
        restFunderseWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funderseWallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].limit").value(hasItem(sameNumber(DEFAULT_LIMIT))))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))))
            .andExpect(jsonPath("$.[*].spent").value(hasItem(sameNumber(DEFAULT_SPENT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getFunderseWallet() throws Exception {
        // Initialize the database
        funderseWalletRepository.saveAndFlush(funderseWallet);

        // Get the funderseWallet
        restFunderseWalletMockMvc
            .perform(get(ENTITY_API_URL_ID, funderseWallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(funderseWallet.getId().intValue()))
            .andExpect(jsonPath("$.limit").value(sameNumber(DEFAULT_LIMIT)))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)))
            .andExpect(jsonPath("$.spent").value(sameNumber(DEFAULT_SPENT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFunderseWallet() throws Exception {
        // Get the funderseWallet
        restFunderseWalletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFunderseWallet() throws Exception {
        // Initialize the database
        funderseWalletRepository.saveAndFlush(funderseWallet);

        int databaseSizeBeforeUpdate = funderseWalletRepository.findAll().size();

        // Update the funderseWallet
        FunderseWallet updatedFunderseWallet = funderseWalletRepository.findById(funderseWallet.getId()).get();
        // Disconnect from session so that the updates on updatedFunderseWallet are not directly saved in db
        em.detach(updatedFunderseWallet);
        updatedFunderseWallet
            .limit(UPDATED_LIMIT)
            .balance(UPDATED_BALANCE)
            .spent(UPDATED_SPENT)
            .description(UPDATED_DESCRIPTION)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS);

        restFunderseWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFunderseWallet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFunderseWallet))
            )
            .andExpect(status().isOk());

        // Validate the FunderseWallet in the database
        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeUpdate);
        FunderseWallet testFunderseWallet = funderseWalletList.get(funderseWalletList.size() - 1);
        assertThat(testFunderseWallet.getLimit()).isEqualByComparingTo(UPDATED_LIMIT);
        assertThat(testFunderseWallet.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testFunderseWallet.getSpent()).isEqualByComparingTo(UPDATED_SPENT);
        assertThat(testFunderseWallet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFunderseWallet.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testFunderseWallet.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testFunderseWallet.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testFunderseWallet.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingFunderseWallet() throws Exception {
        int databaseSizeBeforeUpdate = funderseWalletRepository.findAll().size();
        funderseWallet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFunderseWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, funderseWallet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funderseWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunderseWallet in the database
        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFunderseWallet() throws Exception {
        int databaseSizeBeforeUpdate = funderseWalletRepository.findAll().size();
        funderseWallet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunderseWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funderseWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunderseWallet in the database
        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFunderseWallet() throws Exception {
        int databaseSizeBeforeUpdate = funderseWalletRepository.findAll().size();
        funderseWallet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunderseWalletMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funderseWallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FunderseWallet in the database
        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFunderseWalletWithPatch() throws Exception {
        // Initialize the database
        funderseWalletRepository.saveAndFlush(funderseWallet);

        int databaseSizeBeforeUpdate = funderseWalletRepository.findAll().size();

        // Update the funderseWallet using partial update
        FunderseWallet partialUpdatedFunderseWallet = new FunderseWallet();
        partialUpdatedFunderseWallet.setId(funderseWallet.getId());

        partialUpdatedFunderseWallet
            .balance(UPDATED_BALANCE)
            .spent(UPDATED_SPENT)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS);

        restFunderseWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFunderseWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFunderseWallet))
            )
            .andExpect(status().isOk());

        // Validate the FunderseWallet in the database
        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeUpdate);
        FunderseWallet testFunderseWallet = funderseWalletList.get(funderseWalletList.size() - 1);
        assertThat(testFunderseWallet.getLimit()).isEqualByComparingTo(DEFAULT_LIMIT);
        assertThat(testFunderseWallet.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testFunderseWallet.getSpent()).isEqualByComparingTo(UPDATED_SPENT);
        assertThat(testFunderseWallet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFunderseWallet.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testFunderseWallet.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testFunderseWallet.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testFunderseWallet.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateFunderseWalletWithPatch() throws Exception {
        // Initialize the database
        funderseWalletRepository.saveAndFlush(funderseWallet);

        int databaseSizeBeforeUpdate = funderseWalletRepository.findAll().size();

        // Update the funderseWallet using partial update
        FunderseWallet partialUpdatedFunderseWallet = new FunderseWallet();
        partialUpdatedFunderseWallet.setId(funderseWallet.getId());

        partialUpdatedFunderseWallet
            .limit(UPDATED_LIMIT)
            .balance(UPDATED_BALANCE)
            .spent(UPDATED_SPENT)
            .description(UPDATED_DESCRIPTION)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS);

        restFunderseWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFunderseWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFunderseWallet))
            )
            .andExpect(status().isOk());

        // Validate the FunderseWallet in the database
        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeUpdate);
        FunderseWallet testFunderseWallet = funderseWalletList.get(funderseWalletList.size() - 1);
        assertThat(testFunderseWallet.getLimit()).isEqualByComparingTo(UPDATED_LIMIT);
        assertThat(testFunderseWallet.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testFunderseWallet.getSpent()).isEqualByComparingTo(UPDATED_SPENT);
        assertThat(testFunderseWallet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFunderseWallet.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testFunderseWallet.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testFunderseWallet.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testFunderseWallet.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingFunderseWallet() throws Exception {
        int databaseSizeBeforeUpdate = funderseWalletRepository.findAll().size();
        funderseWallet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFunderseWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, funderseWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funderseWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunderseWallet in the database
        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFunderseWallet() throws Exception {
        int databaseSizeBeforeUpdate = funderseWalletRepository.findAll().size();
        funderseWallet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunderseWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funderseWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunderseWallet in the database
        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFunderseWallet() throws Exception {
        int databaseSizeBeforeUpdate = funderseWalletRepository.findAll().size();
        funderseWallet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunderseWalletMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(funderseWallet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FunderseWallet in the database
        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFunderseWallet() throws Exception {
        // Initialize the database
        funderseWalletRepository.saveAndFlush(funderseWallet);

        int databaseSizeBeforeDelete = funderseWalletRepository.findAll().size();

        // Delete the funderseWallet
        restFunderseWalletMockMvc
            .perform(delete(ENTITY_API_URL_ID, funderseWallet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FunderseWallet> funderseWalletList = funderseWalletRepository.findAll();
        assertThat(funderseWalletList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
