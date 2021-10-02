package necoapi.services;

import necoapi.domain.*;
import necoapi.models.User;

import java.util.List;
import java.util.Map;

public interface Service {

    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse editProduct(Long productId, ProductRequest productRequest);
    ProductResponse findProductByProductId(Long productId);
    Map<String, Object> deleteProduct(Long productId);
    List<ProductResponse> listProducts();

    AccountResponse createAccount(AccountRequest accountRequest);
    AccountResponse editAccount(Long accountId, AccountRequest accountRequest);
    AccountResponse findAccountByAccountId(Long accountId);
    AccountResponse findAccountByAccountName(String accountName);
    Map<String, Object> deleteAccount(Long accountId);
    List<AccountResponse> listAccounts();
    List<TransactionResponse> listAccountTransactions(Long accountId);
    List<TransactionResponse> listAccountTransactionsByAccountName(String accountName);

    TransactionResponse createTransaction(TransactionRequest transactionRequest);
    TransactionResponse editTransaction(Long transactionId, TransactionRequest transactionRequest);
    TransactionResponse findTransactionByTransactionId(Long transactionId);
    Map<String, Object> deleteTransaction(Long transactionId);

    User createUser(UserRequest userRequest);

    List<AccountResponse> searchAccounts(String keyWord) throws Exception;
    TopUp topUpAccount(TopUp topUp, Long accountId);

    List<TopUpResponse> getTopUp(Long accountId);

    List<UserResponse> getUsers();
}
