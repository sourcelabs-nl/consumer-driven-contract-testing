## Spring Cloud Contract - hands-on

This repository consists of two microservices

- customer-service (**provider**): which services customer information like an invoice address.
- invoice-service (**consumer**): which stores invoices and uses the customer-service for retrieval of invoice address information.

Both services are exposing an HTTP based API and are implemented using Spring Boot.

### Running the provider: customer-service

This service can be started using the Spring Boot maven plugin. Run the following command to launch the application:

```shell
./mvnw -pl customer-service spring-boot:run
```

Navigate to the following URL to see the API:

* http://localhost:9090/swagger-ui/index.html

### Running the consumer: invoice-service

This service can be started using the Spring Boot maven plugin. Run the following command to launch the application:

```shell
./mvnw -pl invoice-service spring-boot:run
```

Navigate to the following URL to see the API:

* http://localhost:8080/swagger-ui/index.html


### Create an invoice

Make sure that both the customer-service and the invoice-service are running.

1. Navigate to the Swagger UI of the invoice-service.
2. Select the POST /invoices endpoint.
3. Press the 'Try it out' button
4. Add the following data to the request body field:

    ```json
    {
      "customerId": "cust123",
      "invoiceAmountInCents": 2000,
      "invoiceDate": "2024-01-01",
      "description": "invoice for cust123"
    }
    ```

5. Press 'Execute' and inspect the results of the POST.
6. Inspect the invoiceAddress information, notice anything missing?

We are going to create a contract test to fix whatever is wrong in the next exercises.

### Defining a contract using the Groovy Contract DSL

We are going to define a contract between te invoice-service and the customer-service using the Groovy Contract DSL.

1. Open `invoice-service/src/test/resources/contracts/ShouldReturnACustomerWithInvoiceAddress.groovy`.
2. Modify the request part to match the following content:

    ```groovy
    request {
        method 'GET'
        url '/customers/cust123'
    }
    ```
3. Modify the response part to match the following content:

    ```groovy
    response {
        status 200
        headers {
            header 'Content-Type': 'application/json'
        }
        body("""
        {
            "customerId": "cust123",
            "invoiceAddress" : {
                "postalCode" : "1234AB",
                "houseNumber" : "123"
            }
        }
        """)
     }
    ```
4. Enable the Spring Cloud Contract maven plugin in `invoice-service/pom.xml`, and add the following plugin to the `<plugins>` section:
    ```xml
    <plugin>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-contract-maven-plugin</artifactId>
        <version>4.1.2</version>
        <extensions>true</extensions>
        <configuration>
            <mavenTestSkip>true</mavenTestSkip>
            <httpPort>9090</httpPort>
            <testFramework>JUNIT5</testFramework>
        </configuration>
    </plugin>
    ```
5. This enables Spring Cloud Contract to look for contracts in the maven module. Run the following maven command
    ```shell
    ./mvnw -pl invoice-service clean verify    
    ```
5. Inspect the `invoice-service/target/stubs` folder and look at the contracts and mappings. Inside the mappings folder a
   WireMock stub should have been created based on the contract defined in the contracts folder.

Great work, you have created your first contract definition!

### Running generated Stubs

We can start a stub server that will serve responses based the generated stubs in the previous step.

> **NOTE** Before continuing make sure that the customer-service is not running on port 9090

Now run the following command:

```shell
./mvnw -pl invoice-service spring-cloud-contract:run
```

The terminal should at the end show:
```
...
[INFO] Started stub server for project [] on port 9090 with [1] mappings
[INFO] All stubs are now running RunningStubs [namesAndPorts={=9090}]
[INFO] Press ENTER to continue...
```

This means that our Stub server is up and running op port 9090. You can verify if it works as expected
by navigating to the following URL: http://localhost:9090/customers/cust123 which should return the following response:

```json
{
  "customerId": "cust123",
  "invoiceAddress": {
    "postalCode": "1234AB",
    "houseNumber": "123"
  }
}
```

Let's use the running stub server in our test:

1. Open `invoice-service/src/test/java/nl/sourcelabs/service/invoice/CustomerServiceContractTest.java`.
2. Remove the `@Disable` annotation from the test method and run the test.
3. Run the test. Does it work?

While this work, this is not how we want to use the stubs. The problem is that the invoice-service, which is the **consumer**, contains the contract definition and generate stubs,
while the whole idea behind Consumer Driven Contract testing is that we want the provider to own the contract definitions. And, the provider
should verify the contracts on their side.

