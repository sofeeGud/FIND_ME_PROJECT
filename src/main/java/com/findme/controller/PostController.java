package com.findme.controller;
import com.findme.models.Post;
import com.findme.models.User;
import com.findme.service.PostService;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PostController {
    private PostService postService;
    private UserService userService;

    @Autowired
    public PostController(PostService postService,UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @RequestMapping(path = "/save-post/{userId}", method = RequestMethod.POST)
    public ResponseEntity<String> savePost(@ModelAttribute Post post, HttpSession session, @PathVariable String userId, @RequestParam String usersTaggedIds){
        if(session.getAttribute("loggedUserId")==null) {
            return new ResponseEntity<>("You are not logged in to see this information.", HttpStatus.FORBIDDEN);
        }
        User userPagePosted = userService.findById(Long.valueOf(userId));
        String loggedUserId = (String) session.getAttribute("loggedUserId");
        User UserPosted = userService.findById(Long.valueOf(loggedUserId));


        post.setUserPosted(UserPosted);
        post.setUserPagePosted(userPagePosted);
        postService.save(post, usersTaggedIds);
        return new ResponseEntity<>( HttpStatus.OK);
    }


    @RequestMapping(path = "/get-filtered-posts/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> posFilters(@RequestParam Boolean ownerPosts, Boolean friendsPosts, Long userPostedId, HttpSession session, @PathVariable String userId  ) {
        String loggedUserId = (String) session.getAttribute("loggedUserId");
        if(loggedUserId==null) {
            return new ResponseEntity<>("You are not logged in to see this information.", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(postService.getPostsByFilterOwner(ownerPosts, friendsPosts, userPostedId, Long.valueOf(userId)), HttpStatus.OK);
    }
    @RequestMapping(path = "/delete-post", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePost(@RequestParam String postId, HttpSession session){
        String loggedUserId = (String) session.getAttribute("loggedUserId");
        if(loggedUserId==null) {
            return new ResponseEntity<>("You are not logged in to see this information.", HttpStatus.FORBIDDEN);
        }
        postService.delete(Long.valueOf(postId), Long.valueOf(loggedUserId));
        return new ResponseEntity<>( HttpStatus.OK);
    }

}
