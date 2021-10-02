package necoapi.helpers;

import necoapi.domain.*;
import necoapi.models.Account;
import necoapi.models.Product;
import necoapi.models.TopUp;
import necoapi.models.Transaction;
import necoapi.models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ObjectMappers {

    private static DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    public static Product mapProductEntity(ProductRequest productRequest) {
        return Product.builder()
                .productName(productRequest.getProductName())
                .productDescription(productRequest.getProductDescription())
                .productUnitPrice(productRequest.getProductUnitPrice())
                .build();
    }

    public static Product mapExistingProductEntity(
            Product oldProduct, ProductRequest productRequest) {
        oldProduct.setProductName(productRequest.getProductName());
        oldProduct.setProductDescription(productRequest.getProductDescription());
        oldProduct.setProductUnitPrice(productRequest.getProductUnitPrice());
        return oldProduct;
    }

    public static ProductResponse mapProductResponse(Product product) {
        return ProductResponse.productResponseBuilder()
                .productName(product.getProductName())
                .productUnitPrice(product.getProductUnitPrice())
                .productDescription(product.getProductDescription())
                .productId(product.getId())
                .build();
    }

    public static Account mapAccountEntity(AccountRequest accountRequest) {
        return Account.builder()
                .accountName(accountRequest.getAccountName())
                .openingBalance(accountRequest.getOpeningBalance())
                .currentBalance(accountRequest.getOpeningBalance())
                .openingDate(accountRequest.getOpeningDate())
                .bankOverdraftAllowed(accountRequest.getBankOverdraftAllowed())
                .build();
    }

    public static Account mapExistingAccountEntity(Account account, AccountRequest accountRequest) {
        account.setAccountName(accountRequest.getAccountName());
        account.setOpeningBalance(accountRequest.getOpeningBalance());
        account.setOpeningDate(accountRequest.getOpeningDate());
        account.setBankOverdraftAllowed(accountRequest.getBankOverdraftAllowed());
        return account;
    }

    public static AccountResponse mapAccountResponse(Account account) {
        return AccountResponse.accountResponseBuilder()
                .accountId(account.getId())
                .accountName(account.getAccountName())
                .currentBalance(account.getCurrentBalance())
                .openingBalance(account.getOpeningBalance())
                .openingDate(account.getOpeningDate())
                .bankOverdraftAllowed(account.getBankOverdraftAllowed())
                .createdBy(account.getCreatedBy())
                .createdDate(account.getCreatedDate())
                .lastModifiedBy(account.getLastModifiedBy())
                .lastModifiedDate(account.getLastModifiedDate())
                .build();
    }

    public static TransactionResponse mapTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .transactionDate(transaction.getTransactionDate())
                .transactionAmount(transaction.getTransactionAmount())
                .orderNumber(transaction.getOrderNumber())
                .accountName(transaction.getAccount().getAccountName())
                .productName(transaction.getProduct().getProductName())
                .vehicleNumber(transaction.getVehicleNumber())
                .transactionQuantity(transaction.getTransactionQuantity())
                .build();
    }

    public static Transaction mapTransactionEntity(
            TransactionRequest transactionRequest,
            Account account,
            Product product,
            Double transactionAmount) {
        return Transaction.builder()
                .transactionDate(transactionRequest.getTransactionDate())
                .orderNumber(transactionRequest.getOrderNumber())
                .vehicleNumber(transactionRequest.getVehicleNumber())
                .transactionAmount(transactionAmount)
                .product(product)
                .account(account)
                .transactionQuantity(transactionRequest.getTransactionQuantity())
                .build();
    }

    public static Transaction mapExistingTransactionEntity(
            Transaction originalTransaction,
            TransactionRequest transactionRequest,
            Product product,
            Account account,
            Double transactionAmount) {
        originalTransaction.setTransactionQuantity(transactionRequest.getTransactionQuantity());
        originalTransaction.setTransactionAmount(transactionAmount);
        originalTransaction.setTransactionDate(transactionRequest.getTransactionDate());
        originalTransaction.setAccount(account);
        originalTransaction.setProduct(product);
        originalTransaction.setOrderNumber(transactionRequest.getOrderNumber());
        originalTransaction.setVehicleNumber(transactionRequest.getVehicleNumber());
        return originalTransaction;
    }

    public static User mapUserInfoToUser(UserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .roles(userRequest.getRoles())
                .address(userRequest.getAddress())
                .build();
    }

    public static TopUp mapTopUp(necoapi.domain.TopUp topUp, Account account) {
        return TopUp.builder()
                .amount(topUp.getAmount())
                .account(account)
                .previousBalance(account.getCurrentBalance() - topUp.getAmount())
                .currentBalance(account.getCurrentBalance())
                .build();
    }

    public static TopUpResponse mapTopUpResponse(TopUp topUp) {
        return TopUpResponse.builder()
                .topUpId(topUp.getId())
                .topUpAmount(topUp.getAmount())
                .previousBalance(topUp.getPreviousBalance())
                .currentBalance(topUp.getCurrentBalance())
                .createdBy(topUp.getCreatedBy())
                .createdDate(dateFormatter.format(topUp.getCreatedDate()))
                .lastModifiedBy(topUp.getLastModifiedBy())
                .lastModifiedDate(dateFormatter.format(topUp.getLastModifiedDate()))
                .build();
    }

    public static UserResponse mapUserToUserResponse(User user) {
        final String[] roles = {""};
        user.getRoles().stream().forEach(role -> {
            roles[0] += role.getRole();
        });
        return UserResponse.builder()
                .id(user.getId())
                .address(user.getAddress())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles[0])
                .createdBy(user.getCreatedBy())
                .createdDate(dateFormatter.format(user.getCreatedDate()))
                .lastModifiedBy(user.getLastModifiedBy())
                .lastModifiedDate(dateFormatter.format(user.getLastModifiedDate()))
                .build();
    }
}
