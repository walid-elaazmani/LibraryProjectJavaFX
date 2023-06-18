package repository;

import model.BorrowedBook;
import model.Librarian;
import model.Person;
import model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserRepository {
    private static UserRepository userRepositoryInstance;
    private static List<Person> personList;
    private UserRepository() {
        personList = new ArrayList<>();
        personList.add(new User("Alina Berns", "AlinaB", "AlinaB"));
        personList.add(new User("Walid El Aazmani", "walid123", "walid123"));
        personList.add(new Librarian("Tom", "Tom", "Tom"));
        personList.add(new User("Tom Lamers", "TomL@mers1", "T2L"));
    }
    public static UserRepository userRepository() {
        if (userRepositoryInstance == null) {
            userRepositoryInstance = new UserRepository();
        }
        return userRepositoryInstance;
    }
    public static boolean userExists(String username) {
        return personList.stream().
                anyMatch(a -> a.getUserName().equals(username));
    }
    public static Optional<Person> getUser(String userName){
        return  personList.stream().filter(person -> userName.equals(person.getUserName())).findFirst();
    }
    public static void addUser(User user){
        personList.add(user);
    }
    public static List<Person> getPersonList() {
        return personList;
    }
    public static void removeUser(String userName){
        personList.removeIf(user -> user.getUserName().equals(userName));
    }
    public static Optional<Person> login(String userName, String password){
        return personList.stream()
                .filter(person -> userName.equals(person.getUserName()) && password.equals(person.getPassword()))
                .findFirst();
    }
}
