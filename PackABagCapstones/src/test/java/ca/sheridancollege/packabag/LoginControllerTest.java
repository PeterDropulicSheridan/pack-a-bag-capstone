package ca.sheridancollege.packabag;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void testLoginSuccess() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "UserA")
                .param("password", "aaa"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/manager")); 
    }

    @Test
    public void testLoginFailure() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "wrongUser")
                .param("password", "wrongPassword"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error")); 
    }
    
}
