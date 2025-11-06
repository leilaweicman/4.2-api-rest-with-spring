package cat.itacademy.s04.s02.n01.fruit.controllers;

import cat.itacademy.s04.s02.n01.fruit.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.repository.FruitRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FruitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FruitRepository fruitRepository;

    @BeforeEach
    void cleanDatabase() {
        fruitRepository.deleteAll();
    }

    @Test
    void createFruit_returnsCreatedFruit() throws Exception {
        Fruit fruit = new Fruit("Apple", 10);

        mockMvc.perform(post("/fruits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.weight").value(10));
    }

    @Test
    void createFruit_returnsBadRequest_whenNameIsBlank() throws Exception {
        Fruit fruit = new Fruit("", 10);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFruit_returnsBadRequest_whenWeightInvalid() throws Exception {
        Fruit fruit = new Fruit("Apple", -5);
        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllFruits_returnsEmptyListInitially() throws Exception {
        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAllFruits_returnsListOfFruits_whenFruitsExist() throws Exception {
        Fruit apple = new Fruit("Apple", 10);
        Fruit banana = new Fruit("Banana", 5);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apple)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(banana)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].name").value("Banana"));
    }

    @Test
    void getFruitById_returnsFruit_whenExists() throws Exception {
        Fruit fruit = new Fruit("Banana", 5);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/fruits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Banana"))
                .andExpect(jsonPath("$.weight").value(5));
    }

    @Test
    void getFruitById_returnsNotFound_whenMissing() throws Exception {
        mockMvc.perform(get("/fruits/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateFruit_returnsUpdatedFruit_whenExists()  throws Exception {
        Fruit apple = new Fruit("Apple", 10);
        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apple)))
                .andExpect(status().isCreated());


        Fruit banana = new Fruit("Banana", 5);
        mockMvc.perform(put("/fruits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(banana)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Banana"))
                .andExpect(jsonPath("$.weight").value(5));
    }

    @Test
    void updateFruit_returnsNotFound_whenMissing() throws Exception {
        Fruit apple = new Fruit("Apple", 10);
        mockMvc.perform(put("/fruits/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apple)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateFruit_returnsBadRequest_whenInvalidData() throws Exception {
        Fruit apple = new Fruit("Apple", 10);
        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apple)))
                .andExpect(status().isCreated());

        Fruit fruit = new Fruit("", -10);
        mockMvc.perform(put("/fruits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isBadRequest());
    }
}
