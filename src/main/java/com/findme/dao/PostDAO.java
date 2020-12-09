package com.findme.dao;

import com.findme.models.Post;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Transactional
@Repository
public class PostDAO extends GeneralDAOImpl<Post> {

}
