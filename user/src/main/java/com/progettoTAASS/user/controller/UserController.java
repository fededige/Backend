package com.progettoTAASS.user.controller;

import com.progettoTAASS.user.enums.BookUserTypesEnum;
import com.progettoTAASS.user.model.Book;
import com.progettoTAASS.user.model.BookUser;
import com.progettoTAASS.user.model.User;
import com.progettoTAASS.user.repository.BookUserRepository;
import com.progettoTAASS.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookUserRepository bookUserRepository;

    @GetMapping("/{id}")
    public User getUserInfos(@PathVariable int id) {
        User currentUser = userRepository.findDistinctFirstById(id);

        return currentUser;
    }

    @GetMapping("/{id}/coins")
    public int getUserCoins(@PathVariable int id) {
        User currentUser = userRepository.findDistinctFirstById(id);

        return currentUser.getWallet().getCoins();
    }

    @GetMapping("/{id}/read")
    private List<BookUser> getBooksRead(@PathVariable int id) {
        List<BookUser> listBookRead = bookUserRepository.findBookUserByUserIdAndType(id, BookUserTypesEnum.LETTO);

        return listBookRead;
    }

    @GetMapping("/{id}/loan")
    private List<BookUser> getBooksLoan(@PathVariable int id) {
        List<BookUser> listBookLoan = bookUserRepository.findBookUserByUserIdAndType(id, BookUserTypesEnum.PRESTATO);

        return listBookLoan;
    }
}
