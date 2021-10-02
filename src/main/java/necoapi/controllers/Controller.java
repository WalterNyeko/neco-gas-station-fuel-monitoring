package necoapi.controllers;

import necoapi.domain.*;
import necoapi.helpers.jwt.JwtUtils;
import necoapi.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/neco/v1")
public class Controller {

    @Autowired
    Service service;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * Create A New Account For Customer
     * @param accountRequest
     * @return
     */
    @PostMapping("/account")
    public ResponseEntity<Object> createAccount(
            @Valid @RequestBody AccountRequest accountRequest) {

        return new ResponseEntity<>(
                service.createAccount(accountRequest),
                HttpStatus.CREATED);
    }

    /**
     * Edit Account Details For A Customer
     * @param accountId
     * @param accountRequest
     * @return
     */
    @PutMapping("/account/{accountId}")
    public ResponseEntity<Object> editAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody AccountRequest accountRequest) {

        return new ResponseEntity<>(
                service.editAccount(accountId, accountRequest),
                HttpStatus.OK);
    }

    /**
     * Find An Account Using Its ID
     * @param accountId
     * @return
     */
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Object> findAccountById(
            @PathVariable Long accountId) {

        return new ResponseEntity<>(
                service.findAccountByAccountId(accountId),
                HttpStatus.OK);
    }

    /**
     * Delete An Existing Account Using Its ID
     * @param accountId
     * @return
     */
    @DeleteMapping("/account/{accountId}")
    public ResponseEntity<Object> deleteAccount(
            @PathVariable Long accountId) {

        return new ResponseEntity<>(
                service.deleteAccount(accountId),
                HttpStatus.OK);
    }

    /**
     * List All Existing Accounts
     * @return
     */
    @GetMapping("/accounts")
    public ResponseEntity<Object> listAccounts() {

        return new ResponseEntity<>(
                service.listAccounts(),
                HttpStatus.OK);
    }

    @GetMapping("/account/{accountName}/transactions")
    public ResponseEntity<Object> listAccountTransactionsByAccountName(
            @PathVariable String accountName) {

        return new ResponseEntity<>(
                service.listAccountTransactionsByAccountName(accountName),
                HttpStatus.OK);
    }


    /**
     * Create A New Product AT Neco Petrol Station
     * @param productRequest
     * @return
     */
    @PostMapping("/product")
    public ResponseEntity<Object> createProduct(
            @Valid @RequestBody ProductRequest productRequest) {

        return new ResponseEntity<>(
                service.createProduct(productRequest),
                HttpStatus.CREATED);
    }

    /**
     * Edit Product Details
     * @param productId
     * @param productRequest
     * @return
     */
    @PutMapping("/product/{productId}")
    public ResponseEntity<Object> editProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest productRequest) {

        return new ResponseEntity<>(
                service.editProduct(productId, productRequest),
                HttpStatus.OK);
    }

    /**
     * Find Specific Product Using Its ID
     * @param productId
     * @return
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<Object> findProductById(
            @PathVariable Long productId) {

        return new ResponseEntity<>(
                service.findProductByProductId(productId),
                HttpStatus.OK);
    }

    /**
     * Delete Product Using Its ID
     * @param productId
     * @return
     */
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Object> deleteProduct(
            @PathVariable Long productId) {

        return new ResponseEntity<>(
                service.deleteProduct(productId),
                HttpStatus.OK);
    }

    /**
     * List All Existing Products
     * @return
     */
    @GetMapping("/products")
    public ResponseEntity<Object> listProducts() {

        return new ResponseEntity<>(
                service.listProducts(),
                HttpStatus.OK);
    }


    /**
     * Create New Fueling Transaction
     * @param transactionRequest
     * @return
     */
    @PostMapping("/transaction")
    public ResponseEntity<Object> createTransaction(
            @Valid @RequestBody TransactionRequest transactionRequest) {

        return new ResponseEntity<>(
                service.createTransaction(transactionRequest),
                HttpStatus.CREATED);
    }

    /**
     * Edit Fueling Transaction Details
     * @param transactionId
     * @param transactionRequest
     * @return
     */
    @PutMapping("/transaction/{transactionId}")
    public ResponseEntity<Object> editTransaction(
            @PathVariable Long transactionId,
            @Valid @RequestBody TransactionRequest transactionRequest) {

        return new ResponseEntity<>(
                service.editTransaction(transactionId, transactionRequest),
                HttpStatus.OK);
    }

    /**
     * Find Specific Transaction Using Its ID
     * @param transactionId
     * @return
     */
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Object> findTransactionById(
            @PathVariable Long transactionId) {

        return new ResponseEntity<>(
                service.findTransactionByTransactionId(transactionId),
                HttpStatus.OK);
    }

    /**
     * Delete Specific Transaction Using Its ID
     * @param transactionId
     * @return
     */
    @DeleteMapping("/transaction/{transactionId}")
    public ResponseEntity<Object> deleteTransaction(
            @PathVariable Long transactionId) {

        return new ResponseEntity<>(
                service.deleteTransaction(transactionId),
                HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody UserRequest userRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getUsername(),
                            userRequest.getPassword())
            );
        }catch (Exception ex){
            throw new Exception("Invalid username or password");
        }
        return new ResponseEntity<>(new LoginResponse(jwtUtils.generateToken(userRequest.getUsername())), HttpStatus.CREATED);
    }

    /**
     * Search Accounts By Any of Its Parameters
     * @param searchKey
     * @return
     * @throws Exception
     */
    @GetMapping("/search/accounts")
    public ResponseEntity<Object> searchAccounts(
            @PathParam(value = "searchKey") String searchKey)
            throws Exception {
        return new ResponseEntity<>(
                service.searchAccounts(searchKey),
                HttpStatus.OK);
    }

}
