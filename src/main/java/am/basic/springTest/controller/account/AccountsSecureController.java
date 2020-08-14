package am.basic.springTest.controller.account;

import am.basic.springTest.model.User;
import am.basic.springTest.model.exceptions.AccessDeniedException;
import am.basic.springTest.model.exceptions.NotFoundException;
import am.basic.springTest.model.exceptions.NotMatchingPasswordsException;
import am.basic.springTest.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import static am.basic.springTest.util.constants.Messages.INTERNAL_ERROR_MESSAGE;
import static am.basic.springTest.util.constants.Messages.PASSWORD_CHANGE_SUCCESS_MESSAGE;
import static am.basic.springTest.util.constants.Pages.HOME_PAGE;
import static am.basic.springTest.util.constants.ParameterKeys.MESSAGE_ATTRIBUTE_KEY;

@Log4j2
@Controller
@RequestMapping("/secure")
public class AccountsSecureController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ModelAndView changePassword(@RequestParam String password,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmingPassword,
                                       @SessionAttribute("user") User sessionUser) {

        try {
            log.info("<<<in change-password-method:::");
            userService.changePassword(sessionUser.getUsername(), password, newPassword, confirmingPassword);
            log.info("<<<password's been changed:");
            return new ModelAndView(HOME_PAGE, MESSAGE_ATTRIBUTE_KEY, PASSWORD_CHANGE_SUCCESS_MESSAGE);
        } catch (NotFoundException e) {
            log.warn(">>>user in home-page trying to change password ,some error is-can't find user:");
            log.warn(">>>session user id :{}", sessionUser.getId());
            e.printStackTrace();
            return new ModelAndView(HOME_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (RuntimeException ex) {
            log.error(">>>some unwanted error during change-password:");
            ex.printStackTrace();
            return new ModelAndView(HOME_PAGE, MESSAGE_ATTRIBUTE_KEY, INTERNAL_ERROR_MESSAGE);
        } catch (AccessDeniedException e) {
            log.warn(">>>during changing password ,not matching old and new passwords:");
            log.warn(">>> wrong password:{}", password);
            e.printStackTrace();
            return new ModelAndView(HOME_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (NotMatchingPasswordsException e) {
            log.warn(">>>during changing password not matching one found:");
            log.warn(">>>wrong confirmed password:{}", confirmingPassword);
            e.printStackTrace();
            return new ModelAndView(HOME_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());

        }

    }

}