### Move the contract to the provider

Move the file from `invoice-service/src/test/resources/contracts/ShouldReturnACustomerWithInvoiceAddress.groovy`
to `customer-service/src/test/resources/contracts/ShouldReturnACustomerWithInvoiceAddress.groovy`. Create the target folder structure if necessary.
Now that the contract file is in the correct location, the customer-service, we can create a contract verification test on the provider side.

### Create the contract verifier test

In the customer-service we are going to enable the Spring Cloud Contract maven plugin. This plugin can generate
test that run the contract verifications on the provider side.

1. Open `customer-service/pom.xml`
2. Add the Spring Cloud Contract maven plugin to the `<plugins>` section
    ```xml
    <plugin>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-contract-maven-plugin</artifactId>
        <version>4.1.2</version>
        <extensions>true</extensions>
        <configuration>
            <testFramework>JUNIT5</testFramework>
            <baseClassForTests>nl.sourcelabs.service.customer.BaseTestClass</baseClassForTests>
        </configuration>
    </plugin>
   ```
3. Open `customer-service/src/test/java/nl/sourcelabs/service/customer/BaseTestClass.java` and remove the @Disabled from the class.
4. Implement the mock behaviour that is needed to make the verifier test success. Add the code below to the `setupMocksForVerifierTests()` method body:
    ```java
    var customer = new Customer("cust123", new InvoiceAddress("1234AB", "123"));
    when(customerService.getCustomerById("cust123")).thenReturn(customer);
    ```
5. Now run the tests
    ```shell
    ./mvnw -pl customer-service clean verify
    ```
6. The contract verifier test is failing! This is because the contract expects a field
   called `postalCode` while the customer-service returns a field called `zipCode`. We found an issue in the contract, let's fix it!
   Open `customer-service/src/main/java/nl/sourcelabs/service/customer/model/InvoiceAddress.java` and rename `zipCode` to `postalCode`.
7. Now run the tests, they should now succeed!
    ```shell
    ./mvnw -pl customer-service clean verify
    ```

What happened? The Spring Cloud Contract maven plugin generates a test in the target folder. See: `customer-service/target/generated-test-sources/contracts/nl/sourcelabs/service/customer/ContractVerifierTest.java`.
This test calls our application and verifies its response based on the contract we defined earlier. The implementation
of the service had an issue with a field name being incorrect. After fixing the implementation the contract test succeeded!
Next to that the provider has also generated stubs that are according to the defined contract which can be used by its consumers.

### Use the verified stubs on the consumer side

Now that the provider contract verifier test are working, we can start using the generated provider stubs on the consumer side. 
In order to be able to do that we can share the stubs as a shared library (jar file). 

1. When we run the maven install command on the customer-server module it generates a stubs jar and installs it in the local maven repository.
   We can then use the installed stubs jar in the invoice-service (consumer). Let's install the stubs jar:
    ```shell
    ./mvnw -pl customer-service clean install
    ```
2. Open `invoice-service/src/test/java/nl/sourcelabs/service/invoice/CustomerServiceContractTest.java`
3. Add the stub runner to the class
    ```java
    @SpringBootTest
    @AutoConfigureStubRunner(
      stubsMode = StubRunnerProperties.StubsMode.LOCAL,
      ids = {"nl.sourcelabs:customer-service:+:stubs:9999"}
    )
    class CustomerServiceContractTest {
    ```
4. Point the URL in the test to the stubrunner port:
    ```java
    @Test
    void testGetCustomerByIdWithSpringCloudContract() {
        var underTest = new CustomerService("http://localhost:9999");
        Customer customer = underTest.getCustomerById("cust123");
    ```

5. Run the test either in your IDE or by executing the following maven command:
    ```shell
    ./mvnw -pl invoice-service clean verify
    ```

The build should now succeed! The `@AutoConfigureStubRunner` starts a WireMock server that serves the stubs defined
in the customer-service shared stubs library: `nl.sourcelabs:customer-service:+:stubs:9999`. The `+` defines the version
of the stub library to use, and `+` means the latest version. The `:9999` specifies the port where the WireMock server
should start.

At this point we completed our consumer driven contract testing exercise with Spring Cloud Contract.
We move the contract from the consumer to the provider side.
We created a verifier test on the provider side that verifies the contract and we have generated a stubs library.
Last but not least we created a contract test on the consumer side using the validated stubs from the provider!

Well done!