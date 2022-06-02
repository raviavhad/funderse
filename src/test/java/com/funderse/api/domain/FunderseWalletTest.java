package com.funderse.api.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.funderse.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FunderseWalletTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FunderseWallet.class);
        FunderseWallet funderseWallet1 = new FunderseWallet();
        funderseWallet1.setId(1L);
        FunderseWallet funderseWallet2 = new FunderseWallet();
        funderseWallet2.setId(funderseWallet1.getId());
        assertThat(funderseWallet1).isEqualTo(funderseWallet2);
        funderseWallet2.setId(2L);
        assertThat(funderseWallet1).isNotEqualTo(funderseWallet2);
        funderseWallet1.setId(null);
        assertThat(funderseWallet1).isNotEqualTo(funderseWallet2);
    }
}
