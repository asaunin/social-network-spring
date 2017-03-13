# Spring Boot version of the Social Network Application [![Build Status](https://travis-ci.org/ASaunin/social-network-spring.svg?branch=master)](https://travis-ci.org/ASaunin/social-network-spring/)

![Spring Boot version of the Social Network Application](https://cloud.githubusercontent.com/assets/19559375/23728361/73745b58-046d-11e7-8849-c8e9140d3e6e.png)

##Stack

**Spring:** Boot, RESTful WS, Data, Security

**Web:** AngularJS, Bootstrap, Bower, Gulp

**Tests:** JUnit, Mockito, AssertJ

**CI:** Travis

**Third-party libraries:** [Letter avatar](https://agentejo.com/blog/tired-of-gravatar-try-letter-avatar) (by Artur Heinze)


##Functionality

- Sign-In / Sign-Up
- Send messages
- Add / remove friends
- Update contact information / Change password
- View person & friend lists
- View person's contact information
- View last messages
- Profile images / avatars
- Pagination
- Search

##Build & Run application using Intellij IDEA

```
git clone https://github.com/ASaunin/social-network-spring.git
cd social-network-spring
```
Start Spring boot application from the classpath: `org.asaunin.socialnetwork.SocialNetworkApplication`

Open [http://localhost:8080](http://localhost:8080) browser location

##Deploy separate application parts

###Deploy backend (REST API)
```
git clone https://github.com/ASaunin/social-network-spring.git
cd social-network-spring
mvnw clean install
cd api
..\mvnw spring-boot:run
```
###Deploy frontend (Web GUI)
Configure web-server host & port. They should differ from the API ones. Set `resources.web-url` property for production environement.

Deploy **war-file** from `webapp\target` folder on the web-server

Open browser location matchin for deployed application

NB: **URI** constant in `webapp/srs/scripts/app.js` defines API url (by default it matches to http://localhost:8080)

##Credentials

- E-mail:   m_korleone@mail.ru
- Password: 12345