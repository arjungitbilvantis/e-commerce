package com.bilvantis.ecommerce.dao.data.model;

import com.bilvantis.ecommerce.dao.util.BaseEntity;
import com.bilvantis.ecommerce.dao.util.CommonEntityListener;
import com.bilvantis.ecommerce.dao.util.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(CommonEntityListener.class)
public class User extends BaseEntity implements Serializable {

    @Id
    @Column(name = "user_id", columnDefinition = "VARCHAR(36)")
    private String userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender")
    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "address")
    private String address;

    @Column(name = "first_time_user")
    private Boolean firstTimeUser;

    @Column(name = "password", length = 20)
    private String password;

    @Column(name = "otp_gen", length = 6)
    private String otp;

    @Column(name = "otp_gen_time")
    private Long otpGenerationTime;

}
