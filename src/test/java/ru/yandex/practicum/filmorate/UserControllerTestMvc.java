package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/*
UserControllerTestMvc
User person = new User();
        person.setEmail("test@test.ru");
        person.setLogin("test");
*/


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTestMvc {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Проверка доступности базового урла
    @Test
    public void getAllUsersTest() throws Exception {
        mvc.perform( MockMvcRequestBuilders
                        .get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200));
    }

    // Проверка ответа, если нет объекта поиска
    @Test
    public void getUser1() throws Exception {
        mvc.perform( MockMvcRequestBuilders
                        .get("/users/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));
    }

    // Проверка создания пользователя
    @Test
    public void createUserTest() throws Exception   {
        User newUser = new User();
        newUser.setEmail("test@test.ru");
        newUser.setLogin("test");

        mvc.perform( MockMvcRequestBuilders
                        .post("/users")
                        .content(asJsonString(newUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())        ;

        mvc.perform( MockMvcRequestBuilders
                        .get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@test.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("test"));
    }


    // Проверка валидации email
    @Test
    public void createUserValidEmailTest() throws Exception   {
        User newUserValid = new User();
        newUserValid.setEmail("test test.ru");
        newUserValid.setLogin("test");

        mvc.perform( MockMvcRequestBuilders
                        .post("/users")
                        .content(asJsonString(newUserValid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }



}