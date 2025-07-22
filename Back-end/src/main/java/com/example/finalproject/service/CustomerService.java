package com.example.finalproject.service;

import com.example.finalproject.dto.AccountDTO;
import com.example.finalproject.dto.CustomerDTO;
import com.example.finalproject.entity.Account;
import com.example.finalproject.entity.Customer;
import com.example.finalproject.repository.AccountRepository;
import com.example.finalproject.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ImgurService imgurService;


    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public CustomerDTO getCustomerById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Customer not found"));
        return convertToDto(customer);
    }


    public CustomerDTO getCustomerByAccountId(Integer accountId) {
        Customer customer = customerRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Customer not found with accountId: " + accountId));
        return convertToDto(customer);
    }


    public CustomerDTO createCustomer(CustomerDTO customerDTO, MultipartFile file) throws IOException {
        Account account = accountRepository.findById(customerDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("❌ Account not found"));

        String imageUrl = (file != null && !file.isEmpty())
                ? imgurService.uploadToImgur(file)
                : "https://i.imgur.com/Fw9zB3T.png";

        Customer customer = new Customer(
                customerDTO.getBirthYear(),
                customerDTO.getGender(),
                customerDTO.getPoint() != null ? customerDTO.getPoint() : 0,
                imageUrl,
                customerDTO.getAddress(),
                account
        );

        return convertToDto(customerRepository.save(customer));
    }

    // ✅ Cập nhật khách hàng (upload ảnh mới nếu có)
    public CustomerDTO updateCustomer(Integer id, CustomerDTO customerDTO, MultipartFile file) throws IOException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Customer not found with ID: " + id));

        if (customerDTO.getBirthYear() != null) customer.setBirthYear(customerDTO.getBirthYear());
        if (customerDTO.getGender() != null) customer.setGender(customerDTO.getGender());
        if (customerDTO.getPoint() != null) customer.setPoint(customerDTO.getPoint());
        if (customerDTO.getAddress() != null) customer.setAddress(customerDTO.getAddress());

        if (file != null && !file.isEmpty()) {
            customer.setImageFile(imgurService.uploadToImgur(file));
        }

        return convertToDto(customerRepository.save(customer));
    }


    public void deleteCustomer(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Customer not found"));
        customerRepository.deleteById(id);
    }


    private CustomerDTO convertToDto(Customer customer) {
        Account account = customer.getAccount();

        AccountDTO accountDTO = new AccountDTO(
                account.getAccountId(),
                account.getUsername(),
                account.getEmail(),
                account.getPhoneNumber()
        );

        return new CustomerDTO(
                customer.getId(),
                customer.getBirthYear(),
                customer.getGender(),
                customer.getPoint(),
                customer.getImageFile(),
                customer.getAddress(),
                account.getAccountId(),
                accountDTO
        );
    }
}
