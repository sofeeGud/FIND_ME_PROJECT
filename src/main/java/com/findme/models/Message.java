package com.findme.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "MESSAGE")
public class Message {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "MESSAGE_ID_SEQ", sequenceName = "MESSAGE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGE_ID_SEQ")
    private Long id;

    @ManyToOne
    @JoinColumn(name="USER_FROM_ID", nullable = false)
    private User userFrom;

    @ManyToOne
    @JoinColumn(name="USER_TO_ID", nullable = false)
    private User userTo;

    @Column(name = "DATE_SENT")
    private Date dateSent;

/*    @Column(name = "DATE_EDITED")
    private Date dateEdited;*/

/*    @Column(name = "DATE_DELETED")
    private Date dateDeleted;*/

    @Column(name = "DATE_READ")
    private Date dateRead;

    @Column(name = "TEXT")
    private String text;

}
