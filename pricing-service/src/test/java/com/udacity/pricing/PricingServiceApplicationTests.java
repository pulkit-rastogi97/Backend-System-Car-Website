package com.udacity.pricing;

import com.udacity.pricing.entity.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

//	@Autowired
//	private JacksonTester<Price> jsonPath;

	@Autowired
	MockMvc mvc;

	@Test
	public void contextLoads() {
	}


	@Test
	public void getPriceByIdTest() throws Exception {
		mvc.perform(
					get("/prices/3")
					.accept(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(jsonPath("currency", is("INR")))
					.andExpect(jsonPath("price", is(56400.98)))
					.andExpect(jsonPath("vehicleId", is(3))
					);

	}

	@Test
	public void getAllPricesTest() throws Exception{
		mvc.perform(
				   get("/prices")
				   .accept(MediaType.APPLICATION_JSON_UTF8))
				   .andExpect(status().isOk())
				   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				   .andExpect(jsonPath("_embedded.prices", hasSize(10)))
				   .andExpect(jsonPath("_embedded.prices[7].currency", is("INR")))
				   .andExpect(jsonPath("_embedded.prices[7].price", is(90123.86)))
				   .andExpect(jsonPath("_embedded.prices[7].vehicleId", is(8))
				   );

	}

}
