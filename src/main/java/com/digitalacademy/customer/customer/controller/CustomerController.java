package com.digitalacademy.customer.customer.controller;

import com.digitalacademy.customer.customer.model.Customer;
import com.digitalacademy.customer.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

//    @GetMapping("/list")
//    public ArrayList<Map> customerList() {
//        ArrayList<Map> customers = new ArrayList<>();
//        Map<String, String> customer = new HashMap<>();
//        customer.put("1", "Gique");
//        customer.put("2", "Net");
//        customer.put("3", "First");
//        customers.add(customer);
//
//        return customers;
//    }

//    @GetMapping("/list")
//    public List<Customer> customerList() {
//        List<Customer> css = new ArrayList<>();
//
//        Customer cs = new Customer();
//        cs.setId(1L);
//        cs.setFirstName("Ryan");
//        cs.setLastName("Giggs");
//        cs.setEmail("gique@test.com");
//        cs.setPhoneNo("66615559999");
//        cs.setAge(18);
//        css.add(cs);
//
//        cs = new Customer();
//        cs.setId(1L);
//        cs.setFirstName("Ka");
//        cs.setLastName("Mak");
//        cs.setEmail("ka@mak.com");
//        cs.setPhoneNo("66615559999");
//        cs.setAge(22);
//        css.add(cs);
//
//        return css;
//    }

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/list")
    public List<Customer> customerList() {
        return customerService.getCustomerList();
    }

    //GetById
    @GetMapping("/{id}")
    public Customer getCustomerId(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    //localhost:8081/api/customer
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer body) {
        Customer customer = customerService.createCustomer(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id,
                                            @Valid @RequestBody Customer body) {
        body.setId(id);
        Customer customer = customerService.updateCustomer(id, body);
        return customer != null ? ResponseEntity.ok(customer)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteById(id) ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

}
