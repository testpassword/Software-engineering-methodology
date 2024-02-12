package finist.back.model;

import finist.back.model.enums.MarriageStatus;

import javax.persistence.*;

@Entity
@Table (name="marriages")
public class Marriage {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional=false, cascade= CascadeType.ALL)
    @JoinColumn(name="competition_id")
    private Competition competition;

    @OneToOne(optional=false, cascade= CascadeType.ALL)
    @JoinColumn(name="groom_id")
    private User groom;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="bride_id")
    private User bride;

    @Column(name = "status")
    private MarriageStatus marriageStatus;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public MarriageStatus getMarriageStatus() {
        return marriageStatus;
    }

    public User getBride() {
        return bride;
    }
}
