package com.progettoTAASS.user.repository;

import com.progettoTAASS.user.enums.BookUserTypesEnum;
import com.progettoTAASS.user.model.BookUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookUserRepository extends CrudRepository<BookUser, Integer> {
    List<BookUser> findBookUserByUserIdAndType(int id, BookUserTypesEnum type);
}
