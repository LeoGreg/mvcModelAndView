package am.basic.springTest.controller.account;

import am.basic.springTest.model.User;
import am.basic.springTest.model.exceptions.AccessDeniedException;
import am.basic.springTest.model.exceptions.DuplicateDataException;
import am.basic.springTest.model.exceptions.NotFoundException;
import am.basic.springTest.model.exceptions.UnverifiedException;
import am.basic.springTest.service.UserService;
import am.basic.springTest.util.CookieUtil;
import am.basic.springTest.util.encoder.Encryptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import static am.basic.springTest.util.constants.Messages.*;
import static am.basic.springTest.util.constants.Pages.*;
import static am.basic.springTest.util.constants.ParameterKeys.*;

@Controller
@Log4j2
public class AccountsController {

    @Autowired
    private UserService userService;



    @GetMapping(path = "/")
    public ModelAndView start(@CookieValue(name = "remember_token", required = false) String rememberToken,
                              HttpSession session, HttpServletResponse response) throws Exception {

        if (rememberToken == null) {
            return new ModelAndView(INDEX_PAGE);
        }

        String token = Encryptor.decrypt(rememberToken);
        String username = token.split(":")[0];
        String password = token.split(":")[1];

        return login(username, password, "ON", session, response);
    }


    @PostMapping(path = "/login")
    public ModelAndView login(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam(required = false, defaultValue = "OFF") String remember,
                              HttpSession session,
                              HttpServletResponse response) throws Exception {

        try {
            User user = userService.login(username, password);
            log.info("<<<user logged in:");
            session.setAttribute(USER_ATTRIBUTE_KEY, user);
            log.info("<<<session is set up with user");
            session.setMaxInactiveInterval(60 * 60 * 60 * 24);
            log.info("<<<set max interval {}", 60 * 60 * 60 * 24+" sec");

            if ("ON".equalsIgnoreCase(remember)) {
                log.info("<<<on is pressed:");
                Cookie cookie = new Cookie(REMEMBER_TOKEN_COOKIE_KEY, Encryptor.encrypt(username + ":" + password));
                cookie.setMaxAge(60 * 60 * 60);
                log.info("<<<cookie max interval is set up :{}", 60 * 60 * 60+" sec");
                response.addCookie(cookie);
                log.info("<<<cookie is added:");
            }
            return new ModelAndView(HOME_PAGE);
        } catch (UnverifiedException e) {
            log.warn(">>>user's registered, but status is set to 0:{}",e);
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView(VERIFICATION_PAGE);
            modelAndView.addObject(MESSAGE_ATTRIBUTE_KEY, e.getMessage());
            modelAndView.addObject(USERNAME_PARAM_KEY, username);
            return modelAndView;
        } catch (NotFoundException e) {
            log.warn(">>>can't find user ,maybe wrong username or password:{}",e);
            return new ModelAndView(INDEX_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (RuntimeException ex) {
            log.error(">>>something went wrong during login process:{}",ex);
            ex.printStackTrace();
            return new ModelAndView(INDEX_PAGE, MESSAGE_ATTRIBUTE_KEY, INTERNAL_ERROR_MESSAGE);
        }

    }


//jdbcGo

    @PostMapping(path = "/register")
    public ModelAndView register(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String name,
                                 @RequestParam String surname,
                                 @RequestParam String g_mail,
                                 @RequestParam(required = false) String dataOfBirth,
                                 @RequestParam String userCountry,
                                 @RequestParam User.Gender userGender) {

        try {
            log.info("<<<in register-method:::");
            User user = new User();
            log.info("<<<user example:");
            user.setGender(userGender.toString());
            log.info("<<<gender is set:");
            user.setCountry(userCountry);
            log.info("<<<country is set:");
            user.setUsername(username);
            log.info("<<<username is set:");
            user.setPassword(password);
            log.info("<<<password is set:");
            user.setName(name);
            log.info("<<<name is set:");
            user.setSurname(surname);
            log.info("<<<surname is set:");
            user.setDataOfBirth(dataOfBirth);
            log.info("<<<data of birth is set:");
            user.setG_mail(g_mail);
            log.info("<<<g_mail is set:");
            userService.register(user);
            log.info("<<<user's registered:");

            ModelAndView modelAndView = new ModelAndView(VERIFICATION_PAGE);
            modelAndView.addObject(USERNAME_PARAM_KEY, username);
            modelAndView.addObject(MESSAGE_ATTRIBUTE_KEY, REGISTRATION_SUCCESS_MESSAGE);
            log.info("<<<added object username ,page verification:");
            return modelAndView;
        } catch (DuplicateDataException ex) {
            log.warn(">>>can't be registered with already used username:");
            log.warn(">>>username:{}", username);
            ex.printStackTrace();
            return new ModelAndView(REGISTER_PAGE, MESSAGE_ATTRIBUTE_KEY, ex.getMessage());
        } catch (RuntimeException ex) {
            log.error(">>>some unexpected error during register:");
            ex.printStackTrace();
            return new ModelAndView(REGISTER_PAGE, MESSAGE_ATTRIBUTE_KEY, ex.getMessage());
        }
    }

    @PostMapping(path = "/forget-password")
    public ModelAndView forgetPassword(@RequestParam String username) {
        try {
            userService.sendCode(username);
            log.info("<<<forget-password-method:::code has been sent:");
            return new ModelAndView(RECOVER_PASSWORD_PAGE, USERNAME_PARAM_KEY, username);
        } catch (NotFoundException e) {
            log.warn(">>>error during forget password ,can't find given username to send code :");
            e.printStackTrace();
            return new ModelAndView(FORGET_PASSWORD_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (RuntimeException ex) {
            log.error(">>>unexpected error during forget-password:");
            ex.printStackTrace();
            return new ModelAndView(FORGET_PASSWORD_PAGE, MESSAGE_ATTRIBUTE_KEY, INTERNAL_ERROR_MESSAGE);
        }
    }


    @PostMapping(path = "/recover-password")
    public ModelAndView recoverPassword(@RequestParam String username,
                                        @RequestParam String password,
                                        @RequestParam String code) {

        try {
            userService.recoverPassword(username, code, password);
            log.info("<<<recover-password-method:::new password's set with code at the same time:");
            return new ModelAndView(INDEX_PAGE, MESSAGE_ATTRIBUTE_KEY, PASSWORD_CHANGE_SUCCESS_MESSAGE);
        } catch (NotFoundException e) {
            log.warn(">>>recover-password page error with finding username it's null:");
            log.warn(">>>username:{}", username);
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView(RECOVER_PASSWORD_PAGE);
            modelAndView.addObject(USERNAME_PARAM_KEY, username);
            modelAndView.addObject(MESSAGE_ATTRIBUTE_KEY, e.getMessage());
            return modelAndView;
        } catch (RuntimeException ex) {
            log.error(">>>unexpected error during recover-password time:{}", code + ":" + username);
            ex.printStackTrace();
            ModelAndView modelAndView = new ModelAndView(RECOVER_PASSWORD_PAGE);
            modelAndView.addObject(USERNAME_PARAM_KEY, username);
            modelAndView.addObject(MESSAGE_ATTRIBUTE_KEY, INTERNAL_ERROR_MESSAGE);
            return modelAndView;
        } catch (AccessDeniedException e) {
            log.warn(">>>in recover-password time user's given code doesn't match db code:");
            log.warn(">>>code by request :{}", code);
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView(RECOVER_PASSWORD_PAGE);
            modelAndView.addObject(USERNAME_PARAM_KEY, username);
            modelAndView.addObject(MESSAGE_ATTRIBUTE_KEY, e.getMessage());
            return modelAndView;
        }
    }


    @PostMapping(path = "/resend")
    public ModelAndView resend(@RequestParam String username) {

        try {
            userService.sendCode(username);
            log.info("<<resend method:::<code 's been sent:");
            ModelAndView modelAndView = new ModelAndView(VERIFICATION_PAGE);
            modelAndView.addObject(USERNAME_PARAM_KEY, username);
            modelAndView.addObject(MESSAGE_ATTRIBUTE_KEY, CODE_SUCCESSFULLY_SEND_MESSAGE);
            return modelAndView;

        } catch (NotFoundException e) {
            log.warn(">>>during resend code process can't find username ,so it's null:");
            log.warn(">>>username:{}", username);
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView(VERIFICATION_PAGE);
            modelAndView.addObject(USERNAME_PARAM_KEY, username);
            modelAndView.addObject(MESSAGE_ATTRIBUTE_KEY, e.getMessage());
            return modelAndView;
        } catch (RuntimeException ex) {
            log.error(">>>some unexpected during resending :");
            ex.printStackTrace();
            ModelAndView modelAndView = new ModelAndView(VERIFICATION_PAGE);
            modelAndView.addObject(USERNAME_PARAM_KEY, username);
            modelAndView.addObject(MESSAGE_ATTRIBUTE_KEY, INTERNAL_ERROR_MESSAGE);
            return modelAndView;
        }
    }

    @PostMapping(path = "/verify")
    public ModelAndView verify(@RequestParam String username, @RequestParam String code) {
        try {
            userService.verify(username, code);
            log.info("<<<verify-method:::just verified user:");
            return new ModelAndView(INDEX_PAGE, MESSAGE_ATTRIBUTE_KEY, VERIFICATION_SUCCESS_MESSAGE);
        } catch (NotFoundException e) {
            log.warn(">>> during verification after register some error about finding user, so it's set to null:");
            log.warn(">>>username:{}", username);
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView(VERIFICATION_PAGE);
            modelAndView.addObject(USERNAME_PARAM_KEY, username);
            modelAndView.addObject(MESSAGE_ATTRIBUTE_KEY, e.getMessage());
            return modelAndView;
        } catch (RuntimeException ex) {
            log.error(">>>some not wanted exception during verification:");
            ex.printStackTrace();
            ModelAndView modelAndView = new ModelAndView(VERIFICATION_PAGE);
            modelAndView.addObject(USERNAME_PARAM_KEY, username);
            modelAndView.addObject(MESSAGE_ATTRIBUTE_KEY, INTERNAL_ERROR_MESSAGE);
            return modelAndView;
        } catch (AccessDeniedException e) {
            log.warn(">>>not matching codes in verify-page for turning status 1:");
            log.warn(">>>code:{}", code);
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView(VERIFICATION_PAGE);
            modelAndView.addObject(USERNAME_PARAM_KEY, username);
            modelAndView.addObject(MESSAGE_ATTRIBUTE_KEY, e.getMessage());
            return modelAndView;
        }
    }


    @GetMapping(path = "/logout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("<<<in logout method:::");
            session.invalidate();
            log.info("<<<session's invalidated:");
            CookieUtil.removeCookie(request, response, REMEMBER_TOKEN_COOKIE_KEY);
            log.info("<<<cookie's been removed:");
            return INDEX_PAGE;
        } catch (RuntimeException e) {
            log.error(">>>logout error for some reason:");
            e.printStackTrace();
            session.invalidate();
            log.info("<<<before logout got runtime ,just invalidated session:");
            CookieUtil.removeCookie(request, response, REMEMBER_TOKEN_COOKIE_KEY);
            log.info("<<<before logout got runtime ,just removed cookie:");
            return INDEX_PAGE;
        }
    }
}
