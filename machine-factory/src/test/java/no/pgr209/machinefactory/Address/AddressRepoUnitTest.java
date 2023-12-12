package no.pgr209.machinefactory.Address;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("dev")
public class AddressRepoUnitTest {

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Test
    public void save_shouldReturnAddress() throws Exception{
        Address address = new Address();
        Address savedAddress = addressRepo.save(address);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getAddressId()).isNotNull();
    }
}
