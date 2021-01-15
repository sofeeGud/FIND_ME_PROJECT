package com.findme.dao;

import com.findme.exceptions.InternalServerError;
import com.findme.models.Post;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class PostDAO extends GeneralDAOImpl<Post> {

    public PostDAO() {
        setClazz(Post.class);
    }

    public List<Post> getPostsByFilterOwner(Long userId, Boolean ownerPosts, Boolean friendsPosts, Long userPostedId) throws InternalServerError {
        String filters = "";

        if (ownerPosts != null && ownerPosts) {
            filters = " AND p.userPosted.id=:userId ";
        }
        if (friendsPosts != null && friendsPosts) {
            filters = " AND r.status = 'FRIENDS'";
        }
        if (userPostedId != null && userPostedId > 0) {
            filters = " AND p.userPosted.id=" + userPostedId + " ";
        }
        try {

            return entityManager.createQuery("SELECT p" +
                    " FROM Post p " +
                    " LEFT JOIN Relationship r ON (r.userFrom.id = :userId AND r.userTo.id = p.userPosted.id) OR (r.userTo.id = :userId AND r.userFrom.id = p.userPosted.id)" +
                    "WHERE p.userPagePosted.id = :userId " +
                    filters +
                    " ORDER BY p.datePosted DESC", Post.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }
}
