package ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import json.CashFlowPersistence;
import core.AbstractAccount;
import core.User;

public class RemoteAccess implements CashFlowAccess {

    private final URI endpointBaseUri;
    private ObjectMapper objectMapper;
    private User user;
    private CashFlowPersistence cfp;
    public final static String SERVERSAVEFILE = "Server-SaveData.json";

    public RemoteAccess(URI endpointBaseUri) {
        this.endpointBaseUri = endpointBaseUri;
        this.objectMapper = CashFlowPersistence.createObjectMapper();
        this.cfp = new CashFlowPersistence();
        cfp.setSaveFilePath(SERVERSAVEFILE);
    }

    public User getUser() {
        if (user == null) {
            HttpRequest request = HttpRequest.newBuilder(endpointBaseUri).header("Accept", "application/json").GET()
                    .build();
            try {
                final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                        HttpResponse.BodyHandlers.ofString());
                this.user = objectMapper.readValue(response.body(), User.class);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }

    private URI accountUri(int accountNumber) {
        return endpointBaseUri.resolve(String.valueOf(accountNumber));
    }

    /**
     * Gets the account with the given account number.
     * 
     * @param accountNumber the account number
     * @return the account with the given account number
     */
    @Override
    public AbstractAccount getAccount(int accountNumber) {
        AbstractAccount oldAccount = this.user.getAccount(accountNumber);
        if (oldAccount == null) {
            HttpRequest request = HttpRequest.newBuilder(accountUri(accountNumber)).header("Accept", "application/json")
                    .GET().build();
            try {
                final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                        HttpResponse.BodyHandlers.ofString());
                String responseString = response.body();
                System.out.println("getAccount(" + accountNumber + ") response: " + responseString);
                AbstractAccount account = objectMapper.readValue(responseString, AbstractAccount.class);
                this.user.addAccount(account);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return oldAccount;
    }

    private void putAccount(AbstractAccount account) {
        user.addAccount(account);
        try {
            String json = objectMapper.writeValueAsString(account);
            HttpRequest request = HttpRequest.newBuilder(accountUri(account.getAccountNumber()))
                    .header("Accept", "application/json").header("Content-Type", "application/json")
                    .PUT(BodyPublishers.ofString(json)).build();
            final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
            String responseString = response.body();
            Boolean added = objectMapper.readValue(responseString, Boolean.class);
            if (added != null) {
                user.addAccount(account);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addAccount(AbstractAccount account) {
        putAccount(account);
    }

    @Override
    public boolean deleteAccount(int accountNumber) {
        try {
            HttpRequest request = HttpRequest.newBuilder(accountUri(accountNumber)).header("Accept", "application/json")
                    .DELETE().build();
            final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
            String responseString = response.body();
            Boolean removed = objectMapper.readValue(responseString, Boolean.class);
            if (removed != null) {
                return user.removeAccount(user.getAccount(accountNumber));
            }
            return false;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void transfer(AbstractAccount payer, AbstractAccount reciever, double amount) {
        payer.transfer(reciever, amount);
        putAccount(payer);
        putAccount(reciever);
    }

    @Override
    public void saveUser() throws IllegalStateException, IOException {
        cfp.saveUser(user, SERVERSAVEFILE);
    }

    @Override
    public User loadInitialUser() throws IllegalStateException, IOException {
        if (cfp.doesFileExist(SERVERSAVEFILE)){
            return cfp.loadUser(SERVERSAVEFILE);
        }
        return new User(123456);
    }

}