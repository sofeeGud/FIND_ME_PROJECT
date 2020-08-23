package com.findme.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "POST")
public class Post {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "POST_ID_SEQ", sequenceName = "POST_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_ID_SEQ")
    private Long id;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "DATE_POSTED")
    private Date datePosted;

    @Column(name = "LOCATION")
    private String location;

    @ManyToOne
    @JoinColumn(name="USER_POSTED_ID", nullable = false)
    private User userPosted;

    @ManyToOne
    @JoinColumn(name="USER_PAGE_POSTED_ID", nullable = false)
    private User userPagePosted;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="USERS_TAGGED", joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private List<User> usersTagged;

}
