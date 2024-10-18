# Pact Broker

## pre-requisites

Make sure you have Docker Desktop or Rancher Desktop installed 
and that you are able to run the `docker compose` command.

## docker compose

Run docker compose to start the pact broker

```shell
docker compose up
```

Now browse to http://localhost:9292

# Upload pacts

Run the following maven command on the invoice-service 

```shell
./mvnw -pl invoice-service clean install pact:publish
```

Now go to http://localhost:9292 again, what has changed?

# Verify the contract

Run the following maven command on the customer-service

```shell
./mvnw -pl customer-service clean install
```

# Fix the Pact

Now fix the pact and repeat the steps above!

# Upload results

The maven build process is configured in such a way that the results of the verification tests are uploaded to the Pact broker, this can be done
by specifying Pact specific environment variables. See the `pom.xml` file of the customer-service and have a look at the maven-surefire-plugin:

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
./mvnw -pl customer-service clean install
```

Browse to http://localhost:9292 and notice that verification results should be available!