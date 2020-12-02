package com.digitalacademy.customer.controller;

import com.digitalacademy.customer.customer.controller.CustomerController;
import com.digitalacademy.customer.customer.model.Customer;
import com.digitalacademy.customer.customer.service.CustomerService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import javax.print.attribute.standard.Media;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerControllerTest {

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerController = new CustomerController(customerService);
        mvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @DisplayName("Test get customer info by id should return customer information")
    @Test
    void testGetCustomerInfoByIdEquals1() throws Exception {
        Long reqParam = 1L;

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirst_name("Ryan");
        customer.setLast_name("Giggs");
        customer.setPhoneNo("66818884484");
        customer.setEmail("gique@gique.com");
        customer.setAge(32);

        when(customerService.getCustomerById(reqParam)).thenReturn(customer);

        MvcResult mvcResult = mvc.perform(get("/customer/" + reqParam))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

    }
}
