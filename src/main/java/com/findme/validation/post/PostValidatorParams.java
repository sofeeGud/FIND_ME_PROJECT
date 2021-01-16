package com.findme.validation.post;

import com.findme.models.Post;
import com.findme.models.Relationship;
import com.findme.models.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
public class PostValidatorParams {
    private Post post;
    private Relationship relBtwAuthorAndPagePostedUser;
    private List<User> usersTagged;
}
