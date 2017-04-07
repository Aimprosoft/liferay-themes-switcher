package com.aimprosoft.lfs.model.persist;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The representation of the entities which could be serialized
 *
 * @author AimProSoft
 */
@MappedSuperclass
public abstract class PersistModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}