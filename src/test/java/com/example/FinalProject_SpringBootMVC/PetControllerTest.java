package com.example.FinalProject_SpringBootMVC;

import com.example.FinalProject_SpringBootMVC.model.Pet;
import com.example.FinalProject_SpringBootMVC.model.User;
import com.example.FinalProject_SpringBootMVC.service.PetService;
import com.example.FinalProject_SpringBootMVC.service.UserPetService;
import com.example.FinalProject_SpringBootMVC.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPetService userPetService;

    User createUserForTestPetController() {
        var user = new User(
                null,
                "Olga",
                "shakina@mail.com",
                19,
                new ArrayList<>()
        );
        return userService.createUser(user);
    }

    @Test
    void shouldSuccessCreatePet() throws Exception {
        var pet = new Pet(
                null,
                "Шарик",
                createUserForTestPetController().getId()
        );

        String petJson = objectMapper.writeValueAsString(pet);

        String createdPetJson = mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson)
        )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pet createdPetResponse = objectMapper.readValue(createdPetJson, Pet.class);

        Assertions.assertEquals(pet.getName(), createdPetResponse.getName());
        Assertions.assertNotNull(createdPetResponse.getId());
    }

    @Test
    void shouldNotCreatePetWhenRequestNotValid() throws Exception {
        var pet = new Pet(
                null,
                null,
                createUserForTestPetController().getId()
        );

        String petJson = objectMapper.writeValueAsString(pet);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("Ошибка валидации данных"))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }

    @Test
    void shouldNotCreatePetWhenRequestNotValid2() throws Exception {
        var pet = new Pet(
                null,
                "Шарик",
                -4L
        );

        String petJson = objectMapper.writeValueAsString(pet);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("Id питомца не может быть отрицательным!"))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldSuccessSearchPetId() throws Exception {
        var pet = new Pet(
                null,
                "Шарик",
                createUserForTestPetController().getId()
        );

        pet = petService.createPet(pet);

        String foundPetJson = mockMvc.perform(get("/api/pets/{id}", pet.getId()))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pet petResponse = objectMapper.readValue(foundPetJson, Pet.class);

        Assertions.assertEquals(pet.getName(), petResponse.getName());
        Assertions.assertEquals(pet.getId(), petResponse.getId());
    }

    @Test
    void shouldReturnNotFoundWhenPetNotPresent() throws Exception {
        mockMvc.perform(get("/api/pets/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value("Pet с ID %s не найден".formatted(Integer.MAX_VALUE)))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDeleted() throws Exception {
        User user = createUserForTestPetController();
        var pet = new Pet(
                null,
                "Бобик",
                user.getId()
        );
        pet = petService.createPet(pet);
        userPetService.deleteUserWithPets(user.getId());
        mockMvc.perform(get("/api/pets/{id}", pet.getId()))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value("Pet с ID %s не найден".formatted(pet.getId())))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void shouldSuccessUpdatePetId() throws Exception {
        User user = createUserForTestPetController();

        var firstPet = new Pet(
                null,
                "Шарик",
                user.getId()
        );
        firstPet = petService.createPet(firstPet);

        var secondPet = new Pet(
                null,
                "Бобик",
                user.getId()
        );

        String secondPetJson = objectMapper.writeValueAsString(secondPet);

        String updatedPetJson = mockMvc.perform(put("/api/pets/{id}", firstPet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondPetJson)
        )
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pet updatedPet = objectMapper.readValue(updatedPetJson, Pet.class);

        Assertions.assertEquals(secondPet.getName(), updatedPet.getName());
        Assertions.assertTrue(firstPet.getName() != updatedPet.getName());
    }

    @Test
    void shouldSuccessDeletePetId() throws Exception {
        var pet = new Pet(
                null,
                "Шарик",
                createUserForTestPetController().getId()
        );
        pet = petService.createPet(pet);

        mockMvc.perform(delete("/api/pets/{id}", pet.getId()))
                .andExpect(status().is(204));
    }

    @Test
    void shouldReturnNotFoundWhenDeletePetIdNotPresent() throws Exception {
        mockMvc.perform(delete("/api/pets/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value("Pet с ID %s не найден".formatted(Integer.MAX_VALUE)))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }
}