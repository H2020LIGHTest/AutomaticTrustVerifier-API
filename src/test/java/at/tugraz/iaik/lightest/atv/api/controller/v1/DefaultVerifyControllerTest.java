package at.tugraz.iaik.lightest.atv.api.controller.v1;

import at.tugraz.iaik.lightest.atv.api.model.Instance;
import at.tugraz.iaik.lightest.atv.api.model.InstanceVerification;
import at.tugraz.iaik.lightest.atv.api.model.NewInstance;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DefaultVerifyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void v1verifyTest() throws Exception {
        mockMvc.perform(get("/api/v1/"))
                .andDo(print())
                .andExpect(jsonPath("result").value(1))
                .andExpect(jsonPath("verificationResult").value("This is for testing"));
    }


    @Test
    public void addInstanceTest() throws Exception {


        String transactionFilePath = "Bid.asice";
        String policyFilePath = "policy_eidas.tpl";

        // Continue here later
        byte[] transactionBytes = Files.readAllBytes(Paths.get(transactionFilePath));
        byte[] policyBytes = Files.readAllBytes(Paths.get(policyFilePath));


        NewInstance newInstance = new NewInstance(policyBytes, transactionBytes);

        mockMvc.perform(post("/api/v1/addInstance")
                .contentType(MediaType.ALL.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(newInstance)))
                .andDo(print());
    }

    @Test
    public void findInstancebyIDTest() throws Exception {

        // Perform API request
        String transactionFilePath = "Bid.asice";
        String policyFilePath = "policy_eidas.tpl";

        byte[] transactionBytes = Files.readAllBytes(Paths.get(transactionFilePath));
        byte[] policyBytes = Files.readAllBytes(Paths.get(policyFilePath));

        NewInstance newInstance = new NewInstance(policyBytes, transactionBytes);

        MvcResult result = mockMvc.perform(post("/api/v1/addInstance")
                .contentType(MediaType.ALL.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(newInstance)))
                .andDo(print())
                .andReturn();


        // Now to the interesting part.
        String stringResult = result.getResponse().getContentAsString();

        // Map to instance verification class
        InstanceVerification response = objectMapper.readValue(stringResult, InstanceVerification.class);

        Instance instance = new Instance(response.getId());

        // Perform post request to find the result of previous API request
        mockMvc.perform(post("/api/v1/findInstance")
                .contentType(MediaType.ALL.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(instance)))
                .andDo(print());
    }

}
