package integrationTest;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import softLaunch.SoftLaunchMain;
import softLaunch.repository.ClientRepository;
import softLaunch.service.client.Client;
import softLaunch.service.whitelist.RequestWrapper;
import softLaunch.service.whitelist.WhiteList;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SoftLaunchMain.class)
@AutoConfigureMockMvc
@Transactional
public class ClientEndpointTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ClientRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void listClientsShouldReturnStatusCode200(){
        List<Client> clients = asList(new Client(1L,"Cliente","10"));
        BDDMockito.when(repository.findAll()).thenReturn(clients);
        ResponseEntity<String> response = restTemplate.getForEntity("/client/", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void createClientShouldReturnStatusCode201() {
        List<WhiteList> list = new ArrayList<>();
        list.add(new WhiteList("name","cpf"));
        RequestWrapper whiteList = new RequestWrapper(list);
        ResponseEntity<RequestWrapper> responseEntity =
                restTemplate.postForEntity("/client/batch", whiteList, RequestWrapper.class);
        RequestWrapper wrapper = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(whiteList.getWhiteLists().size(),wrapper.getWhiteLists().size());
    }
}
