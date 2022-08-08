package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
/*
UserControllerTestMvc
User person = new User();
        person.setEmail("test@test.ru");
        person.setLogin("test");
*/


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerDeleteFriendTestMvc {
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

    // Проверка удаления друзей
    @Test
    public void UsersTest() throws Exception   {

        // Добавляем двух пользователей
        User newUser01 = new User();
        newUser01.setEmail("test@test.ru");
        newUser01.setLogin("test");

        mvc.perform( MockMvcRequestBuilders
                        .post("/users")
                        .content(asJsonString(newUser01))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        User newUser02 = new User();
        newUser02.setEmail("test2@test.ru");
        newUser02.setLogin("test2");

        mvc.perform( MockMvcRequestBuilders
                        .post("/users")
                        .content(asJsonString(newUser02))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // удаляем не существующего друга
        mvc.perform( MockMvcRequestBuilders
                        .delete("/users/1/friends/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));

        // Добавляем существующего друга
        mvc.perform( MockMvcRequestBuilders
                        .put("/users/1/friends/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // удаляем существующего друга
        mvc.perform( MockMvcRequestBuilders
                        .delete("/users/1/friends/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200));

        // Проверяем список друзей
        mvc.perform( MockMvcRequestBuilders
                        .get("/users/1/friends")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(0)));
    }




}