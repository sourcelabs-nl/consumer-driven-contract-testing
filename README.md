# Pact Consumer Driven Contract testing example

## pre-requisites

Make sure you have Docker Desktop or Rancher Desktop installed 
and that you are able to run the `docker compose` command.

## Running the pact broker

Run the pact broker using docker compose

```shell
cd docker && docker compose up
```

Now browse to http://localhost:9292

# Publish pacts

Run the following maven command on the invoice-service 

```shell
./mvnw -pl checkout-service clean test
```

After the test there should be pact files in the target/pacts folder. You can publish them using the pact maven plugin by running the following commmand:

```shell
./mvnw -pl checkout-service pact:publish
```

Now go to http://localhost:9292 again, what has changed?

# Verify the contract

Run the following maven command on the checkout-service

```shell
./mvnw -pl product-service clean test
```

# Fix the Pact

Now fix the contract between the two services. Rename the field `priceInCents` to `priceCents` in `nl.sourcelabs.service.product.Product` and repeat the steps above!

# Pact verifier result publication

Pact does not write the verification results to the file system. The maven build process can be configured in such a way 
that the results of the verification tests are uploaded to the Pact broker, this can be done by specifying Pact specific environment variables. 
See the `pom.xml` and have a look at the maven-surefire-plugin:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <environmentVariables>
            <pact.provider.version>${project.version}</pact.provider.version>
            <pact.verifier.publishResults>true</pact.verifier.publishResults>
            <pact_do_not_track>true</pact_do_not_track>
        </environmentVariables>
    </configuration>
</plugin>
```
You can upload the verification results by running the following command:

```shell
./mvnw -pl product-service clean test
```

Browse to http://localhost:9292 and notice that verification results should be available!