package service;

import model.Person;
import repository.BookRepository;
import repository.UserRepository;
import model.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    public UserService() {
        UserRepository.userRepository();
    }

    public Optional<Person> login(String userName, String password){
        return UserRepository.login(userName, password);
    }

    public Optional<Person> register(String name, String userName, String password){
        if(getUser(userName).isPresent()){
            System.out.println("Username taken or too long");
            return Optional.empty();
        }
        if (!(validateName(name) && validatePassword(password) && validateUserName(userName))){
            return Optional.empty();
        }
        addUser(name, password, userName);
        return getUser(userName);
    }

    private boolean validateName(String name){
        if (name == null){
            return false;
        }
        return (name.matches("^[a-zA-Z]+\\s[a-zA-Z]+$") || name.matches("^[a-zA-Z]+\\s[a-zA-Z]+\\s[a-zA-Z]+$"));
    }

    private boolean validatePassword(String password){
        if (password == null){
            return false;
        }

        // Check if the string contains at least 8 and at most 24 characters
        if (password.length() < 8 || password.length() > 24) {
            System.out.println("password must be between 8 and 24 characters");
            return false;
        }

        // Check if the string contains at least one capital letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // Check if the string contains at least one number
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Check if the string contains at least one special character
        return password.matches(".*[^a-zA-Z0-9].*");
    }

    private boolean validateUserName(String userName){
        if (userName == null){
            return false;
        }
        return userName.length() >= 8 && userName.length() <= 24;
    }
    public Optional<Person> getUser(String userName){  // can be private
        return UserRepository.getUser(userName);
    }
    public List<Person> getListOfUsers(){
        return UserRepository.getPersonList();
    }

    public void addUser(String name, String password, String userName){
        User user = new User(name, password, userName);
        UserRepository.addUser(user);
    }

    public boolean userExists(String userName){
        return UserRepository.userExists(userName);
    }

    public void removeUser(String userName){
        UserRepository.removeUser(userName);
    }
}
