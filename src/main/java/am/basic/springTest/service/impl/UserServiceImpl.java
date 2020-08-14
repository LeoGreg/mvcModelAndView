package am.basic.springTest.service.impl;


import am.basic.springTest.model.User;
import am.basic.springTest.model.exceptions.*;
import am.basic.springTest.repository.UserRepository;
import am.basic.springTest.service.UserService;
import am.basic.springTest.util.MailSenderClient;
import am.basic.springTest.util.encoder.Generator;
import am.basic.springTest.util.encoder.Md5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

import static am.basic.springTest.util.constants.Messages.*;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSenderClient mailSenderClient;

//    @Autowired
//    private EntityManager entityManager;

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
//    @Override
//    public List<User> findUsers() {
//
//        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//
//        CriteriaQuery<User> criteria = builder.createQuery(User.class);
//
//        Root<User> root = criteria.from(User.class);
//
//        criteria.where(
//                builder.equal(root.get("name"), "Grigor")
//        );
//
//        List<User> topics = entityManager
//                .createQuery(criteria)
//                .getResultList();
//
//        return topics;
//    }

    @Override
    @Transactional
    public void register(User user) throws DuplicateDataException {
        User duplicate = userRepository.getByUsername(user.getUsername());
        DuplicateDataException.check(duplicate != null, DUPLICATE_USER_MESSAGE);
        user.setPassword(Md5Encoder.encode(user.getPassword()));
        user.setCode(Generator.getRandomDigits(5));
        user.setStatus(0);
        userRepository.save(user);
        try {
            mailSenderClient.sendSimpleMessage(user.getUsername(), "Verification", "Your code is " + user.getCode());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


    @Override
    public User login(String username, String password) throws NotFoundException, UnverifiedException {
        User user = userRepository.getByUsername(username);
        NotFoundException.check(user == null || !Md5Encoder.matches(password, user.getPassword()), INVALID_CREDENTIALS_MESSAGE);
        UnverifiedException.check(user.getStatus() != 1, UNVERIFIED_MESSAGE);
        return user;

    }

    @Override
    public User changePassword(String username, String password, String newPassword,String confirmingPassword) throws NotFoundException, AccessDeniedException, NotMatchingPasswordsException {
        User user = userRepository.getByUsername(username);
        NotFoundException.check(user == null, USER_NOT_EXIST_MESSAGE);
        AccessDeniedException.check(!user.getPassword().equals(Md5Encoder.encode(password)), WRONG_PASSWORD_MESSAGE);
        NotMatchingPasswordsException.check(!newPassword.equals(confirmingPassword),NEW_OLD_PASSWORD_NOT_MATCH);
        user.setPassword(Md5Encoder.encode(newPassword));
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public void sendCode(String username) throws NotFoundException {
        User user = userRepository.getByUsername(username);
        NotFoundException.check(user == null, USER_NOT_EXIST_MESSAGE);
        user.setCode(Generator.getRandomDigits(5));
        userRepository.save(user);
        try {
            mailSenderClient.sendSimpleMessage(user.getUsername(), "ACCESS CODE", "Your code is " + user.getCode());

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void recoverPassword(String username, String code, String password) throws NotFoundException, AccessDeniedException {
        User user = userRepository.getByUsername(username);
        NotFoundException.check(user == null, USER_NOT_EXIST_MESSAGE);
        AccessDeniedException.check(!user.getCode().equals(code), WRONG_CODE_MESSAGE);
        user.setPassword(Md5Encoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void verify(String username, String code) throws NotFoundException, AccessDeniedException {
        User user = userRepository.getByUsername(username);
        NotFoundException.check(user == null, USER_NOT_EXIST_MESSAGE);
        AccessDeniedException.check(!user.getCode().equals(code), WRONG_CODE_MESSAGE);
        user.setStatus(1);
        userRepository.save(user);
    }

}
