package com.example.FinalProject_SpringBootMVC;

import com.example.FinalProject_SpringBootMVC.model.User;
import com.example.FinalProject_SpringBootMVC.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private final User user = new User(
            null,
            "Olga",
            "shakina@mail.com",
            19,
            new ArrayList<>()
    );

    @Test
    void shouldSuccessCreateUser() throws Exception {
        String userJson = objectMapper.writeValueAsString(user);

        String createdUserJson = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        User createdUserResponse = objectMapper.readValue(createdUserJson, User.class);

        Assertions.assertEquals(user.getName(), createdUserResponse.getName());
        Assertions.assertNotNull(createdUserResponse.getId());
    }

    @Test
    void shouldNotCreateUserWhenRequestNotValid() throws Exception {
        var user = new User(
                null,
                null,
                "shakina@mail.com",
                19,
                new ArrayList<>()
        );

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("Ошибка валидации данных"))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }

    @Test
    void shouldNotCreateUserWhenRequestNotValid2() throws Exception {
        var user = new User(
                null,
                "Olga",
                "shakina@mail.com",
                3,
                new ArrayList<>()
        );

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("Возраст пользователя не может быть меньше 5!"))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldSuccessSearchUserId() throws Exception {
        User gotUser = userService.createUser(user);

        String foundUserJson = mockMvc.perform(get("/api/users/{id}", gotUser.getId()))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        User userResponse = objectMapper.readValue(foundUserJson, User.class);

        Assertions.assertEquals(gotUser.getName(), userResponse.getName());
        Assertions.assertEquals(gotUser.getId(), userResponse.getId());
    }

    @Test
    void shouldReturnNotFoundWhenUserNotPresent() throws Exception {
        mockMvc.perform(get("/api/users/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value("User с ID %s не найден".formatted(Integer.MAX_VALUE)))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void shouldSuccessUpdateUserId() throws Exception {
        User firstUser = userService.createUser(user);

        var secondUser = new User(
                null,
                "David",
                "shakina54@mail.com",
                21,
                new ArrayList<>()
        );

        String secondUserJson = objectMapper.writeValueAsString(secondUser);

        String updatedUserJson = mockMvc.perform(put("/api/users/{id}", firstUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondUserJson)
        )
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        User updatedUser = objectMapper.readValue(updatedUserJson, User.class);

        Assertions.assertEquals(secondUser.getName(), updatedUser.getName());
        Assertions.assertTrue(firstUser.getName() != updatedUser.getName());
    }

    @Test
    void shouldNotUpdateUserWhenRequestConflict() throws Exception {
        User firstUser = userService.createUser(user);

        var secondUser = new User(
                null,
                "David",
                "shakina@mail.com",
                21,
                new ArrayList<>()
        );

        String secondUserJson = objectMapper.writeValueAsString(secondUser);

        mockMvc.perform(put("/api/users/{id}", firstUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondUserJson)
        )
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.message").value("У двух разных пользователей не может быть одинакового email!"))
                .andExpect(jsonPath("$.errorCode").value("CONFLICT"));
    }

    @Test
    void shouldSuccessDeleteUserId() throws Exception {
        User deletedUser = userService.createUser(user);

        mockMvc.perform(delete("/api/users/{id}", deletedUser.getId()))
                .andExpect(status().is(204));
    }

    @Test
    void shouldReturnNotFoundWhenDeleteUserIdNotPresent() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value("User с ID %s не найден".formatted(Integer.MAX_VALUE)))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }
}