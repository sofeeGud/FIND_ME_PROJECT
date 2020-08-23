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
@Table(name = "USER")
public class User {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "USER_ID_SEQ", sequenceName = "USER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_SEQ")
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    //TODO from existed data
  //  @ManyToOne
    @Column(name = "COUNTRY_ID")
    private String country;

    @Column(name = "CITY")
    private String city;

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "DATE_REGISTERED")
    private Date dateRegistered;

    @Column(name = "DATE_LAST_ACTIVE")
    private Date dateLastActive;

    //TODO enum
    @Column(name = "RELATIONSHIP_STATUS")
    private String relationshipStatus;

    @Column(name = "RELIGION")
    private String religion;

    //TODO from existed data
    @Column(name = "SCHOOL")
    private String school;

    @Column(name = "UNIVERSITY")
    private String university;

/*    private List<Message> messagesSent;
    private List<Message> messagesReceived;*/

}
