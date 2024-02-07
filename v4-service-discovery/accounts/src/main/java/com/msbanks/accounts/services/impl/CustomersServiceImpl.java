package com.msbanks.accounts.services.impl;

import com.msbanks.accounts.dto.AccountsDto;
import com.msbanks.accounts.dto.CardsDto;
import com.msbanks.accounts.dto.CustomerDetailsDto;
import com.msbanks.accounts.dto.LoansDto;
import com.msbanks.accounts.entity.Accounts;
import com.msbanks.accounts.entity.Customer;
import com.msbanks.accounts.exception.ResourceNotFoundException;
import com.msbanks.accounts.mapper.AccountsMapper;
import com.msbanks.accounts.mapper.CustomerMapper;
import com.msbanks.accounts.repository.AccountsRepository;
import com.msbanks.accounts.repository.CustomerRepository;
import com.msbanks.accounts.services.ICustomersService;
import com.msbanks.accounts.services.client.CardsFeignClient;
import com.msbanks.accounts.services.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;


    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber){
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

      CustomerDetailsDto customerDetailsDto =  CustomerMapper.mapToCustomerDetailsDto( customer, new CustomerDetailsDto());
      customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

      ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoansDetails(mobileNumber);
      customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        return customerDetailsDto;
    }

}
