package necoapi.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import necoapi.domain.*;
import necoapi.models.Role;
import necoapi.models.TopUp;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class RESTCallHelpers {
    static RestTemplate restTemplate = new RestTemplate();
    public static void populateAccounts(TreeItem accountItems, String url) {
        accountItems.getChildren().clear();
        ResponseEntity<List> response= restTemplate.exchange(url, HttpMethod.GET,null,List.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Object> accountResponses = mapper.convertValue(response.getBody(), List.class);
        accountResponses.forEach(item -> {
            AccountResponse accountResponse = mapper.convertValue(item, AccountResponse.class);
            accountItems.getChildren().add(new TreeItem(accountResponse.getAccountName()));
        });
    }

    public static void populateProducts(TreeItem productItems) {
        List<ProductResponse> productResponses = getProducts();
        productItems.getChildren().clear();
        productResponses.forEach(productResponse -> {
            productItems.getChildren().add(new TreeItem(productResponse.getProductName()));
        });
    }

    public static List<TransactionResponse> queryTransactionsForAccount(String accountName) {
        List<TransactionResponse> transactionResponseList = new ArrayList<>();
        String url="http://localhost:8080/neco/v1/account/"+accountName+"/transactions";
        HttpEntity<ProductRequest> httpEntity = new HttpEntity<>(null, createHeaders(Selected.getInstance().getToken()));
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET,httpEntity,List.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Object> transactionsResponse = mapper.convertValue(response.getBody(), List.class);
        transactionsResponse.forEach(item -> {
            TransactionResponse transactionResponse = mapper.convertValue(item, TransactionResponse.class);
            transactionResponseList.add(transactionResponse);
        });
        Selected.getInstance().setTransactionResponses(transactionResponseList);
        return transactionResponseList;
    }

    public static ResponseEntity<Object> submitAccount(AccountRequest accountRequest) {
        String url="http://localhost:8080/neco/v1/account";
        HttpEntity<AccountRequest> httpEntity = new HttpEntity<>(accountRequest, createHeaders(Selected.getInstance().getToken()));
        return accountPostAndPut(url, httpEntity, HttpMethod.POST);
    }

    public static ResponseEntity<Object> editAccount(AccountRequest accountRequest, Long accountId) {
        String url="http://localhost:8080/neco/v1/account/"+accountId;
        HttpEntity<AccountRequest> httpEntity = new HttpEntity<>(accountRequest, createHeaders(Selected.getInstance().getToken()));
        return accountPostAndPut(url, httpEntity, HttpMethod.PUT);
    }

    private static ResponseEntity<Object> accountPostAndPut(String url, HttpEntity<AccountRequest> httpEntity, HttpMethod put) {
        ResponseEntity<Object> response = null;
        try {
            response = restTemplate.exchange(url, put, httpEntity, Object.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            APIError apiError = null;
            try {
                apiError = new ObjectMapper().readValue(exception.getResponseBodyAsString(), APIError.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(apiError, exception.getStatusCode());
        }
        return response;
    }

    public static ResponseEntity<Object> submitTransaction(TransactionRequest transactionRequest) {
        String url="http://localhost:8080/neco/v1/transaction";
        HttpEntity<TransactionRequest> httpEntity = new HttpEntity<>(transactionRequest, createHeaders(Selected.getInstance().getToken()));
        return transactionPostAndPut(url, httpEntity, HttpMethod.POST);
    }

    public static ResponseEntity<Object> editTransaction(TransactionRequest transactionRequest, Long transactionId) {

        String url="http://localhost:8080/neco/v1/transaction/"+transactionId;
        HttpEntity<TransactionRequest> httpEntity = new HttpEntity<>(transactionRequest, createHeaders(Selected.getInstance().getToken()));
        return transactionPostAndPut(url, httpEntity, HttpMethod.PUT);
    }

    public static ResponseEntity<Object> submitProduct(ProductRequest productRequest) {

        String url="http://localhost:8080/neco/v1/product";
        HttpEntity<ProductRequest> httpEntity = new HttpEntity<>(productRequest, createHeaders(Selected.getInstance().getToken()));
        return productPostAndPut(url, httpEntity, HttpMethod.POST);
    }

    public static ResponseEntity<Object> editProduct(ProductRequest productRequest, Long productId) {
        String url="http://localhost:8080/neco/v1/product/"+productId;
        HttpEntity<ProductRequest> httpEntity = new HttpEntity<>(productRequest, createHeaders(Selected.getInstance().getToken()));
        return productPostAndPut(url, httpEntity, HttpMethod.PUT);
    }

    private static ResponseEntity<Object> productPostAndPut(String url, HttpEntity<ProductRequest> httpEntity, HttpMethod put) {
        ResponseEntity<Object> response = null;
        try {
            response = restTemplate.exchange(url, put, httpEntity, Object.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            APIError apiError = null;
            try {
                apiError = new ObjectMapper().readValue(exception.getResponseBodyAsString(), APIError.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(apiError, exception.getStatusCode());
        }
        return response;
    }

    public static ResponseEntity<Object> usersPostAndPut(String url, HttpEntity<UserRequest> httpEntity, HttpMethod put) {
        ResponseEntity<Object> response = null;
        try {
            response = restTemplate.exchange(url, put, httpEntity, Object.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            APIError apiError = null;
            try {
                apiError = new ObjectMapper().readValue(exception.getResponseBodyAsString(), APIError.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(apiError, exception.getStatusCode());
        }
        return response;
    }

    public static List<AccountResponse> getAccounts() {
        List<AccountResponse> accountResponseList = new ArrayList<>();
        String url="http://localhost:8080/neco/v1/accounts";
        HttpEntity<ProductRequest> httpEntity = new HttpEntity<>(null, createHeaders(Selected.getInstance().getToken()));
        ResponseEntity<List> response= restTemplate.exchange(url, HttpMethod.GET, httpEntity ,List.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Object> accountResponses = mapper.convertValue(response.getBody(), List.class);
        accountResponses.forEach(item -> {
            AccountResponse accountResponse = mapper.convertValue(item, AccountResponse.class);
            accountResponseList.add(accountResponse);
        });
        return accountResponseList;
    }

    public static List<UserResponse> getUsers() {
        List<UserResponse> userResponseList = new ArrayList<>();
        String url="http://localhost:8080/users";
        ResponseEntity<List> response= restTemplate.exchange(url, HttpMethod.GET, null ,List.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Object> userResponses = mapper.convertValue(response.getBody(), List.class);
        userResponses.forEach(item -> {
            UserResponse userResponse = mapper.convertValue(item, UserResponse.class);
            userResponseList.add(userResponse);
        });
        Selected.getInstance().setUsers(userResponseList);
        return userResponseList;
    }


    public static List<AccountResponse> searchAccounts(String searchKey) {
        List<AccountResponse> accountResponseList = new ArrayList<>();
        String url=APIConstants.SEARCH_ACCOUNTS+searchKey;
        HttpEntity<ProductRequest> httpEntity = new HttpEntity<>(null, createHeaders(Selected.getInstance().getToken()));
        ResponseEntity<List> response= restTemplate.exchange(url, HttpMethod.GET, httpEntity ,List.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Object> accountResponses = mapper.convertValue(response.getBody(), List.class);
        accountResponses.forEach(item -> {
            AccountResponse accountResponse = mapper.convertValue(item, AccountResponse.class);
            accountResponseList.add(accountResponse);
        });
        return accountResponseList;
    }

    public static void updateTableAccounts(TableView tableView) {
        List<AccountResponse> accountResponses = getAccounts();
        populateAccountsTable(tableView, accountResponses);
    }

    public static void updateTableAccountsAfterSearch(TableView tableView, String keyword) {
        List<AccountResponse> accountResponses = searchAccounts(keyword);
        populateAccountsTable(tableView, accountResponses);
    }

    private static void populateAccountsTable(TableView tableView, List<AccountResponse> accountResponses) {
        Selected.getInstance().setAccountResponses(accountResponses);
        ObservableList<Map<String, Object>> items =
                FXCollections.<Map<String, Object>>observableArrayList();
        if (accountResponses != null) {
            for (AccountResponse accountResponse : accountResponses) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Map<String, Object> mapOfItem = new HashMap<>();
                mapOfItem.put("accountId", accountResponse.getAccountId());
                mapOfItem.put("accountName", accountResponse.getAccountName());
                mapOfItem.put("openingDate", accountResponse.getOpeningDate());
                mapOfItem.put("openingBalance", NumberFormat.getCurrencyInstance().format(accountResponse.getOpeningBalance()));
                mapOfItem.put("createdBy", accountResponse.getCreatedBy());
                mapOfItem.put("createdDate", df.format(accountResponse.getCreatedDate()));
                mapOfItem.put("lastModifiedBy", accountResponse.getLastModifiedBy());
                mapOfItem.put("lastModifiedDate", df.format(accountResponse.getLastModifiedDate()));
                items.add(mapOfItem);
            }
            tableView.getItems().clear();
            tableView.getItems().addAll(items);
        }
    }

    public static void updateTableTransactions(TableView tableView, String accountName) {
        List<TransactionResponse> transactionResponses = queryTransactionsForAccount(accountName);
        ObservableList<Map<String, Object>> items =
                FXCollections.<Map<String, Object>>observableArrayList();
        if (transactionResponses != null) {
            for (TransactionResponse transactionResponse : transactionResponses) {
                Map<String, Object> mapOfItem = new HashMap<>();
                mapOfItem.put("transactionId", transactionResponse.getTransactionId());
                mapOfItem.put("transactionDate", transactionResponse.getTransactionDate());
                mapOfItem.put("transactionAccountName" , transactionResponse.getAccountName());
                mapOfItem.put("transactionProductName" , transactionResponse.getProductName());
                mapOfItem.put("transactionVehicleNumber", transactionResponse.getVehicleNumber());
                mapOfItem.put("transactionQuantity" , transactionResponse.getTransactionQuantity());
                mapOfItem.put("transactionAmount" , NumberFormat.getCurrencyInstance().format(transactionResponse.getTransactionAmount()));
                mapOfItem.put("transactionCurrentBalance" , NumberFormat.getCurrencyInstance().format(transactionResponse.getCurrentBalance()));
                mapOfItem.put("transactionOrderNumber" , transactionResponse.getOrderNumber());
                items.add(mapOfItem);
            }
            tableView.getItems().clear();
            tableView.getItems().addAll(items);
        }
    }

    public static void updateTableTopUps(TableView tableView) {
        List<TopUpResponse> topUps = getTopUps();
        ObservableList<Map<String, Object>> items =
                FXCollections.<Map<String, Object>>observableArrayList();
        if (topUps != null && !topUps.isEmpty()) {
            for (TopUpResponse topUp : topUps) {
                Map<String, Object> mapOfItem = new HashMap<>();
                mapOfItem.put("id", topUp.getTopUpId());
                mapOfItem.put("previousBalance", topUp.getPreviousBalance());
                mapOfItem.put("amount" , topUp.getTopUpAmount());
                mapOfItem.put("currentBalance" , topUp.getCurrentBalance());
                mapOfItem.put("createdBy", topUp.getCreatedBy());
                mapOfItem.put("createdDate" , topUp.getCreatedDate());
                mapOfItem.put("lastModifiedBy" , topUp.getLastModifiedBy());
                mapOfItem.put("lastModifiedDate" , topUp.getLastModifiedDate());
                items.add(mapOfItem);
            }
            Selected.getInstance().setAccountResponses(getAccounts());
            tableView.getItems().clear();
            tableView.getItems().addAll(items);
        }
    }

    public static void updateTableUsers(TableView tableView) {
        List<UserResponse> userResponses = getUsers();
        ObservableList<Map<String, Object>> items =
                FXCollections.<Map<String, Object>>observableArrayList();
        if (userResponses != null && !userResponses.isEmpty()) {
            for (UserResponse userResponse : userResponses) {
                Map<String, Object> mapOfItem = new HashMap<>();
                mapOfItem.put("id", userResponse.getId());
                mapOfItem.put("username", userResponse.getUsername());
                mapOfItem.put("email" , userResponse.getEmail());
                mapOfItem.put("address" , userResponse.getAddress());
                mapOfItem.put("roles" , userResponse.getRoles());
                mapOfItem.put("createdBy", userResponse.getCreatedBy());
                mapOfItem.put("createdDate" , userResponse.getCreatedDate());
                mapOfItem.put("lastModifiedBy" , userResponse.getLastModifiedBy());
                mapOfItem.put("lastModifiedDate" , userResponse.getLastModifiedDate());
                items.add(mapOfItem);
            }
            tableView.getItems().clear();
            tableView.getItems().addAll(items);
        }
    }

    public static List<ProductResponse> getProducts() {
        List<ProductResponse> productResponseList = new ArrayList<>();
        String url="http://localhost:8080/neco/v1/products";
        HttpEntity<ProductRequest> httpEntity = new HttpEntity<>(null, createHeaders(Selected.getInstance().getToken()));
        ResponseEntity<List> response= restTemplate.exchange(url, HttpMethod.GET,httpEntity,List.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Object> productResponses = mapper.convertValue(response.getBody(), List.class);
        productResponses.forEach(item -> {
            ProductResponse productResponse = mapper.convertValue(item, ProductResponse.class);
            productResponseList.add(productResponse);
        });
        Selected.getInstance().setProductResponses(productResponseList);
        return productResponseList;
    }

    public static List<TopUpResponse> getTopUps() {
        List<TopUpResponse> topUps = new ArrayList<>();
        String url="http://localhost:8080/account/"+Selected.getInstance().getSelectedAccount().getAccountId()+"/topups";
        ResponseEntity<List> response= restTemplate.exchange(url, HttpMethod.GET,null,List.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Object> topUpResponse = mapper.convertValue(response.getBody(), List.class);
        topUpResponse.forEach(item -> {
            TopUpResponse topUp = mapper.convertValue(item, TopUpResponse.class);
            topUps.add(topUp);
        });
        Selected.getInstance().setTopUps(topUps);
        return topUps;
    }

    public static void populateProductsComboBox(ComboBox comboBox) {
        comboBox.getItems().clear();
        comboBox.getItems().add(0, "Select Product");
        List<ProductResponse> productResponseList = getProducts();
        Selected.getInstance().setProductResponses(productResponseList);
        for (ProductResponse productResponse : productResponseList) {
            comboBox.getItems().add(productResponse.getProductName());
        }
        comboBox.getSelectionModel().select(0);
    }

    private static ResponseEntity<Object> transactionPostAndPut(String url, HttpEntity<TransactionRequest> httpEntity, HttpMethod put) {
        ResponseEntity<Object> response = null;
        try {
            response = restTemplate.exchange(url, put, httpEntity, Object.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            APIError apiError = null;
            try {
                apiError = new ObjectMapper().readValue(exception.getResponseBodyAsString(), APIError.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(apiError, exception.getStatusCode());
        }
        return response;
    }

    public static ResponseEntity<Object> deleteRequest(String url) {
        ResponseEntity<Object> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.DELETE, null, Object.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            APIError apiError = null;
            try {
                apiError = new ObjectMapper().readValue(exception.getResponseBodyAsString(), APIError.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(apiError, exception.getStatusCode());
        }
        return response;
    }

    public static void updateBalancesInHeading(Panel panel) {
        Selected.getInstance().setAccountResponses(RESTCallHelpers.getAccounts());
        Optional<AccountResponse> optionalAccountResponse =
                Selected.getInstance().getAccountResponses()
                        .stream().filter(accountResponse ->
                        accountResponse.getAccountName()
                                .equalsIgnoreCase(Selected.getInstance()
                                        .getSelectedAccount()
                                        .getAccountName()))
                        .findFirst();
        Selected.getInstance().setSelectedAccount(optionalAccountResponse.get());
        String accountName = optionalAccountResponse.get().getAccountName();
        String openingBalance = optionalAccountResponse.get().getOpeningBalance().toString();
        String currentBalance = optionalAccountResponse.get().getCurrentBalance().toString();
        panel.setText(accountName + " (Opening Balance: " +
                NumberFormat.getCurrencyInstance().format(Double.parseDouble(openingBalance)) + " || Current Balance: " +
                NumberFormat.getCurrencyInstance().format(Double.parseDouble(currentBalance)) + ")");
    }

    public static ResponseEntity<Object> post(String url, HttpEntity<UserRequest> httpEntity, HttpMethod method) {
        ResponseEntity<Object> response = null;
        try {
            response = restTemplate.exchange(url, method, httpEntity, Object.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            APIError apiError = null;
            try {
                apiError = new ObjectMapper().readValue(exception.getResponseBodyAsString(), APIError.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(apiError, exception.getStatusCode());
        }
        return response;
    }

    public static HttpHeaders createHeaders(String token){
        return new HttpHeaders() {{
            String authHeader = "Bearer " + token;
            set( "Authorization", authHeader );
        }};
    }

    public static ResponseEntity<Object> saveTopUp(String url, HttpMethod method, HttpEntity httpEntity) {
        ResponseEntity<Object> response = null;
        try {
            response = restTemplate.exchange(url, method, httpEntity, Object.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            APIError apiError = null;
            try {
                apiError = new ObjectMapper().readValue(exception.getResponseBodyAsString(), APIError.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(apiError, exception.getStatusCode());
        }
        return response;
    }

    public static void registerAdminUser(TableView tableViewUsers) {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("admin");
        userRequest.setPassword("admin");
        userRequest.setEmail("admin@gmail.com");
        userRequest.setAddress("NECO Gas Station, Gulu");
        Set<Role> roleList = new HashSet<>();
        List<String> roles = Arrays.asList("ADMIN".split(","));
        for (String role: roles) {
            Role roleModel = new Role();
            roleModel.setRole(role);
            roleList.add(roleModel);
        }
        userRequest.setRoles(roleList);

        HttpEntity<UserRequest> httpEntity = new HttpEntity<>(userRequest,
                RESTCallHelpers.createHeaders(Selected.getInstance().getToken()));
        ResponseEntity<Object> responseEntity = RESTCallHelpers
                .usersPostAndPut(APIConstants.POST_USER, httpEntity, HttpMethod.POST);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            tableViewUsers.refresh();
            RESTCallHelpers.updateTableUsers(tableViewUsers);
        }else {
            System.out.println("Something is not working well...");
            APIError apiError = new ObjectMapper().convertValue(responseEntity.getBody(), APIError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error saving user");
            alert.setHeaderText(apiError.getReason());
            alert.setContentText(apiError.getMessage());
            alert.showAndWait();
        }

    }
}
