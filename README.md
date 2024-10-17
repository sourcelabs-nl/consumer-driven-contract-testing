# Pact Broker

Run docker compose to start the pact broker

```shell
docker compose up
```

Now browse to http://localhost:9292

# Upload pacts

Run the following maven command on the invoice-service 

```shell
cd invoice-service
mvn clean install pact:publish
```

Now go to http://localhost:9292 again, what has changed?

# Verify the contract

Run the following maven command on the customer-service

```shell
cd customer-service
mvn clean install
```

# Fix the Pact

Now fix the pact and repeat the steps above!
