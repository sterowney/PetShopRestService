package com.thebluecabrio.petstore.rest;

import com.jayway.jsonassert.JsonAssert;
import com.jayway.jsonpath.JsonPath;
import com.thebluecabrio.petstore.PetStoreReStServiceApplication;
import com.thebluecabrio.petstore.domain.Status;
import com.thebluecabrio.petstore.service.requests.CreatePetRequest;
import com.thebluecabrio.petstore.service.requests.LookupItem;
import com.thebluecabrio.petstore.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PetStoreReStServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String accessToken = "";
    private String refreshToken = "";
    private String tokenType = "Bearer";

    @Before
    public void setUp() throws Exception {

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

        JsonAssert.with(content).assertNotNull("access_token");
        JsonAssert.with(content).assertNotNull("refresh_token");
        JsonAssert.with(content).assertNotNull("token_type");
        JsonAssert.with(content).assertNotNull("expires_in");
        JsonAssert.with(content).assertNotNull("scope");
        JsonAssert.with(content).assertNotNull("jti");

        accessToken = JsonPath.read(content, "$.access_token").toString().trim();
        refreshToken = JsonPath.read(content, "$.refresh_token").toString().trim();

        assertNotNull(accessToken);
        assertNotNull(refreshToken);
    }

    @Test
    public void test_find_pets() throws Exception {

        MvcResult result = mockMvc
                .perform(get("/pet")
                .header(HttpHeaders.AUTHORIZATION, tokenType+" "+accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result);
        assertNotNull(result.getResponse().getContentAsString());

        String content = result.getResponse().getContentAsString();

        JsonAssert.with(content).assertNotNull("$.content[0].id");
        JsonAssert.with(content).assertNotNull("$.content[0].name");
        JsonAssert.with(content).assertNotNull("$.content[0].status");
        JsonAssert.with(content).assertNotNull("$.content[0].tags");
        JsonAssert.with(content).assertNotNull("$.content[0].photoUrls");
        JsonAssert.with(content).assertNotNull("$.content[0].category");

    }

    @Test
    public void test_get_pet() throws Exception {

        MvcResult result = mockMvc
                .perform(get("/pet/1")
                .header(HttpHeaders.AUTHORIZATION, tokenType+" "+accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result);
        assertNotNull(result.getResponse().getContentAsString());

        String content = result.getResponse().getContentAsString();

        JsonAssert.with(content).assertNotNull("$.id");
        JsonAssert.with(content).assertNotNull("$.name");
        JsonAssert.with(content).assertNotNull("$.status");
        JsonAssert.with(content).assertNotNull("$.tags");
        JsonAssert.with(content).assertNotNull("$.photoUrls");
        JsonAssert.with(content).assertNotNull("$.category");
    }

    @Test
    public void test_delete_pet() throws Exception {

        MvcResult result = mockMvc
                .perform(delete("/pet/3")
                .header(HttpHeaders.AUTHORIZATION, tokenType+" "+accessToken))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        assertNotNull(result);
        assertNotNull(result.getResponse().getContentAsString());

        assertEquals("content is blank", "", result.getResponse().getContentAsString());
    }

    @Test
    public void test_update_pet() throws Exception {

        MvcResult getResult = mockMvc
                .perform(get("/pet/1")
                .header(HttpHeaders.AUTHORIZATION, tokenType+" "+accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(getResult);
        assertNotNull(getResult.getResponse().getContentAsString());

        MvcResult updateResult = mockMvc.perform(put("/pet/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, tokenType+" "+accessToken)
                .content(getResult.getResponse().getContentAsString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(updateResult);
        assertNotNull(updateResult.getResponse().getContentAsString());

        String content = updateResult.getResponse().getContentAsString();

        JsonAssert.with(content).assertNotNull("$.id");
        JsonAssert.with(content).assertNotNull("$.name");
        JsonAssert.with(content).assertNotNull("$.status");
        JsonAssert.with(content).assertNotNull("$.tags");
        JsonAssert.with(content).assertNotNull("$.photoUrls");
        JsonAssert.with(content).assertNotNull("$.category");
    }

    @Test
    public void test_create_new_pet() throws Exception {

        CreatePetRequest createPetRequest = new CreatePetRequest();
        createPetRequest.setName("Zebra");
        createPetRequest.setStatus(Status.AVAILABLE.name());
        createPetRequest.setCategory(new LookupItem(1l, "Puppy"));
        createPetRequest.setPhotoUrls(Arrays.asList(new LookupItem("http://google.com")));
        createPetRequest.setTags(Arrays.asList(new LookupItem("zebra")));

        MvcResult result = mockMvc.perform(post("/pet")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.toJson(createPetRequest))
                .header(HttpHeaders.AUTHORIZATION, tokenType+" "+accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result);
        assertNotNull(result.getResponse().getContentAsString());

        String content = result.getResponse().getContentAsString();

        JsonAssert.with(content).assertNotNull("$.id");
        JsonAssert.with(content).assertNotNull("$.name");
        JsonAssert.with(content).assertNotNull("$.status");
        JsonAssert.with(content).assertNotNull("$.tags");
        JsonAssert.with(content).assertNotNull("$.photoUrls");
        JsonAssert.with(content).assertNotNull("$.category");
    }

    @Test
    public void test_create_new_pet_with_no_category_or_status() throws Exception {

        CreatePetRequest createPetRequest = new CreatePetRequest();

        MvcResult result = mockMvc.perform(post("/pet")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.toJson(createPetRequest))
                .header(HttpHeaders.AUTHORIZATION, tokenType+" "+accessToken))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertNotNull(result);
        assertNotNull(result.getResponse().getContentAsString());

        String content = result.getResponse().getContentAsString();

        JsonAssert.with(content).assertNotNull("$.code");
        JsonAssert.with(content).assertNotNull("$.message");

        JsonAssert.with(content).assertEquals("$.code", 404);
        JsonAssert.with(content).assertEquals("$.message", "Category not found null");

    }

    @Test
    public void test_delete_a_pet_when_not_authorized() throws Exception {

        String content = mockMvc.perform(
                post("/oauth/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "a") // login as A user
                        .param("password", "password")
                        .param("scope", "petshop")
                        .param("client_id", "petshop")
                        .param("grant_type", "password")
        ).andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertNotNull("content should not be null", content);

        String a_UserAccessToken = JsonPath.read(content, "$.access_token").toString().trim();

        mockMvc
                .perform(delete("/pet/3")
                .header(HttpHeaders.AUTHORIZATION, tokenType+" "+a_UserAccessToken))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }
}