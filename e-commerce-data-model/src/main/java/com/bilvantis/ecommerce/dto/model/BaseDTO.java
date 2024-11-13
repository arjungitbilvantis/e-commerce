package com.bilvantis.ecommerce.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class BaseDTO {

    private Boolean isActive;

    private String createdBy;

    private Date createdDate;

    private String updatedBy;

    private Date updatedDate;
}
