# backend-exam
Exam hand in for the Backend-programming course fall 2023

<p>Course code: PGR209<br/>
Course name: Backend programmering<br/>
Assessment type: mappeksamen i gruppe (2 studenter)<br/>
Start date: 28.11.2023<br/>
End date: 19.12.2023</p>
<p>Plagiarism detection:
All exam papers, including bachelor’s and master’s theses will be checked for plagiarism. You can read more about this in the guidelines on citing sources (referencing), plagiarism, and formal requirements for submitting assignments.</p>

<p><h3>General information:</h3>
Start uploading your exam paper/file ahead of time, as it may take a long time to upload
Exam papers that are not handed in on Wiseflow by the specified time of the submission date will not be
proceeded to assessment<br>
Before submitting, remember to check that all files can be opened and that every file is included. It may be a good
idea to check the saved files on several machines before submitting in Wiseflow.
Incorrect file format or lacking documents may result in the submission not being passed or assessed.
The total file size of the entire exam paper/file must not exceed 5GB in zip format or 5MB in PDF format</p>

<h3>Assignment:</h3>
  
- [ ] You will build a rest Api using Java and Spring Boot.
- [ ] You must use Spring Data JPA and H2 in-memory database to persist all data.
- [ ] You must use Repositories, Services, and RestControllers
- [ ] You should implement pagination for all endpoints that return lists
- [ ] You may implement Flyway, but it is not required
- [ ] You should have the following tests, making use of @SpringBootTest, @DataJpaTest where necessary:
- [ ] Unit tests of services and business logic using MockBean
- [ ] Integrations tests
- [ ] End-to-end tests using MockMvc

  
You are building an order system for a factory that sells machines.
The domain objects are as follows:

- [ ] Customer
- [ ] Address
- [ ] Order
- [ ] Machine
- [ ] Subassembly
- [ ] Part

<p>A customer can have many addresses<br>
An address has one or more customers<br>
A customer can have many orders<br>
An order has one or more machines<br>
A machine has one or more subassemblies<br>
A subassembly has one or more parts</p>

<p>- Customer must know about addresses, and addresses must know customers they belong to.<br/>
- Order must know about customers, and customers must know all their orders.<br>
- Order must know about machines, but machines do not need to know what orders they are part of.<br>
- Machines must know what subassemblies they require, <br>
  subassemblies do not need to know what machines they are a part of.<br>
- Subassemblies must know about parts, parts do not need to know what machines they are part of</p>

<p>Controllers, Services, Repositories should be implemented for all domain objects,<br/> implementing the follow
ing functionality:</br>
- Get one by id</br>
- Get all with pagination<br>
- Create one<br>
- Delete one<br>
- Update one<br></p>

<p>Additional functionality can be added, such as:<br/>
- Create a customer, and add an address to it<br/>
- Create an address and add it to a customer<br/>
- Add an address to a customer</p>

<h3>Delivery:</h3>

- [ ] You should deliver a zip file with your complete code.<br/>
- [ ] DO NOT INCLUDE THE /TARGET DIRECTORY.<br/>
- [ ] Include a readme file in the project root.<br/>
- [ ] This file should include any instructions needed to run the application.<br/>
- [ ] This readme should also include anything you were unable to get working, <br/>
- [ ] and what you tried to do to fix it.<br/>
- [ ] Do not include names of the group members.

<h3>Grading:</h3>

- [ ] F - That application does not include at least one model,<br>
repository, service, controller, or the application does not run.
- [ ] E - The application implements partial functionality<br>
- [ ] D - The application implements complete fuctionality, but no tests
- [ ] C - The application implements complete functionality,<br>
some additional functionality and includes unit tests
- [ ] B - The application implements complete functionality,<br>
- [ ] some additional functionality and includes both unit and integration tests
- [ ] A - The application implements complete functionality, <br>
some additional functionality and includes unit, integration, <br>
and end-to-end tests
