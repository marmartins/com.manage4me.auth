package com.manage4me.route.account;

import com.manage4me.route.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<User, String> {

    User findByEmail(String email);

}
