# PGR209 - Backendprogramming exam

**Candidate numbers:** <br>
2014 <br>
2006

<h2>Getting started:</h2>
- Run 'MachineFactoryApplication.java' <br>
- The application runs on 'http://localhost:8080' <br>
- Use Postman or any other API platform to send requests <br>
- Please use the provided json-templates in “TEMPLATES.txt”, for PUT and POST requests. <br>
&nbsp;&nbsp;&nbsp;All requests are also showcased in our endpoint tests.

<h2>To run all tests, you can either:</h2>
- Right-click java folder under the ‘test’ folder and Run tests<br>
- Run ‘mvn test’ in terminal from ‘..\machine-factory’. (‘cd machine-factory’)<br>
- Run each class file or test method individually.

<h2>Project information:</h2>
- All features, business logic and rules are showcased and thoroughly tested in our tests.<br>
- An overview over the highlighted features, business logic and rules are described below. <br>
- We’ve made a service file named ‘DataFeedService’ to initialize sample data using our applications logic,<br> 
&nbsp;&nbsp;&nbsp;following the order of how everything is created, saved and set.

<h2>API:</h2>
/api/order <br>
/api/customer <br>
/api/address <br>
/api/machine <br>
/api/subassembly <br>
/api/part <br>

<h2>All APIs have implemented the following METHODS:</h2>
GET                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-> returns all entities of a specific domain <br>
GET /{id}            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-> returns an entity by id <br>
GET /page/{pageNr}   &nbsp;&nbsp;&nbsp;-> returns all entities on a specific page <br>
POST                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-> creates an entity of a specific domain <br>
DELETE /{id}         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-> deletes an entity by id <br>
PUT /{id}            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-> updates an entity by id <br>


<h2>Besides implementing all required features, here’s our additional features:</h2>
- Create a customer, and add an address to it. <br>
- Create an address and add it to a customer. <br>
- Add an address to a customer. <br>
- Added controlled responses for all requests to the API endpoints. <br>
- Implemented DTOs for all models. <br>
- Deleting a machine, deletes all orders containing that machine. <br>
- Deleting a part or subassembly sets fields containing those <br>
&nbsp;&nbsp;&nbsp;entities to null before deleting the part or subassembly. <br>
- The user can update machine with an empty subassemblyId, <br>
&nbsp;&nbsp;&nbsp;but gets an error even if the field contains just one id that doesn't exist in the subassembly table. <br>
- The user can update subassembly with an empty partId, <br>
&nbsp;&nbsp;&nbsp;but gets an error even if the field contains just one id that doesn't exist in the part table. <br>
- Besides the required tests, we also made unit tests for the repositories <br>
- Implemented ActiveProfiles for more controlled testing environments

<h2>Logic / relationship </h2>
<p>Deleting an Order, deletes the whole order, but the customer,
while the address and machine will still exist in their own tables.</p>

<p>Deleting a Customer, deletes the customer and the order(if there is any),
while the address will still exist in its own table.</p>

<p>Deleting an Address, deletes the address and the order(if there is any),
while the customer will still exist in its own table.</p>

<p>Deleting a Machine, deletes all orders containing the machine, then deletes the machine.</p>

<p>Deleting a Subassembly, sets the subassemblyId field in all machines containing that 
subassemblyId to null, then deletes the subassembly.</p>

<p>Deleting a Part, sets the partId field in all subassemblies containing that 
partId to null, then deletes the part.</p>

<h2>Rules:</h2>
<p>Before creating an order, the customer, address and machine(s) must be created(if they don't already exist). <br> 
Then create an order with the correct ids associated.</p>

<p>Machines, subassemblies and parts can be created separately. <br>
They can be connected by adding the associated ids upon creation or when updating.</p>

<p>The same goes for the relationship between customers and addresses.</p>

<h2>Sources:</h2>
- Associated lectures and course material in course PGR209, autumn 2023

<h3>Spring Boot and Spring Data JPA:</h3>
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://www.baeldung.com/java-dto-pattern#2-connecting-both-sides <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpHeaders.html <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://www.geeksforgeeks.org/stream-anymatch-java-examples/ <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://www.baeldung.com/java-optional-uses <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>

<h3>Database, H2 and entities: </h3>
- https://www.h2database.com/html/tutorial.html <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://www.baeldung.com/jpa-cascade-types <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://www.geeksforgeeks.org/spring-boot-h2-database/ <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>

<h3>Mock and testing:</h3>
- https://docs.spring.io/spring-framework/reference/testing.html <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://docs.spring.io/spring-framework/reference/testing/annotations/integration-spring/annotation-activeprofiles.html <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://docs.spring.io/spring-framework/reference/testing/spring-mvc-test-framework/vs-end-to-end-integration-tests.html <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://www.baeldung.com/maven-run-single-test <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/MediaType.html <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023)

<h3>Github repositories from lectures:</h3>
- https://github.com/jlwcrews2/vet-clinic <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023) <br>
- https://github.com/jlwcrews2/jpa-demo <br>
&nbsp;&nbsp;&nbsp;(Referred and accessed in December, 2023)

