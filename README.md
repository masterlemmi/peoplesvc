##  PERSON FINDER SERVICE
- rest resource protected by keycloak oauth implementation

###  Local Setup
- Intellij (add annotation processors:)  
  - lombok.launch.AnnotationProcessorHider$AnnotationProcessor
  - org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor
- 18C Oracle XE
  - run sql scripts in test resources
- Disable Security by commenting SecurityConfig(todo: replace with property setting hcange)F
- Disable CorsMapping in WebConfig (todo: replace with property setting hcange)F
- mvn spring-boot:run


### Docker build
docker build -t peoplesvc .  
docker run -p8081:8081 peoplesvc

### End Points
- localhost:8081
- /people/id -- get person by id
- /people/simple/id   --- get simplified details of person by id
- /people/{id}/relations    --- get relationships of person


###  DEPLOY
- enable security by uncomenting SecurityConfig
- re-enable cor
