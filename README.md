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

We need to add the `pact.verifier.publishResults=true` to the environment variables in order
to upload the results to the Pact broker. Run the following maven command on the customer-service:

```shell
./mvnw -pl customer-service clean install -Dpact.verifier.publishResults=true
```

Now go to http://localhost:9292 again, what has changed?