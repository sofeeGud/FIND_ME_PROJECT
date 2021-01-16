package com.findme.service;

import com.findme.dao.RelationshipDAO;
import com.findme.dao.UserDAO;
import com.findme.exceptions.BadRequestException;
import com.findme.dao.PostDAO;
import com.findme.exceptions.InternalServerError;
import com.findme.models.Post;
import com.findme.models.Relationship;
import com.findme.models.User;
import com.findme.validation.post.AbstractPostValidator;
import com.findme.validation.post.PostValidatorParams;
import com.findme.validation.post.UserPagePostedValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostService {
    private PostDAO postDAO;
    private UserDAO userDAO;
    private RelationshipDAO relationshipDAO;

    @Autowired
    public PostService(PostDAO postDAO, UserDAO userDAO, RelationshipDAO relationshipDAO) {
        this.postDAO = postDAO;
        this.userDAO = userDAO;
        this.relationshipDAO = relationshipDAO;

    }


    public Post save(Post post, String usersTaggedIds) throws InternalServerError, BadRequestException {
        post.setDatePosted(new Date());
        User userPosted = userDAO.findById(post.getUserPosted().getId());
        Relationship relBtwAuthorAndPagePostedUser = relationshipDAO.getRelationship(post.getUserPosted().getId().toString(), post.getUserPagePosted().getId().toString());
        List<User> usersTagged = new ArrayList<>();
        String[] usersTaggedId =  usersTaggedIds.split(",");
        for (String id : usersTaggedId){
            if (id != null && Long.parseLong(id)>0) {
                User user = userDAO.findById(Long.valueOf(id));
                if (user != null) {
                    usersTagged.add(user);
                }
            }
        }
        post.setUsersTagged(usersTagged);

        post.setUserPagePosted(post.getUserPagePosted());
        AbstractPostValidator usersValidator = new UserPagePostedValidator();
        usersValidator.check(
                PostValidatorParams.builder()
                        .post(post)
                        .relBtwAuthorAndPagePostedUser(relBtwAuthorAndPagePostedUser)
                        .usersTagged(usersTagged)
                        .build()
        );
        return postDAO.save(post);
    }

    public Post update(Post post) throws BadRequestException {
        return postDAO.update(post);
    }

    public void delete(Long id, Long userId) throws InternalServerError, BadRequestException {
        Post post = postDAO.findById(id);
        if (post == null) {
            throw new BadRequestException("Wrong post id");
        }
        if (!post.getUserPosted().getId().equals(userId)) {
            throw new BadRequestException("You can not remove this post");
        }
        postDAO.delete(id);
    }

    public Post findById(Long id) throws BadRequestException {
        return postDAO.findById(id);
    }

    public List<Post> getPostsByFilterOwner(Boolean ownerPosts, Boolean friendsPosts, Long userPostedId, Long userIdPage) throws BadRequestException, InternalServerError {
        return postDAO.getPostsByFilterOwner(ownerPosts, friendsPosts, userPostedId, userIdPage);
    }

}
