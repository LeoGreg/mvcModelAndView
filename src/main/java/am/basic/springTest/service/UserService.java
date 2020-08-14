package am.basic.springTest.service;


import am.basic.springTest.model.User;
import am.basic.springTest.model.exceptions.*;

import java.util.List;

public interface UserService {


//    void findUsers();

    //    @Override
//    public void findUsers() {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
//        Root<User> root = criteriaQuery.from(User.class);
//        criteriaQuery.select(root);
//        List<User> resultList = entityManager.createQuery(criteriaQuery).getResultList();
//        resultList.forEach(System.out::println);
//        criteriaQuery.where(criteriaBuilder.equal(root.get(User.class.getName()), "IT"));
//        List<User> resultList2 = entityManager.createQuery(criteriaQuery).getResultList();
//        resultList2.forEach(System.out::println);
//
//    }
//    List<User> findUsers();

    void register(User user) throws DuplicateDataException;

    User login(String username, String password) throws NotFoundException, UnverifiedException;

    User changePassword(String username, String password, String newPassword, String c_pass) throws NotFoundException, AccessDeniedException, NotMatchingPasswordsException;

    void sendCode(String username) throws NotFoundException;

    void recoverPassword(String username, String code, String password) throws NotFoundException, AccessDeniedException;

    void verify(String username, String code) throws NotFoundException, AccessDeniedException;

}
