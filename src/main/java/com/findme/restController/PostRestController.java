package com.findme.restController;

import com.findme.models.Post;
import com.findme.models.User;
import com.findme.service.PostService;
import com.findme.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Log4j
@RestController
public class PostRestController {
    private PostService postService;
    private UserService userService;

    @Autowired
    public PostRestController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @RequestMapping(path = "/save-post/{userId}", method = RequestMethod.POST)
    public ResponseEntity<String> savePost(@ModelAttribute Post post, HttpSession session, @PathVariable String userId, @RequestParam String usersTaggedIds) {
        if (session.getAttribute("loggedUserId") == null) {
            log.warn("User is not authorized");
            return new ResponseEntity<>("You are not logged in to see this information.", HttpStatus.FORBIDDEN);
        }
        User userPagePosted = userService.findById(Long.valueOf(userId));
        String loggedUserId = (String) session.getAttribute("loggedUserId");
        User UserPosted = userService.findById(Long.valueOf(loggedUserId));


        post.setUserPosted(UserPosted);
        post.setUserPagePosted(userPagePosted);
        if (usersTaggedIds != null && post.getMessage() != null && post.getLocation() != null) {
            postService.save(post, usersTaggedIds);
        } else
            return new ResponseEntity<>("You post is empty", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(path = "/get-filtered-posts/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> posFilters(@RequestParam Boolean ownerPosts, Boolean friendsPosts, Long userPostedId, HttpSession session, @PathVariable String userId) {
        String loggedUserId = (String) session.getAttribute("loggedUserId");
        if (loggedUserId == null) {
            log.warn("User is not authorized");
            return new ResponseEntity<>("You are not logged in to see this information.", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(postService.getPostsByFilter(ownerPosts, friendsPosts, userPostedId, Long.valueOf(userId)), HttpStatus.OK);
    }

    @RequestMapping(path = "/delete-post", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePost(@RequestParam String postId, HttpSession session) {
        String loggedUserId = (String) session.getAttribute("loggedUserId");
        if (loggedUserId == null) {
            log.warn("User is not authorized");
            return new ResponseEntity<>("You are not logged in to see this information.", HttpStatus.FORBIDDEN);
        }
        postService.delete(Long.valueOf(postId), Long.valueOf(loggedUserId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/get-news", method = RequestMethod.GET)
    public ResponseEntity<?> getNewsFeed(HttpSession session,
                                         @RequestParam int maxResult,
                                         @RequestParam int currentListPart) {
        String loggedUserId = (String) session.getAttribute("loggedUserId");
        if (loggedUserId == null) {
            log.warn("User is not authorized");
            return new ResponseEntity<>("You are not logged in to see this information.", HttpStatus.FORBIDDEN);
        }
        List<Post> list = postService.getNews(Long.valueOf(loggedUserId), maxResult, currentListPart);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
