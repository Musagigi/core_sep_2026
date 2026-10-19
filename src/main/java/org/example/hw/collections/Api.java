package org.example.hw.collections;

import org.example.hw.collections.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

//На этапе старта программы считайте пользователей  и создайте
//коллекции которые будут использованы для вызовов методов
//выбирайте коллекции так чтобы время на вызов было минимальным.
//каждому методу ставьте комментарий со сложностью O(?)
public class Api {

    private static final String DATA_URL = "https://gist.githubusercontent.com/isicju/f40e58017d64533a2660cf04285f6cb9/raw/5fde264d0758aab1df4afe92f33bd83ad3d7b9ad/gistfile1.txt";
    private static List<User> userListWithoutSorting;
    private static List<User> userListWithSorting;

    public static void main(String[] args) {

        userListWithoutSorting = UserLoader.load(DATA_URL);
        userListWithSorting = userListWithoutSorting.stream().sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());

        // User testUser =
        // User.builder().id(999L).name("test").birthDate(LocalDate.of(2000, 2, 5))
        // .city("testCity")
        // .address("tesAddres")
        // .email("test@yahoo.com")
        // .phone("99990099")
        // .build();

        // System.out.println(getUserByIndexWithoutSorting(0));
        // System.out.println(getUsersSortedByName());
        // System.out.println(getUsersByBirthDate(LocalDate.of(2017, 2, 5)));
        // System.out.println(getAllUsersOlderThan(LocalDate.of(2017, 2, 5)));
        // System.out.println(getAllUsersBetweenBirthDates(LocalDate.of(2017, 2, 5),
        // LocalDate.of(2017, 2, 20)));
        // System.out.println(getUserSortedByIndex(0));
        // System.out.println(getUsersByTheYear(2017));
        // System.out.println(getUsersByMonth(0));

        // System.out.println(addUserWithoutMapNoDOBDuplicates(testUser));
        // System.out.println(userListWithoutSorting.getFirst());
        // System.out.println(userListWithoutSorting.getLast());

        // System.out.println(addUserWithoutMapNoEmailDuplicates(testUser));
        // System.out.println(userListWithoutSorting.getFirst());
        // System.out.println(userListWithoutSorting.getLast());

