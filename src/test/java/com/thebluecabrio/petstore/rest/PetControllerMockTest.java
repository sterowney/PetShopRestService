package com.thebluecabrio.petstore.rest;

import com.jayway.jsonassert.JsonAssert;
import com.thebluecabrio.petstore.domain.Status;
import com.thebluecabrio.petstore.service.impl.PetServiceImpl;
import com.thebluecabrio.petstore.service.requests.CreatePetRequest;
import com.thebluecabrio.petstore.service.requests.LookupItem;
import com.thebluecabrio.petstore.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by stevenrowney on 16/05/2017.
 */
public class PetControllerMockTest {

    private MockMvc mockMvc;

    @Mock
    private PetServiceImpl petService;

    @InjectMocks
    private PetController petController = new PetController();

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(petController)
                .setControllerAdvice(new ExceptionHandlerAdvice())
                .build();
    }

    @Test
    public void test_internal_server_error() throws Exception {

        when(petService.getPet(anyLong())).thenThrow(new RuntimeException("Something went wrong!!"));

        CreatePetRequest createPetRequest = new CreatePetRequest();
        createPetRequest.setName("Zebra");
        createPetRequest.setStatus(Status.AVAILABLE.name());
        createPetRequest.setCategory(new LookupItem(1l, "Puppy"));
        createPetRequest.setPhotoUrls(Arrays.asList(new LookupItem("http://google.com")));
        createPetRequest.setTags(Arrays.asList(new LookupItem("zebra")));

        MvcResult result = mockMvc
                .perform(get("/pet/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.toJson(createPetRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        assertNotNull(result);
        assertNotNull(result.getResponse().getContentAsString());

        String content = result.getResponse().getContentAsString();

        JsonAssert.with(content).assertNotNull("$.code");
        JsonAssert.with(content).assertNotNull("$.message");

        JsonAssert.with(content).assertEquals("$.code", 500);
        JsonAssert.with(content).assertEquals("$.message", "Something went wrong on our side");
    }
}
