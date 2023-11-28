package com.msbanks.accounts.services;

import com.msbanks.accounts.dto.CustomerDto;

public interface IAccountsServices {
    /**
     *
     * @param customerDto
     */
    void createAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber
     * @return Accounts Details based on a given Number
     */
    CustomerDto fetchAccount(String mobileNumber);

    /**
     *
     * @param customerDto
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileAccount
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(String mobileAccount);

}
