package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.*;
import ru.yandex.practicum.filmorate.model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
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

        String stringJson = "{\"email\": \"test1@test.ru\",\"login\": \"test1\",\"name\": \"test1\",\"birthday\": \"2002-06-28\"}";

        mvc.perform( MockMvcRequestBuilders
                        .post("/users")
                        .content(stringJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform( MockMvcRequestBuilders
                        .get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test1@test.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("test1"));

    }


    // Проверка валидации email
    @Test
    public void createUserValidEmailTest() throws Exception   {
        String stringJson = "{\"login\":\"test\",\"email\": \"test test.ru\"}";

        mvc.perform( MockMvcRequestBuilders
                        .post("/users")
                        .content(stringJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }



}