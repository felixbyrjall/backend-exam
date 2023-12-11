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
  
- [x] You will build a rest Api using Java and Spring Boot.
- [x] You must use Spring Data JPA and H2 in-memory database to persist all data.
- [x] You must use Repositories, Services, and RestControllers
- [x] You should implement pagination for all endpoints that return lists
- [ ] You may implement Flyway, but it is not required
- [x] You should have the following tests, making use of @SpringBootTest, @DataJpaTest where necessary:
- [x] Unit tests of services and business logic using MockBean
- [x] Integrations tests
- [x] End-to-end tests using MockMvc

  
You are building an order system for a factory that sells machines.
The domain objects are as follows:

- [x] Customer
- [x] Address
- [x] Order
- [x] Machine
- [x] Subassembly
- [x] Part

Model:
- [x] A customer can have many addresses<br>
- [x] An address has one or more customers<br>
- [x] A customer can have many orders<br>
- [x] An order has one or more machines<br>
- [x] A machine has one or more subassemblies<br>
- [x] A subassembly has one or more parts

More model:
- [x] Customer must know about addresses, and addresses must know customers they belong to.<br/>
- [x] Order must know about customers, and customers must know all their orders.<br>
- [x] Order must know about machines, but machines do not need to know what orders they are part of.<br>
- [x] Machines must know what subassemblies they require, <br>
  subassemblies do not need to know what machines they are a part of.<br>
- [x] Subassemblies must know about parts, parts do not need to know what machines they are part of

Controllers, Services, Repositories should be implemented for all domain objects,<br/> 
implementing the following functionality:</br>
- [x] Get one by id</br>
- [x] Get all with pagination<br>
- [x] Create one<br>
- [x] Delete one<br>
- [x] Update one<br>

<p>Additional functionality can be added, such as:<br/>
- Create a customer, and add an address to it<br/>
- Create an address and add it to a customer<br/>
- Add an address to a customer</p>

<h3>Delivery:</h3>

- [ ] You should deliver a zip file with your complete code.<br/>
- [x] DO NOT INCLUDE THE /TARGET DIRECTORY.<br/>
- [ ] Include a readme file in the project root.<br/>
- [ ] This file should include any instructions needed to run the application.<br/>
- [ ] This readme should also include anything you were unable to get working, <br/>
      and what you tried to do to fix it.<br/>
- [ ] Do not include names of the group members.

<h3>Grading:</h3>

- [x] F - That application does not include at least one model,<br>
repository, service, controller, or the application does not run.
- [x] E - The application implements partial functionality<br>
- [x] D - The application implements complete fuctionality, but no tests
- [x] C - The application implements complete functionality,<br>
some additional functionality and includes unit tests
- [x] B - The application implements complete functionality,<br>
      some additional functionality and includes both unit and integration tests
- [x] A - The application implements complete functionality, <br>
some additional functionality and includes unit, integration, <br>
and end-to-end tests
