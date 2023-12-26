##  PERSON FINDER SERVICE
- rest resource protected by keycloak oauth implementation



###  Local Setup
- Intellij (add annotation processors:)  
  - lombok.launch.AnnotationProcessorHider$AnnotationProcessor
  - org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor
- H2DB (h2) or 18C Oracle XE(local)
  - run sql scripts in test resources
- Disable Security by commenting SecurityConfig(todo: replace with property setting hcange)F
- Disable CorsMapping in WebConfig (todo: replace with property setting hcange)F
- mvn spring-boot:run


### Docker build
docker build -t peoplesvc .  
docker run -p8083:8083 peoplesvc

### End Points
- localhost:8081
- /people/id -- get person by id
- /people/simple/id   --- get simplified details of person by id
- /people/{id}/relations    --- get relationships of person


###  DEPLOY
- enable security by uncomenting SecurityConfig
- re-enable cor


### TODO
1. Flag for Secured Annotation

### Development Process
1. Branch out from branch `local`
2. Ensure local branch has latest `master` changes
3. Ensure branch can run locally with `h2` active profile
4. Make Changes on  your  branch and Test
4. When ready copy master versions for the ff files using `git checkout master/filepath`:
   - SecurityConfig.java
   - PeopleResource.java
 