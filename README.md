# Pet Store Rest Service

App deployed here:

Front End - http://petshopapp.thebluecabrio.com/

Back End - http://petshopapi.thebluecabrio.com/

Requirements:
- Java 8
- Maven

`mvn clean install`

`cd target/`

`java -jar petstore-rest-0.0.1-SNAPSHOT.jar`

## Authentication

This used OAuth2 with JWT

See `src/test/java/com/thebluecabrio.petstore/rest/PetControllerTest.java` for more details on how to authorize

```
    /**
     * GET A TOKEN
     */

    String content = mockMvc.perform(
            post("/oauth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", "b")
                    .param("password", "password")
                    .param("scope", "petshop")
                    .param("client_id", "petshop")
                    .param("grant_type", "password")
    ).andExpect(status().isOk())
            .andDo(print())
            .andReturn().getResponse().getContentAsString();

    assertNotNull("content should not be null", content);
```

Then supply it for every request until it expires

```
.header(HttpHeaders.AUTHORIZATION, tokenType+" "+accessToken))
```



