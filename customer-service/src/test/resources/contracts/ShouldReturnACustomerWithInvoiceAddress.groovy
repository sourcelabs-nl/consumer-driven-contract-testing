import org.springframework.cloud.contract.spec.Contract

Contract.make {

    description "should return a customer with invoice address"

    request {
        method 'GET'
        url '/customers/cust123'
        headers {
            accept(applicationJson())
        }
    }

    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body([
            customerId: "cust123",
            invoiceAddress: [
                zipCode: "1234AB",
                houseNumber: "123"
            ]
        ])
    }
}