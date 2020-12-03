package com.digitalacademy.customer.controller;

import com.digitalacademy.customer.customer.CustomerSupportTest;
import com.digitalacademy.customer.customer.controller.CustomerController;
import com.digitalacademy.customer.customer.model.Customer;
import com.digitalacademy.customer.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerController = new CustomerController(customerService);
        mvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @DisplayName("Test get all customer should return list of customer")
    @Test
    public void testGetCustomerList() throws Exception {
            when(customerService.getCustomerList())
                    .thenReturn(CustomerSupportTest.getListCustomer());

            MvcResult mvcResult = mvc.perform(get("/customer/list"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andReturn();

            JSONArray jsonArray = new JSONArray(mvcResult.getResponse().getContentAsString());
            assertEquals("1", jsonArray.getJSONObject(0).get("id").toString());
            assertEquals("Ryan", jsonArray.getJSONObject(0).get("first_name"));
            assertEquals("Giggs", jsonArray.getJSONObject(0).get("last_name"));
            assertEquals("66818884484", jsonArray.getJSONObject(0).get("phoneNo"));
            assertEquals("gique@gique.com", jsonArray.getJSONObject(0).get("email"));
            assertEquals(32, jsonArray.getJSONObject(0).get("age"));

            assertEquals("2", jsonArray.getJSONObject(1).get("id").toString());
            assertEquals("David", jsonArray.getJSONObject(1).get("first_name"));
            assertEquals("Beckham", jsonArray.getJSONObject(1).get("last_name"));
            assertEquals("66818884999", jsonArray.getJSONObject(1).get("phoneNo"));
            assertEquals("david@david.com", jsonArray.getJSONObject(1).get("email"));
            assertEquals(45, jsonArray.getJSONObject(1).get("age"));

            verify(customerService,times(1)).getCustomerList();
    }

/*    @DisplayName("Test get customer by id should return customer")
    @Test
    void testGetCustomerId() throws Exception {
        Long reqId = 2L;
        when(customerService.getCustomerById(reqId)).thenReturn(
                CustomerSupportTest.getListCustomer().get(1));
        MvcResult mvcResult = mvc.perform(get("/customer/"+ reqId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals("2", jsonObject.get("id").toString());
        assertEquals("Beckham", jsonObject.get("last_name"));
        assertEquals("66818884999", jsonObject.get("phoneNo"));
        assertEquals("david@david.com", jsonObject.get("email"));
        assertEquals(45, jsonObject.get("age"));
    }

    @DisplayName("Test get customer by id should return not found")
    @Test
    void testGetCustomerByIdNotFound() throws Exception {
        Long reqId = 4L;
        mvc.perform(get("/customer"+ reqId))
                .andExpect(status().isNotFound())
                .andReturn();
    }  */

    @DisplayName("Test create customer should return success")
    @Test
    public void testCreateCustomer() throws Exception {
        Customer customerReq = CustomerSupportTest.getCreateCustomer();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customerReq);

        when(customerService.createCustomer(customerReq)).thenReturn(CustomerSupportTest.getCreatedCustomer());

        MvcResult mvcResult = mvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isCreated())
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals("6", jsonObject.get("id").toString());
        assertEquals("New", jsonObject.get("first_name"));
        assertEquals("NewNew", jsonObject.get("last_name"));
        assertEquals("66818884477", jsonObject.get("phoneNo"));
        assertEquals("new@new.com", jsonObject.get("email"));
        assertEquals(10, jsonObject.get("age"));
    }

    @DisplayName("Test create customer with first name is empty")
    @Test
    public void testCreateCustomerWithNameEmpty() throws Exception {
        Customer customerReq = CustomerSupportTest.getCreateCustomer();
        customerReq.setFirst_name("");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customerReq);

        when(customerService.createCustomer(customerReq))
                .thenReturn(CustomerSupportTest.getCreatedCustomer());

        MvcResult mvcResult = mvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("Validation failed for argument [0] in public org.springframework.http.Response<?>" +
                "com.digitalacademy.customer.controller.CustomerController.createCustomer(com.digitalacademy.customer.model.Customer): " +
                "[Field error in object 'customer' on field 'first_name': rejected value []; codes " +
                "[Size.customer.first_name,Size.first_name,Size.java.lang.String,Size]; arguments" +
                "org.springframework.context.support.DefaultMessageSourceResolvable: codes [customer.first_name, first_name]; " +
                "arguments []; default message [[first_name],100,1]; default message [Please type your first name size between 1-100]",
                mvcResult.getResolvedException().getMessage());
    }

    @DisplayName("Test update customer should return success")
    @Test
    public void testUpdateCustomer() throws Exception {
        Customer customerReq = CustomerSupportTest.getBeforeUpdateCustomer();
        Long reqId = 3L;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customerReq);

        when(customerService.updateCustomer(reqId, customerReq))
                .thenReturn(CustomerSupportTest.getBeforeUpdateCustomer());

        MvcResult mvcResult = mvc.perform(put("/customer/" + reqId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isOk())
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals("3", jsonObject.get("id").toString());
        assertEquals("Old", jsonObject.get("first_name"));
        assertEquals("OldOld", jsonObject.get("last_name"));
        assertEquals("66818884477", jsonObject.get("phoneNo"));
        assertEquals("old@old.com", jsonObject.get("email"));
        assertEquals(50, jsonObject.get("age"));
    }

    @DisplayName("Test update customer should return id not found")
    @Test
    public void testUpdateCustomerIdNotFound() throws Exception {
        Customer customerReq = CustomerSupportTest.getBeforeUpdateCustomer();
        Long reqId = 3L;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customerReq);

        when(customerService.updateCustomer(reqId, customerReq))
                .thenReturn(null);

        mvc.perform(put("/customer" + reqId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isNotFound())
                .andReturn();

        verify(customerService, times(1)).updateCustomer(reqId, customerReq);
    }

    @DisplayName("Test delete customer should success")
    @Test
    public void testDeleteCustomer() throws Exception {
        Long reqId = 10L;
        when(customerService.deleteById(reqId)).thenReturn(true);

        mvc.perform(delete("/customer/"+ reqId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andReturn();

        verify(customerService, times(1)).deleteById(reqId);
    }

    @DisplayName("Test delete customer should not found")
    @Test
    public void testDeleteCustomerShouldReturnNotFound() throws Exception {
        Long reqId = 10L;
        when(customerService.deleteById(reqId)).thenReturn(false);

        mvc.perform(delete("/customer/" + reqId)
        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn();

        verify(customerService, times(1)).deleteById(reqId);
    }
}
