package am.basic.springTest.controller.comment;

import am.basic.springTest.model.Comment;
import am.basic.springTest.model.User;
import am.basic.springTest.service.CommentService;
import am.basic.springTest.util.ValidationMessageConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static am.basic.springTest.util.constants.Messages.INTERNAL_ERROR_MESSAGE;
import static am.basic.springTest.util.constants.Pages.COMMENT_PAGE;
import static am.basic.springTest.util.constants.ParameterKeys.COMMENT_KEY;
import static am.basic.springTest.util.constants.ParameterKeys.MESSAGE_ATTRIBUTE_KEY;

@Log4j2
@Controller
@RequestMapping("/secure")
public class CommentController {


    @Autowired
    private CommentService commentService;

    @GetMapping("/comments")
    public ModelAndView getCommentPage(@SessionAttribute("user") User user) {
        try {
            List<Comment> comments = commentService.getByUserId(user.getId());
            log.info("<<<in comment page- method:::");
            return new ModelAndView(COMMENT_PAGE, "comments", comments);
        } catch (RuntimeException e) {
            log.error(">>>some error on comment get page:");
            e.printStackTrace();
            return new ModelAndView(COMMENT_PAGE, MESSAGE_ATTRIBUTE_KEY, INTERNAL_ERROR_MESSAGE);
        }
    }

    @PostMapping("/comments/add")
    public ModelAndView add(@SessionAttribute("user") User user, @RequestParam String name, @RequestParam String description) {
        try {
            Comment comment = new Comment();
            comment.setName(name);
            comment.setDescription(description);
            comment.setUserId(user.getId());
            commentService.add(comment);
            log.info("<<<comment's been added:");
            return getCommentPage(user);
        } catch (ConstraintViolationException exception) {
            log.info(">>>on comment page some model pattern or restriction exception:{}",exception);
            List<Comment> comments = commentService.getByUserId(user.getId());
            ModelAndView modelAndView = new ModelAndView(COMMENT_PAGE);
            modelAndView.addObject(COMMENT_KEY, comments);
            modelAndView.addObject(MESSAGE_ATTRIBUTE_KEY, ValidationMessageConverter.getMessage(exception));
            return modelAndView;
        } catch (RuntimeException exception) {
            log.error(">>>comment add page, unexpected error:");
            exception.printStackTrace();
            return new ModelAndView(COMMENT_PAGE, MESSAGE_ATTRIBUTE_KEY, INTERNAL_ERROR_MESSAGE);
        }
    }


    @PostMapping("/comments/edit")
    public ModelAndView delete(@SessionAttribute("user") User user,
                               @RequestParam Integer id,
                               @RequestParam String name,
                               @RequestParam String description,
                               @RequestParam String submit) {
        try {
            if (submit.equalsIgnoreCase("DELETE")) {
                commentService.delete(id);
                log.info("<<<comment edit method:::comment's been deleted:");
            } else {
                Comment comment = new Comment();
                comment.setUserId(user.getId());
                comment.setId(id);
                comment.setName(name);
                comment.setDescription(description);
                commentService.add(comment);
                log.info("<<<comment edit method:::comment 's been added:");
            }

            return getCommentPage(user);
        } catch (RuntimeException exception) {
            log.error(">>>on comment edit step got some unexpected error:");
            exception.printStackTrace();
            return new ModelAndView(COMMENT_PAGE, MESSAGE_ATTRIBUTE_KEY, INTERNAL_ERROR_MESSAGE);
        }
    }


}
