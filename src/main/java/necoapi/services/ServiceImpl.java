package necoapi.services;

import necoapi.domain.*;
import necoapi.exceptions.*;
import necoapi.helpers.APIConstants;
import necoapi.helpers.ObjectMappers;
import necoapi.models.Account;
import necoapi.models.Product;
import necoapi.models.Transaction;
import necoapi.models.User;
import necoapi.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
@org.springframework.stereotype.Service
public class ServiceImpl implements Service{

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TopUpRepository topUpRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        String productName = productRequest.getProductName();
        Double productUnitPrice = productRequest.getProductUnitPrice();
        if (productName == null || productName.trim().isEmpty()) {
            throw new InvalidProductException(APIConstants.PRODUCT_NAME_IS_MISSING_OR_INVALID);
        }
        if (productUnitPrice == null || productUnitPrice.equals(0)) {
            throw new InvalidProductException(APIConstants.PRODUCT_PRICE_IS_MISSING_OR_INVALID);
        }
        Product product = ObjectMappers.mapProductEntity(productRequest);
        try {
            productRepository.save(product);
        }catch (DataIntegrityViolationException e) {
            String message = e.getCause().getCause().getMessage();
            if (message.contains(APIConstants.UNIQUE_INDEX_VIOLATION))
                throw new ProductAlreadyExistsException(APIConstants.PRODUCT_NAME, product.getProductName());
            throw new DataIntegrityViolationException(message);
        }
        return ObjectMappers.mapProductResponse(product);
    }

    @Override
    public ProductResponse editProduct(Long productId, ProductRequest productRequest) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = ObjectMappers.mapExistingProductEntity(productOptional.get(), productRequest);
            productRepository.save(product);
            return ObjectMappers.mapProductResponse(product);
        }
        throw new ProductNotFoundException(APIConstants.PRODUCT_NOT_FOUND);
    }

    @Override
    public ProductResponse findProductByProductId(Long productId) {
        return null;
    }

    @Override
    public Map<String, Object> deleteProduct(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            productRepository.deleteById(productId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "successfully deleted product");
            return response;
        }
        throw new ProductNotFoundException(APIConstants.PRODUCT_NOT_FOUND);
    }

    @Override
    public List<ProductResponse> listProducts() {
        List<ProductResponse> productResponses = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                productResponses.add(ObjectMappers.mapProductResponse(product));
            }
        }
        return productResponses;
    }

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        String accountName = accountRequest.getAccountName();
        String openingDate = accountRequest.getOpeningDate();
        if (accountName == null || accountName.trim().isEmpty()) {
            throw new InvalidAccountDetailsException(APIConstants.MISSING_OR_INVALID_ACCOUNT_NAME);
        }

        if (openingDate == null || openingDate.trim().isEmpty()) {
            throw new InvalidAccountDetailsException(APIConstants.MISSING_OR_INVALID_OPENING_DATE);
        }
        Account account = ObjectMappers.mapAccountEntity(accountRequest);
        try {
            accountRepository.save(account);
            return ObjectMappers.mapAccountResponse(account);
        }catch (DataIntegrityViolationException e) {
            String message = e.getCause().getCause().getMessage();
            if (message.contains(APIConstants.UNIQUE_INDEX_VIOLATION))
                throw new ProductAlreadyExistsException(APIConstants.ACCOUNT_NAME, account.getAccountName());
            throw new DataIntegrityViolationException(message);
        }
    }

    @Override
    public AccountResponse editAccount(Long accountId, AccountRequest accountRequest) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (!accountOptional.isPresent()) {
            throw new AccountNotFoundException(APIConstants.ACCOUNT_NOT_FOUND);
        }
        String accountName = accountRequest.getAccountName();
        String openingDate = accountRequest.getOpeningDate();
        if (accountName == null || accountName.trim().isEmpty()) {
            throw new InvalidAccountDetailsException(APIConstants.MISSING_OR_INVALID_ACCOUNT_NAME);
        }

        if (openingDate == null || openingDate.trim().isEmpty()) {
            throw new InvalidAccountDetailsException(APIConstants.MISSING_OR_INVALID_OPENING_DATE);
        }
        Account account = ObjectMappers.mapExistingAccountEntity(accountOptional.get(), accountRequest);
        try {
            List<Transaction> transactions = transactionRepository.findTransactionByAccount(accountOptional.get());
            if (transactions == null || transactions.isEmpty())
                account.setCurrentBalance(accountRequest.getOpeningBalance());
            Double totalTransactionAmount = 0.0;
            for (Transaction transaction : transactions) {
                totalTransactionAmount += transaction.getTransactionAmount();
            }
            Double currentBalance = account.getOpeningBalance() - totalTransactionAmount;
            account.setCurrentBalance(currentBalance);

            accountRepository.save(account);
            return ObjectMappers.mapAccountResponse(account);
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getCause().getCause().getMessage());
        }
    }

    @Override
    public AccountResponse findAccountByAccountId(Long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (!accountOptional.isPresent())
            throw new AccountNotFoundException(APIConstants.ACCOUNT_NOT_FOUND);
        return ObjectMappers.mapAccountResponse(accountOptional.get());
    }

    @Override
    public AccountResponse findAccountByAccountName(String accountName) {
        Account account = accountRepository.findAccountByAccountName(accountName);
        if (account == null)
            throw new AccountNotFoundException(APIConstants.ACCOUNT_NOT_FOUND);
        return ObjectMappers.mapAccountResponse(account);
    }

    @Override
    public Map<String, Object> deleteAccount(Long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        Map<String, Object> response = new HashMap<>();
        if (accountOptional.isPresent()) {
            accountRepository.deleteById(accountId);
            response.put(APIConstants.SUCCESS, true);
            response.put(APIConstants.MESSAGE, APIConstants.ACCOUNT_DELETED_SUCCESSFULLY);
            return response;
        }
        throw new AccountNotFoundException(APIConstants.ACCOUNT_NOT_FOUND);
    }

    @Override
    public List<AccountResponse> listAccounts() {
        List<AccountResponse> accountResponsesList = new ArrayList<>();
        List<Account> accountResponses = accountRepository.findAll();
        if (!accountResponses.isEmpty()) {
            for (Account account : accountResponses) {
                accountResponsesList.add(ObjectMappers.mapAccountResponse(account));
            }
        }
        return accountResponsesList;
    }

    @Override
    public List<TransactionResponse> listAccountTransactions(Long accountId) {
        List<TransactionResponse> transactionResponses = new ArrayList<>();
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty() && !accountOptional.isPresent())
            throw new AccountNotFoundException(APIConstants.ACCOUNT_NOT_FOUND);
        List<Transaction> transactions = transactionRepository.findTransactionByAccount(accountOptional.get());
        if (!transactions.isEmpty()) {
            for (Transaction transaction : transactions) {
                TransactionResponse transactionResponse = ObjectMappers.mapTransactionResponse(transaction);
                Double transactionAmount = transaction.getTransactionAmount();
                for (TransactionResponse transactionResponseItem : transactionResponses) {
                    transactionAmount += transactionResponseItem.getTransactionAmount();
                }
                Double currentBalance = accountOptional.get().getOpeningBalance() - transactionAmount;
                transactionResponse.setCurrentBalance(currentBalance);
                transactionResponses.add(transactionResponse);
            }
        }
        return transactionResponses;
    }

    @Override
    public List<TransactionResponse> listAccountTransactionsByAccountName(String accountName) {
        List<TransactionResponse> transactionResponses = new ArrayList<>();
        Optional<Account> accountOptional = Optional.ofNullable(accountRepository.findAccountByAccountName(accountName));
        if (accountOptional.isEmpty() && !accountOptional.isPresent())
            throw new AccountNotFoundException(APIConstants.ACCOUNT_NOT_FOUND);
        List<Transaction> transactions = transactionRepository.findTransactionByAccount(accountOptional.get());
        if (!transactions.isEmpty()) {
            for (Transaction transaction : transactions) {
                TransactionResponse transactionResponse = ObjectMappers.mapTransactionResponse(transaction);
                Double transactionAmount = transaction.getTransactionAmount();
                for (TransactionResponse transactionResponseItem : transactionResponses) {
                    transactionAmount += transactionResponseItem.getTransactionAmount();
                }
                List<necoapi.models.TopUp> topUps = topUpRepository.findByAccount(accountOptional.get());
                Double totalTopups = 0.0;
                for (necoapi.models.TopUp topUp : topUps) {
                    totalTopups += topUp.getAmount();
                }
                Double currentBalance = accountOptional.get().getOpeningBalance() + totalTopups - transactionAmount;
                transactionResponse.setCurrentBalance(currentBalance);
                transactionResponses.add(transactionResponse);
            }
        }
        return transactionResponses;
    }

    @Override
    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        try {
            String transactionAccountId = transactionRequest.getAccountId();
            String transactionProductId = transactionRequest.getProductId();
            Double transactionQuantity = transactionRequest.getTransactionQuantity();

            if (transactionAccountId == null || transactionAccountId.trim().isEmpty())
                throw new InvalidTransactionException(APIConstants.TRANSACTION_ACCOUNT_IS_INVALID);
            if (transactionProductId == null || transactionProductId.trim().isEmpty())
                throw new InvalidTransactionException(APIConstants.TRANSACTION_PRODUCT_IS_INVALID);
            if (transactionQuantity == null || transactionQuantity < 0)
                throw new InvalidTransactionException(APIConstants.TRANSACTION_AMOUNT_IS_INVALID);
            Optional<Account> accountOptional = accountRepository.findById(Long.valueOf(transactionAccountId));
            Optional<Product> productOptional = productRepository.findById(Long.valueOf(transactionProductId));
            if (!accountOptional.isPresent())
                throw new AccountNotFoundException(APIConstants.ACCOUNT_NOT_FOUND);
            if (!productOptional.isPresent())
                throw new ProductNotFoundException(APIConstants.PRODUCT_NOT_FOUND);
            Double transactionAmount = transactionQuantity * productOptional.get().getProductUnitPrice();
            if (!accountOptional.get().getBankOverdraftAllowed()
                    && transactionAmount > accountOptional.get().getCurrentBalance()) {
                throw new InvalidTransactionException(APIConstants.TRANSACTION_AMOUNT_EXCEEDS_BALANCE);
            }
            Account account = accountOptional.get();
            Double currentBalance = account.getCurrentBalance();
            currentBalance -= transactionAmount;
            account.setCurrentBalance(currentBalance);
            accountRepository.save(account);

            Transaction transaction = ObjectMappers.mapTransactionEntity(
                    transactionRequest,
                    accountOptional.get(),
                    productOptional.get(),
                    transactionAmount);
            transactionRepository.save(transaction);
            TransactionResponse transactionResponse = ObjectMappers.mapTransactionResponse(transaction);
            transactionResponse.setCurrentBalance(currentBalance);
            return transactionResponse;
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getCause().getCause().getMessage());
        }
    }

    @Override
    public TransactionResponse editTransaction(Long transactionId, TransactionRequest transactionRequest) {
        try {
            String transactionAccountId = transactionRequest.getAccountId();
            String transactionProductId = transactionRequest.getProductId();
            Double transactionQuantity = transactionRequest.getTransactionQuantity();

            if (transactionAccountId == null || transactionAccountId.trim().isEmpty())
                throw new InvalidTransactionException(APIConstants.TRANSACTION_ACCOUNT_IS_INVALID);
            if (transactionProductId == null || transactionProductId.trim().isEmpty())
                throw new InvalidTransactionException(APIConstants.TRANSACTION_PRODUCT_IS_INVALID);
            if (transactionQuantity == null || transactionQuantity < 1)
                throw new InvalidTransactionException(APIConstants.TRANSACTION_AMOUNT_IS_INVALID);
            Optional<Account> accountOptional = accountRepository.findById(Long.valueOf(transactionAccountId));
            Optional<Product> productOptional = productRepository.findById(Long.valueOf(transactionProductId));
            if (!accountOptional.isPresent())
                throw new AccountNotFoundException(APIConstants.ACCOUNT_NOT_FOUND);
            if (!productOptional.isPresent())
                throw new ProductNotFoundException(APIConstants.PRODUCT_NOT_FOUND);
            Double transactionAmount = transactionQuantity * productOptional.get().getProductUnitPrice();
            if (!accountOptional.get().getBankOverdraftAllowed()
                    && transactionAmount > accountOptional.get().getCurrentBalance()) {
                throw new InvalidTransactionException(APIConstants.TRANSACTION_AMOUNT_EXCEEDS_BALANCE);
            }
            Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
            if (!transactionOptional.isPresent())
                throw new TransactionNotFoundException(APIConstants.TRANSACTION_NOT_FOUND);

            Account account = accountOptional.get();
            Double currentBalance = account.getCurrentBalance();
            currentBalance += transactionOptional.get().getTransactionAmount();
            currentBalance -= transactionAmount;
            account.setCurrentBalance(currentBalance);
            accountRepository.save(account);
            Transaction transaction = ObjectMappers.mapExistingTransactionEntity(
                    transactionOptional.get(),
                    transactionRequest,
                    productOptional.get(),
                    account,
                    transactionAmount);
            transactionRepository.save(transaction);
            TransactionResponse transactionResponse = ObjectMappers.mapTransactionResponse(transaction);
            transactionResponse.setCurrentBalance(currentBalance);
            return transactionResponse;
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getCause().getCause().getMessage());
        }
    }

    @Override
    public TransactionResponse findTransactionByTransactionId(Long transactionId) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if (!transactionOptional.isPresent())
            throw new TransactionNotFoundException(APIConstants.TRANSACTION_NOT_FOUND);
        return ObjectMappers.mapTransactionResponse(transactionOptional.get());
    }

    @Override
    public Map<String, Object> deleteTransaction(Long transactionId) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        Map<String, Object> response = new HashMap<>();
        if (transactionOptional.isPresent()) {
            Optional<Account> accountOptional = accountRepository.findById(transactionOptional.get().getAccount().getId());
            Account account = accountOptional.get();
            Double currentBalance = account.getCurrentBalance() + transactionOptional.get().getTransactionAmount();
            account.setCurrentBalance(currentBalance);
            transactionRepository.deleteById(transactionId);
            accountRepository.save(account);
            response.put(APIConstants.SUCCESS, true);
            response.put(APIConstants.MESSAGE, APIConstants.TRANSACTION_DELETED_SUCCESSFULLY);
            return response;
        }
        throw new TransactionNotFoundException(APIConstants.TRANSACTION_NOT_FOUND);
    }

    @Override
    public User createUser(UserRequest userRequest) {
        User user = ObjectMappers.mapUserInfoToUser(userRequest);
        userRepository.save(user);
        return user;
    }

    @Override
    public List<AccountResponse> searchAccounts(String keyWord) throws Exception {
        List<Account> accountList = new ArrayList<>();
        try {
            accountList = accountRepository.searchAccount(keyWord);
            if (accountList.isEmpty()) {
                return new ArrayList<>();
            }
            List<AccountResponse> accountResponses = new ArrayList<>();
            for (Account account: accountList) {
                accountResponses.add(ObjectMappers.mapAccountResponse(account));
            }
            return accountResponses;
        }catch (Exception ex) {
            throw new Exception("Exception occurred: "+ ex.getMessage());
        }
    }

    @Override
    public TopUp topUpAccount(TopUp topUp, Long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (!accountOptional.isPresent())
            throw new AccountNotFoundException(APIConstants.ACCOUNT_NOT_FOUND);
        try {
            Account account = accountOptional.get();
            account.setCurrentBalance(account.getCurrentBalance() + topUp.getAmount());
            accountRepository.save(account);
            necoapi.models.TopUp topUpModel = ObjectMappers.mapTopUp(topUp, account);
            topUpRepository.save(topUpModel);
        }catch (Exception e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return topUp;
    }

    @Override
    public List<TopUpResponse> getTopUp(Long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (!accountOptional.isPresent())
            throw new AccountNotFoundException(APIConstants.ACCOUNT_NOT_FOUND);
        List<TopUpResponse> topUpsResponses = new ArrayList<>();
        List<necoapi.models.TopUp> topUps = new ArrayList<>();
        try {
            Account account = accountOptional.get();
            topUps = topUpRepository.findByAccount(account);
            for (necoapi.models.TopUp topUp : topUps) {
                topUpsResponses.add(ObjectMappers.mapTopUpResponse(topUp));
            }
        }catch (Exception e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return topUpsResponses;
    }

    @Override
    public List<UserResponse> getUsers() {
        List<UserResponse> userResponses = new ArrayList<>();
        try {
            List<User> users = userRepository.findAll();
            for (User user : users) {
                userResponses.add(ObjectMappers.mapUserToUserResponse(user));
            }
        }catch (Exception e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return userResponses;
    }
}
