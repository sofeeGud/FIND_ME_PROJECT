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

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public RelationshipStatus getStatus() {
        return status;
    }

    public void setStatus(RelationshipStatus status) {
        this.status = status;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }
}
