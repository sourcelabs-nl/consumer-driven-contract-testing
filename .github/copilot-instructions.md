# Consumer Driven Contract Testing Workshop

This repository demonstrates Consumer Driven Contract Testing with Spring Cloud Contract using two Spring Boot microservices: customer-service (provider) and invoice-service (consumer).

**Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.**

## Working Effectively

### Bootstrap, Build, and Test
- Prerequisites: Java 17+ (OpenJDK 17.0.16 pre-installed)
- Build system: Maven with wrapper (./mvnw)
- **NEVER CANCEL builds or tests** - they complete quickly in this project

**Essential Commands (validated to work):**
```bash
# Clean compile (first run with downloads): ~30 seconds. NEVER CANCEL. Set timeout to 60+ minutes for safety.
./mvnw clean compile

# Build with working tests: ~6 seconds. NEVER CANCEL. Set timeout to 30+ minutes.
./mvnw package -Dtest='!CustomerServiceTest#testGetCustomerByIdWithWireMockCloud'

# Test (excluding problematic external dependency): ~6 seconds. NEVER CANCEL. Set timeout to 30+ minutes.
./mvnw test -Dtest='!CustomerServiceTest#testGetCustomerByIdWithWireMockCloud'

# Build individual services:
./mvnw -pl customer-service clean verify    # ~8 seconds
./mvnw -pl invoice-service clean verify     # Fails due to external network dependency

# Install stubs for contract testing: ~4 seconds
./mvnw -pl customer-service clean install
```

### Run the Services
**Start customer-service (provider) on port 9090:**
```bash
./mvnw -pl customer-service spring-boot:run
# Startup time: ~1.5 seconds
# Access Swagger UI: http://localhost:9090/swagger-ui/index.html
# Test endpoint: curl http://localhost:9090/customers/cust123
```

**Start invoice-service (consumer) on port 8080:**
```bash
./mvnw -pl invoice-service spring-boot:run  
# Startup time: ~1.5 seconds
# Access Swagger UI: http://localhost:8080/swagger-ui/index.html
```

**Run as JAR files:**
```bash
java -jar customer-service/target/customer-service-0.0.1-SNAPSHOT.jar
java -jar invoice-service/target/invoice-service-0.0.1-SNAPSHOT.jar
```

## Validation

### Manual Testing Scenarios
**ALWAYS run through these scenarios after making changes:**

1. **Service Integration Test:**
   - Start both services
   - Create an invoice via POST to http://localhost:8080/invoices:
     ```json
     {
       "customerId": "cust123",
       "invoiceAmountInCents": 2000, 
       "invoiceDate": "2024-01-01",
       "description": "invoice for cust123"
     }
     ```
   - Verify response includes customer data from provider service
   - Note: `postalCode` may be null (known issue addressed by contract testing)

2. **Contract Testing Workflow:**
   - Install provider stubs: `./mvnw -pl customer-service clean install`
   - Verify contract tests run (some are @Disabled for tutorial purposes)

### Known Issues and Workarounds
- **Test failure**: `CustomerServiceTest#testGetCustomerByIdWithWireMockCloud` fails due to external network dependency
  - **Workaround**: Always exclude this test: `-Dtest='!CustomerServiceTest#testGetCustomerByIdWithWireMockCloud'`
- **Contract plugin**: Spring Cloud Contract stub runner not configured in invoice-service
  - **Workaround**: Use customer-service for contract generation and verification

## Common Tasks

### Project Structure
```
.
├── README.md                    # Workshop tutorial instructions
├── pom.xml                     # Parent Maven configuration
├── customer-service/           # Provider service (port 9090)
│   ├── pom.xml
│   └── src/
└── invoice-service/            # Consumer service (port 8080)
    ├── pom.xml
    └── src/
```

### Key Dependencies
- Spring Boot 3.2.5
- Spring Cloud Contract 4.1.2
- Java 17
- Maven 3.9.6

### Build Output Locations
- Compiled JARs: `{service}/target/{service}-0.0.1-SNAPSHOT.jar`
- Contract stubs: `customer-service/target/stubs`
- Test reports: `{service}/target/surefire-reports`

## Development Workflow

### Making Changes
1. **Always build first**: `./mvnw clean compile` (timeout: 60+ minutes)
2. **Test your changes**: `./mvnw test -Dtest='!CustomerServiceTest#testGetCustomerByIdWithWireMockCloud'` (timeout: 30+ minutes)
3. **Manual validation**: Run both services and test integration
4. **No linting required**: Project has no additional linting beyond Maven compile

### Contract Testing Development
- Contract definitions: `src/test/resources/contracts/*.groovy`
- Customer service generates and verifies contracts
- Invoice service consumes generated stubs
- Base test class: `customer-service/src/test/java/.../BaseTestClass.java` (@Disabled for tutorial)

### Troubleshooting
- **Build fails**: Check Java version (requires 17+)
- **Network test fails**: Use test exclusion parameter as documented above
- **Port conflicts**: Services run on 8080 and 9090 - ensure ports are available
- **Contract issues**: Refer to README.md tutorial steps for Spring Cloud Contract setup

### Repository Context
This is a **workshop/tutorial repository** for learning Consumer Driven Contract Testing. It contains intentionally incomplete contract configurations that are completed through the tutorial exercises in README.md.