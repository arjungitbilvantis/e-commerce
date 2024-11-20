package com.bilvantis.ecommerce.dao.data.model;

import com.bilvantis.ecommerce.dao.util.BaseEntity;
import com.bilvantis.ecommerce.dao.util.CommonEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(CommonEntityListener.class)
public class Category extends BaseEntity implements Serializable {

    @Id
    @Column(name = "category_id", columnDefinition = "VARCHAR(36)")
    private String categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

}
