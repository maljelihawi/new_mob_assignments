import com.detroitlabs.skateboard.NewMobAssignmentsApplication;
import com.detroitlabs.skateboard.controller.SkateBoardController;
import com.detroitlabs.skateboard.model.SkateBoard;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RunWith(value = SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = NewMobAssignmentsApplication.class)
public class SkateBoardTest {

    ArrayList<SkateBoard> skateBoards = new ArrayList<>();

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private SkateBoardController skateBoardController;

    private MockMvc mockMvc;

    @Before
    public void setup() {

        skateBoards.add(SkateBoard.builder()
                .id(1)
                .ownerName("Rohan Dongre")
                .brand("Adidas")
                .weight(40)
                .length(20)
                .isBoardAvailable(true)
                .build());
        skateBoards.add(SkateBoard.builder()
                .id(2)
                .ownerName("John Hive")
                .brand("Nike")
                .weight(50)
                .length(22)
                .isBoardAvailable(true)
                .build());
        skateBoards.add(SkateBoard.builder()
                .id(3)
                .ownerName("Brittany Spear")
                .brand("Puma")
                .weight(48)
                .length(24)
                .isBoardAvailable(true)
                .build());

        mockMvc = MockMvcBuilders.standaloneSetup(this.skateBoardController).build();
    }

    @Test
    public void createSkateBoard_returnsCreatedSuccessfullyCode_WhenIdIsSet() throws Exception {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/v1/skateboard/";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        SkateBoard createSkateBoard = SkateBoard.builder()
                .id(4)
                .ownerName("Rohan Dongre")
                .brand("Adidas")
                .weight(60)
                .length(20)
                .isBoardAvailable(true)
                .build();

        HttpEntity<SkateBoard> request = new HttpEntity<>(createSkateBoard, headers);
        ResponseEntity actualResponse = this.restTemplate.postForEntity(uri, request, String.class);

        //Verify request succeed
        Assert.assertEquals(201, actualResponse.getStatusCodeValue());
    }

    @Test
    public void getSkateBoard_ReturnsSuccessfulId_IfFound() throws Exception {
        SkateBoard skateBoard1 = skateBoardController.retrieveSkateBoard(1);
        Assert.assertEquals(1, skateBoard1.getId().intValue());

        SkateBoard skateBoard2 = skateBoardController.retrieveSkateBoard(2);
        Assert.assertEquals(2, skateBoard2.getId().intValue());

        //when value not present
        SkateBoard skateBoard3 = skateBoardController.retrieveSkateBoard(3);
        Assert.assertNotEquals(1, skateBoard3.getId().intValue());
    }

    @Test
    public void getAvailableBoards_returnsAvailableBoards() throws Exception {
        List<SkateBoard> boards = skateBoardController.retrieveAllUsers();
        Assert.assertEquals(3, boards.size());
    }

    @Test
    public void getSkateBoard_ReturnsSuccessfulLength_IfFound() throws Exception {
        SkateBoard skateBoard1 = skateBoardController.retrieveSkateBoardByLength(20);
        Assert.assertEquals(20, skateBoard1.getLength());

        SkateBoard skateBoard2 = skateBoardController.retrieveSkateBoardByLength(22);
        Assert.assertEquals(22, skateBoard2.getLength());

        SkateBoard skateBoard3 = skateBoardController.retrieveSkateBoardByLength(24);
        Assert.assertNotEquals(21, skateBoard3.getLength());
    }

    @Test
    public void getSkateBoard_ReturnsSuccessfulWeight_IfFound() throws Exception {
        SkateBoard skateBoard1 = skateBoardController.retrieveSkateBoardByWeight(40);
        Assert.assertEquals(40, skateBoard1.getWeight());

        SkateBoard skateBoard2 = skateBoardController.retrieveSkateBoardByWeight(50);
        Assert.assertEquals(50, skateBoard2.getWeight());

        SkateBoard skateBoard3 = skateBoardController.retrieveSkateBoardByWeight(48);
        Assert.assertNotEquals(22, skateBoard3.getWeight());
    }

    @After
    public void tearDown() {
        skateBoards.clear();
    }

}
