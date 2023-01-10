package com.example.MobilitySupport.DTO;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@Entity
@Data
@Table(name="Zscode")
public class Zscode {

    @Id
    @Column(name="code")
    private Long code;


    @Column(name="region")
    private String region;












}
