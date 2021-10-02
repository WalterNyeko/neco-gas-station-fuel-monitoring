package necoapi.helpers;

import necoapi.domain.*;
import necoapi.models.TopUp;

import java.util.List;

public class Selected {

    private List<AccountResponse> accountResponses;
    private List<ProductResponse> productResponses;
    private AccountResponse selectedAccount;
    private ProductResponse selectedProduct;
    private List<TransactionResponse> transactionResponses;
    private String token = "";
    private List<TopUpResponse> topUps;
    private List<UserResponse> users;

    private static Selected selectedInstance = new Selected();

    private Selected() {
    }

    public static Selected getInstance() {
            return selectedInstance;
    }

    public List<AccountResponse> getAccountResponses() {
        return accountResponses;
    }

    public void setAccountResponses(List<AccountResponse> accountResponses) {
        this.accountResponses = accountResponses;
    }

    public List<ProductResponse> getProductResponses() {
        return productResponses;
    }

    public void setProductResponses(List<ProductResponse> productResponses) {
        this.productResponses = productResponses;
    }

    public AccountResponse getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(AccountResponse selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public ProductResponse getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(ProductResponse selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public List<TransactionResponse> getTransactionResponses() {
        return transactionResponses;
    }

    public void setTransactionResponses(List<TransactionResponse> transactionResponses) {
        this.transactionResponses = transactionResponses;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<TopUpResponse> getTopUps() {
        return topUps;
    }

    public void setTopUps(List<TopUpResponse> topUps) {
        this.topUps = topUps;
    }

    public List<UserResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponse> users) {
        this.users = users;
    }
}
