package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.payload.GetDeviceOutput;
import com.ninjaone.rmm.security.SecurityConfig;
import com.ninjaone.rmm.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceController.class)
@ContextConfiguration(classes={SecurityConfig.class})
public class DeviceControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService service;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    @WithMockUser(username="dev")
    @Test
    public void getDeviceById() throws Exception {

        //List<Employee> allEmployees = Arrays.asList(alex);
        GetDeviceOutput output = new GetDeviceOutput(
                1L, "Windows 10", DeviceType.WINDOWS_WORKSTATION, List.of() );
        when(service.getDeviceById(1L)).thenReturn(output);

        String accessToken = getAccessToken("dev", "dev");


        mockMvc.perform(get("/devive/1")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$", hasSize(1)))
                //.andExpect(jsonPath("$[0].name", is(alex.getName())));
    }

    private String getAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/login")
                        .params(params)
                        .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
