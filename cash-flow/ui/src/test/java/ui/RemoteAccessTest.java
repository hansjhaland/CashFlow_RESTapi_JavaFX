package ui;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.put;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.AbstractAccount;
import core.CheckingAccount;
import core.User;

public class RemoteAccessTest {
    
    private WireMockConfiguration config;
    private WireMockServer wireMockServer;

    private RemoteAccess access;

    //Her er resttjenesten mocket imens klienten/brukergrensesnittet er ekte

    @BeforeEach
    public void startWireMockServerAndSetup() throws URISyntaxException {
        //endre fx:value i RemoteCashFlow?? til en nettsideURL av noe slag
        // hva er dette: "http://localhost:8999/user/" som er kommentert ut i CashFlowController?
        config = WireMockConfiguration.wireMockConfig().port(8099);
        wireMockServer = new WireMockServer(config.portNumber());
        wireMockServer.start();
        WireMock.configureFor("localhost", config.portNumber());
        access = new RemoteAccess(new URI("http://localhost:" + wireMockServer.port() + "/user/"));
        setUpForStubs();
    }

    private void setUpForStubs() {
        stubFor(get(urlEqualTo("/user/"))
            .withHeader("Accept", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"name\":\"nameB\",\"userID\":654321,\"accounts\":[{\"type\":\"savings\",\"name\":\"acA\",\"balance\":200.0,\"accountNumber\":5555},{\"type\":\"bsu\",\"name\":\"acB\",\"balance\":100.0,\"accountNumber\":1234}]}")
            )
        );
        stubFor(get(urlEqualTo("/user/5555"))
            .withHeader("Accept", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"type\":\"savings\",\"name\":\"acA\",\"balance\":200.0,\"accountNumber\":5555}")
            )
        );
        stubFor(get(urlEqualTo("/user/1234"))
            .withHeader("Accept", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"type\":\"bsu\",\"name\":\"acB\",\"balance\":100.0,\"accountNumber\":1234}")
            )
        );
        stubFor(delete("/user/1234")
            .withHeader("Accept", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("true")
            )
        );
        stubFor(put("/user/9999")
            .withHeader("Accept", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("true")
            )
        );

    }

    @Test
    public void testGetUser() {
        User user = access.getUser();
        assertEquals(654321, user.getUserID());
    }

    @Test
    public void testGetTwoAccounts() {
        AbstractAccount account = access.getAccount(5555);
        AbstractAccount account1 = access.getAccount(1234);
        assertEquals(5555, account.getAccountNumber());
        assertEquals(1234, account1.getAccountNumber());
    }

    @Test
    public void testDeleteAccount() {
        access.getUser();
        access.deleteAccount(1234);
        Collection<Integer> accountNumbers = access.getUser().getAccountNumbers();
        assertTrue(accountNumbers.size()==1);
        assertTrue(accountNumbers.containsAll(List.of(5555)));
    }

    @Test
    public void testPuttingAccount() {
        access.getUser();
        AbstractAccount account = new CheckingAccount("check", 100, 9999, null);
        access.addAccount(account);
        assertNotNull(access.getAccount(9999));
    }

    @AfterEach
    public void stopWireMockServer() {
      wireMockServer.stop();
    }

}
