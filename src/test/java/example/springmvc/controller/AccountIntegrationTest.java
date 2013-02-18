package example.springmvc.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:WEB-INF/application-context.xml",
		"classpath:WEB-INF/mvc-dispatcher-servlet.xml" })
@WebAppConfiguration("src/main/webapp")
public class AccountIntegrationTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testGetSignup() throws Exception {
		this.mockMvc.perform(get("/signup")).andExpect(status().isOk())
				.andExpect(view().name("signup"));
	}

	@Test
	public void testGetLogin() throws Exception {
		this.mockMvc.perform(get("/login")).andExpect(status().isOk())
				.andExpect(view().name("login"));
	}

	@Test
	public void testGetIndex_withoutLogin() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(model().attribute("name", "World"))
				.andExpect(model().attribute("isUserLoggedIn", false));
	}

	@Test
	public void testCreateAccountAndLogin() throws Exception {
		this.mockMvc
				.perform(
						post("/signup")
								.param("id", "admin")
								.param("password", "system")
								.param("confirmedPassword", "system"))
				.andExpect(redirectedUrl("/"));
		this.mockMvc
				.perform(
						post("/login").param("userId", "admin")
							.param("password", "system"))
				.andExpect(cookie().exists("sessionId"))
				.andExpect(redirectedUrl("/"));
	}
}
