package wmi.students.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wmi.students.exceptions.BadRequestException;
import wmi.students.exceptions.ConflictException;
import wmi.students.exceptions.NotFoundException;
import wmi.students.model.User;
import wmi.students.service.UserServiceImpl;

import java.util.List;

/**
 * Created by Grzegorz on 2016-04-20.
 */
@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @RequestMapping(method = RequestMethod.DELETE, value = "/users/{userId}")
    public ResponseEntity deleteBook(@PathVariable("userId") Long userId) throws NotFoundException {
        if (userServiceImpl.findById(userId) == null){
            throw new NotFoundException("Użytkownik o podanym id nie istnieje");
        }
        userServiceImpl.delete(userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity addUser(@RequestBody User user) throws BadRequestException, ConflictException {
        if(user.getEmail() == null){
            throw new BadRequestException("Nie podałeś maila(mail)");
        }
        if(user.getLogin() == null){
            throw new BadRequestException("Nie podałeś loginu(login)");
        }
        if(userServiceImpl.findByEmail(user.getEmail()) != null){
            throw  new ConflictException("Podany email już istnieje");
        }
        if(userServiceImpl.findByLogin(user.getLogin()) != null){
            throw  new ConflictException("Podany login już istnieje");
        }
        userServiceImpl.addUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> findUsers() throws NotFoundException {
        if(userServiceImpl.findAll() == null){
            throw new NotFoundException("Nie ma użytkowników w bazie");
        }
        return userServiceImpl.findAll();
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public User findUsersById(@PathVariable Long userId) throws NotFoundException {
        User user = userServiceImpl.findById(userId);
        if(user == null){
            throw new NotFoundException("Użytkownik o podanym id nie istnieje");
        }
        return user;
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
    public ResponseEntity updateUser(@PathVariable("userId") Long userId, @RequestBody User user) throws NotFoundException {
        User userToUpdate = userServiceImpl.findById(userId);
        if(userToUpdate == null){
            throw new NotFoundException("Użytkownik o podanym id nie istnieje");
        }
        String login = user.getLogin();
        String email = user.getEmail();
        String password = user.getPassword();
        userToUpdate.setEmail(email);
        userToUpdate.setPassword(password);
        userToUpdate.setLogin(login);
        userServiceImpl.save(userToUpdate);
        return new ResponseEntity(HttpStatus.OK);
    }
}
