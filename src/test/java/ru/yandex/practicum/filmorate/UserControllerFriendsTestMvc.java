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
import ru.yandex.practicum.filmorate.model.User;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
public class UserControllerFriendsTestMvc {
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


    // Проверка добавления в друзья
    @Test
    public void UsersTest() throws Exception   {

        // Добавляем трех пользователей
        String stringJson = "{\"email\": \"test@test.ru\",\"login\": \"test\",\"name\": \"test1\",\"birthday\": \"2002-06-28\"}";
        mvc.perform( MockMvcRequestBuilders
                .post("/users")
                .content(stringJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());


        String stringJson2 = "{\"email\": \"test2@test.ru\",\"login\": \"test2\",\"name\": \"test2\",\"birthday\": \"2002-06-28\"}";

        mvc.perform( MockMvcRequestBuilders
                        .post("/users")
                        .content(stringJson2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String stringJson3 = "{\"email\": \"test3@test.ru\",\"login\": \"test3\",\"name\": \"test3\",\"birthday\": \"2002-06-28\"}";

        mvc.perform( MockMvcRequestBuilders
                        .post("/users")
                        .content(stringJson3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Проверяем, что нет друзей
        mvc.perform( MockMvcRequestBuilders
                        .get("/users/1/friends")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(0)));

        // Добвляем не существующего друга
        mvc.perform( MockMvcRequestBuilders
                        .put("/users/1/friends/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));

        // Добавляем существующих друзей
        mvc.perform( MockMvcRequestBuilders
                        .put("/users/1/friends/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform( MockMvcRequestBuilders
                        .put("/users/1/friends/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Проверяем список друзей
        mvc.perform( MockMvcRequestBuilders
                        .get("/users/1/friends")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)));
    }



}