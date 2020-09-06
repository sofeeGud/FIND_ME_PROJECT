package com.findme.service;

import com.findme.config.BadRequestException;
import com.findme.dao.PostDAO;
import com.findme.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PostService {

    private PostDAO postDAO;

    @Autowired
    public PostService(PostDAO postDAO) {
        this.postDAO = postDAO;
    }


    public Post save(Post post) throws BadRequestException {
        return postDAO.save(post);
    }

    public Post update(Post post) throws BadRequestException {
        return postDAO.update(post);
    }

    public void delete(Long id) throws BadRequestException {
        Post post = postDAO.findById(id);
        postDAO.delete(id);
    }

    public Post findById(Long id) throws BadRequestException {
        return postDAO.findById(id);
    }
}
