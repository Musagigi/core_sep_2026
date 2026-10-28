package com.kotojava.lesson3.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.kotojava.lesson3.UserLoader;
import com.kotojava.lesson3.model.User;

@Service
public class UserService {
    private static final String DATA_URL = "https://gist.githubusercontent.com/isicju/f40e58017d64533a2660cf04285f6cb9/raw/5fde264d0758aab1df4afe92f33bd83ad3d7b9ad/gistfile1.txt";
    private final List<User> USER_LIST = UserLoader.load(DATA_URL);

    public List<User> getUsers() {
        return USER_LIST;
    }

    public List<User> getUsers(String sort, LocalDate from, LocalDate to, String emailContains) {
        return USER_LIST.stream()
                .filter(u -> from == null || (u.getBirthDate() != null && !u.getBirthDate().isBefore(from)))
                .filter(u -> to == null || (u.getBirthDate() != null && !u.getBirthDate().isAfter(to)))
                .filter(u -> emailContains == null || (u.getEmail() != null && u.getEmail().contains(emailContains)))
                .sorted(getComparator(sort))
                .toList();
    }

    private Comparator<User> getComparator(String sort) {
        if ("name".equals(sort)) {
            return Comparator.comparing(User::getName);
        }
        return Comparator.comparing(User::getId);
    }

    public User getUserById(long userId) {
        return USER_LIST.stream().filter(user -> Objects.equals(user.getId(), userId)).findFirst().orElse(null);
    }
}
