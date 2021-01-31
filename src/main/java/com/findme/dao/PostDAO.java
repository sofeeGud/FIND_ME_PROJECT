package com.findme.dao;

import com.findme.exceptions.InternalServerError;
import com.findme.models.Post;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Log4j
@Transactional
@Repository
public class PostDAO extends GeneralDAOImpl<Post> {
    private static final String GET_SQL_NEWS = "SELECT p " +
            " FROM Post p " +
            " LEFT JOIN Relationship r ON (r.userFrom.id = :userId AND r.userTo.id = p.userPosted.id) OR (r.userTo.id = :userId AND r.userFrom.id = p.userPosted.id)" +
            "WHERE r.status = 'FRIENDS' " +
            " ORDER BY p.datePosted DESC";


    public PostDAO() {
        setClazz(Post.class);
    }

    public List<Post> getPostsByFilter(Boolean ownerPosts, Boolean friendsPosts, Long userPostedId, Long userIdPage) throws InternalServerError {
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
                    "WHERE r.status = 'FRIENDS'" +
                    filters +
                    " ORDER BY p.datePosted DESC", Post.class)
                    .setParameter("userId", userIdPage)
                    .getResultList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalServerError(e.getMessage());
        }
    }


    public List<Post> getPostsNews(Long userId, int rowsFrom, int maxResults) throws InternalServerError {
        try {

            return entityManager.createQuery(GET_SQL_NEWS, Post.class)
                    .setParameter("userId", userId)
                    .setFirstResult(rowsFrom)
                    .setMaxResults(maxResults)
                    .getResultList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalServerError(e.getMessage());
        }
    }

}
