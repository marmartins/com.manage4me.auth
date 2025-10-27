package com.manage4me.route.account;

import com.manage4me.route.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<User, String> {

    User findByEmail(String email);

    List<User> findAllByNameContaining(String name);

    List<User> findAllByCompany_IdAndNameContaining(String companyId, String name);

    List<User> findAllByCompany_Id(String companyId);

}
