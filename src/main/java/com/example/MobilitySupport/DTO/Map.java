package com.example.MobilitySupport.DTO;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



//https://ms-record.tistory.com/68
@Getter
@Setter
@Entity
@Data
@Table(name = "map")
public class Map {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "stat_id")
    public String statId;

    @Column(name = "chger_id")
    public String chgerId;

    @Column(name = "addr")
    public String addr;

    @Column(name = "lat")
    public Double lat;

    @Column(name = "lng")
    public Double lng;

    @Column(name = "stat")
    public Long stat;

    @Column(name = "zcode")
    public Long zcode;

}