        // System.out.println(getAllNamesWithoutDuplicates());
    }

    // O(1)
    public static User getUserByIndexWithoutSorting(int position) {
        return userListWithoutSorting.get(position);
    }

    // O(n)
    public static Collection<User> getUsersSortedByName() {
        return userListWithoutSorting.stream().sorted(Comparator.comparing((User::getName)))
                .collect(Collectors.toList());
    }

    // O(n)
    public static Collection<User> getUsersByBirthDate(LocalDate birthDate) {
        return userListWithoutSorting.stream().filter(user -> user.getBirthDate().equals(birthDate))
                .collect(Collectors.toList());
    }

    // O(n)
    public static Collection<User> getAllUsersOlderThan(LocalDate birthDate) {
        return userListWithoutSorting.stream().filter(user -> user.getBirthDate().isBefore(birthDate))
                .collect(Collectors.toList());
    }

    // O(n)
    public static Collection<User> getAllUsersBetweenBirthDates(LocalDate birthDateFrom, LocalDate birthDateTo) {
        return userListWithoutSorting.stream().filter(
                user -> !user.getBirthDate().isBefore(birthDateFrom) && !user.getBirthDate().isAfter(birthDateTo))
                .collect(Collectors.toList());
    }

    // O(1)
    public static User getUserSortedByIndex(int position) {
        return userListWithSorting.get(position);
    }

    // O(n)
    public static Collection<User> getUsersByTheYear(int year) {
        return userListWithoutSorting.stream().filter(user -> user.getBirthDate().getYear() == year)
                .collect(Collectors.toList());
    }

    // O(n)
    // month can be 0,1,2..,11
    public static Collection<User> getUsersByMonth(int month) {
        int normalMonth = month + 1;

        return userListWithoutSorting.stream().filter(user -> user.getBirthDate().getMonthValue() == normalMonth)
                .collect(Collectors.toList());
    }

    // O(n)
    // add to collection but replace users with the same date of birth (no date of
    // birth duplicates)
    // replace existent if existed, don't use maps
    public static User addUserWithoutMapNoDOBDuplicates(User user) {
        int index = -1;

        for (int i = 0; i < userListWithoutSorting.size(); i++) {
            boolean isExist = userListWithoutSorting.get(i).getBirthDate().equals(user.getBirthDate());

            if (isExist) {

                index = i;
                break;
            }
        }

        if (index >= 0) {
            userListWithoutSorting.set(index, user);
        }

        if (index == -1) {
            userListWithoutSorting.add(user);
        }

        return user;
    }

    // O(n)
    // add to collection but replace users with the same email (no email duplicates)
    // don't use maps
    public static User addUserWithoutMapNoEmailDuplicates(User user) {
        int index = -1;

        for (int i = 0; i < userListWithoutSorting.size(); i++) {
            boolean isExist = userListWithoutSorting.get(i).getEmail().equals(user.getEmail());

            if (isExist) {

                index = i;
                break;
            }
        }

        if (index >= 0) {
            userListWithoutSorting.set(index, user);
        }

        if (index == -1) {
            userListWithoutSorting.add(user);
        }

        return user;
    }

    // O(n)
    // collection should be as small as possible
    public static User deleteUserById(long userId) {
        for (int i = 0; i < userListWithoutSorting.size(); i++) {
            if (userListWithoutSorting.get(i).getId() == userId) {
                return userListWithoutSorting.remove(i);
            }
        }
        return null;
    }

    // O(n)
    public static User getUserById(long userId) {
        for (int i = 0; i < userListWithoutSorting.size(); i++) {
            if (userListWithoutSorting.get(i).getId() == userId) {
                return userListWithoutSorting.get(i);
            }
        }
        return null;
    }

    // O(n)
    // must be sorted by userId
    public static Collection<User> getSubCollectionId(long userIdFrom, long userIdTo) {
        return userListWithSorting.stream().filter(user -> user.getId() >= userIdFrom && user.getId() <= userIdTo)
                .collect(Collectors.toList());
    }

    // O(n)
    public static Collection<String> getAllCitiesWithoutDuplicates() {
        return userListWithoutSorting.stream().map(User::getCity).collect(Collectors.toSet());
    }

    // O(n)
    public static Collection<String> getAllNamesWithoutDuplicates() {
        return userListWithoutSorting.stream().map(User::getName).collect(Collectors.toSet());
    }

    // O(n)
    public static Collection<User> getUsersByName(String name) {
        return userListWithoutSorting.stream().filter(user -> user.getName().equals(name)).collect(Collectors.toList());
    }

    // O(n)
    public static Collection<String> getAllAddressesSortedByNaturalReverseOrder() {
        return userListWithoutSorting.stream().map(User::getAddress).sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    // O(n)
    // поменяйте сигнатуру, нужно вернуть всех пользователей разбитых по городам
    public static Map<String, List<User>> getAllUsersCityWise() {
        return userListWithoutSorting.stream().collect(Collectors.groupingBy(User::getCity));
    }

    // поменяйте сигнатуру, нужно вернуть всех пользователей разбитых с сортировкой
    public Object getAllUsersByCityWiseSorted() {
        return null;
    }

    // Создайте трисет который будет возвращать всех пользователей с сортировкой по
    // имени но
    // при этом пользователи с одинаковым именем не должны быть искючены. при этом
    // нужно помнить
    // что все пользователи имеют уникальный userId и этим можно воспользоваться.
    public static TreeSet<User> getSortedUsersByName() {
        return userListWithoutSorting.stream().collect(Collectors
                .toCollection(() -> new TreeSet<>(Comparator.comparing(User::getName).thenComparing(User::getId))));
    }

    // Верните всех пользователей по 3 символам (идущим вместе) в емейле например
    // vas должен вернуть vasya@gmail.com, 123vaskek@gmail.com
    // проще говорят должен отработать contains(3chars)
    public static Collection<User> getUsersBy3EmailChars(String threeMatchingChars) {
        // threeMatchingChars should be valid for userEmail.contains(threeMatchingChars)
        return userListWithoutSorting.stream().filter(user -> user.getEmail().contains(threeMatchingChars))
                .collect(Collectors.toList());
    }

    // Сигнатура метода не идеальна, хотелось бы чтобы методы ниже (точнее их
    // сигнатуры)
    // были более общны. В зависимости от содержания метода поменяйте сигнатуры
    // чтобы они подходили не только под
    // List интерфейс
    private static <T extends User> void printAllUserNames(Collection<T> usersToBePrinted) {
        for (User user : usersToBePrinted) {
            System.out.println(user.getName());
        }
    }

    private static <T extends User> boolean existentUser(Collection<T> users, User toBeExistent) {
        return users.contains(toBeExistent);
    }

}
