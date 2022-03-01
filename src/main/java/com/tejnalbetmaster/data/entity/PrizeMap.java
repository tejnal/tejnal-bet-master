package com.tejnalbetmaster.data.entity;

import com.tejnalbetmaster.data.entity.models.EPrizeMode;

import javax.persistence.*;

@Entity
@Table(name = "prize_map",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "prize_cd")
})
public class PrizeMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="prize_descr")
    private String prizeDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "prize_cd")
    private EPrizeMode prizeCode;

    public PrizeMap() {
    }

    public PrizeMap(String prizeDescription, EPrizeMode prizeCode) {
        this.prizeDescription = prizeDescription;
        this.prizeCode = prizeCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrizeDescription() {
        return prizeDescription;
    }

    public void setPrizeDescription(String prizeDescription) {
        this.prizeDescription = prizeDescription;
    }

    public EPrizeMode getPrizeCode() {
        return prizeCode;
    }

    public void setPrizeCode(EPrizeMode prizeCode) {
        this.prizeCode = prizeCode;
    }

    @Override
    public String toString() {
        return "PrizeMap{" +
                "id=" + id +
                ", prizeDescription='" + prizeDescription + '\'' +
                ", prizeCode=" + prizeCode +
                '}';
    }
}
