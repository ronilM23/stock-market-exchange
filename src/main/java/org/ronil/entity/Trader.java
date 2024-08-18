package org.ronil.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class Trader implements Serializable {

    private String name;

    @Id
    private Integer id;
}
