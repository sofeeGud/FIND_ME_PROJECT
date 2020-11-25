package com.findme.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "RELATIONSHIP")
public class Relationship implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name="USER_FROM_ID", nullable = false)
    private User userFrom;

    @Id
    @ManyToOne
    @JoinColumn(name="USER_TO_ID", nullable = false)
    private User userTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private RelationshipStatus status;

    @Column(name = "DATE_MODIFIED")
    private Date dateModified;

}
