package com.bilvantis.ecommerce.dao.util;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Date;

public class CommonEntityListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) entity;
            Date currentDate = new Date();
            baseEntity.setCreatedDate(currentDate);
            baseEntity.setUpdatedDate(currentDate);
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) entity;
            baseEntity.setUpdatedDate(new Date());
        }
    }
}
