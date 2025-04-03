package nl.sourcelabs.service.product;


import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@Provider("product-service")
@PactBroker
public class ProductServiceVerifierPactTest extends AbstractProviderVerifierPactTest {

    @MockBean
    private ProductService productService;

    /**
     * Prepares the pact verifier tests and puts the system under test in the state required for passing the pacts belonging to the specified state below.
     */
    @State({"a product exists"})
    public void aProductExists() {
        when(productService.getProductById("1"))
            .thenReturn(new Product("1", "Apple - MacBook Pro - 16'", 3499_000));
    }
}

