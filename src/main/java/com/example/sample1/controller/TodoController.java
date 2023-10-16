package com.example.sample1.controller;

import com.example.sample1.dao.TodoDao;
import com.example.sample1.domain.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TodoController {
    @Autowired
    private DataSource dataSource;

    public List<Todo> list() throws Exception {
        String sql = "SELECT * FROM todo ORDER BY id DESC";

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        List<Todo> list = new ArrayList<>();
        try (connection; statement;resultSet){
            while (resultSet.next()) {
                Todo todo = new Todo();
                todo.setId(resultSet.getInt("id"));
                todo.setTodo(resultSet.getString("todo"));
                todo.setInserted();
            }
        }
    }

    @GetMapping("/")
    public String home(Model model) {
        // 할 일 리스트 읽고 모델에 넣기
        List<Todo> list = todoDao.list();
        model.addAttribute("todoList", list);

    }

    @PostMapping("/add")
    public String add (Todo todo, RedirectAttributes rttr) {
        // 새 할 일 추가하고
        todoDao.insert(todo);

        // 결과 model에 넣고


        // home 으로 redirect
        return "redirect:/";

    }
}
