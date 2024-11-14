package com.bilvantis.ecommerce.dto.model;

import com.bilvantis.ecommerce.dto.util.OnCreate;
import com.bilvantis.ecommerce.dto.util.OnUpdate;
import com.bilvantis.ecommerce.dto.util.ValidEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.bilvantis.ecommerce.dto.util.ECommerceDataModelConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends BaseDTO implements Serializable {

    @Null(groups = OnCreate.class, message = MESSAGE_ID_ON_CREATE)
    @NotNull(groups = OnUpdate.class, message = MESSAGE_ID_ON_UPDATE)
    private String userId;

    @NotBlank(message = MESSAGE_USER_FIRST_NAME)
    @Size(max = 150, message = NAME_LENGTH_MESSAGE)
    @Pattern(regexp = REGEX_PATTERN_FOR_NAMES, message = NAME_MESSAGE)
    private String firstName;

    @Size(max = 150, message = NAME_LENGTH_MESSAGE)
    @Pattern(regexp = REGEX_PATTERN_FOR_LAST_NAME, message = NAME_MESSAGE)
    private String lastName;

    @NotBlank(groups = OnCreate.class, message = MESSAGE_USER_EMAIL_ON_CREATE)
    @Size(max = 100, message = EMAIL_LENGTH_MESSAGE)
    @Email(regexp = REGEX_PATTERN_FOR_EMAIL, flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @NotBlank(groups = OnCreate.class, message = MESSAGE_USER_PHONE_NUMBER_ON_CREATE)
    @Pattern(regexp = REGEX_PATTERN_FOR_PHONE_NUMBER, message = ERROR_MESSAGE_FOR_INVALID_PHONE_NUMBER)
    private String phoneNumber;

    @Pattern(regexp = REGEX_PATTERN_FOR_GENDER, message = MESSAGE_GENDER)
    private String gender;

    @ValidEnum(enumClass = UserTypeDTO.class, message = MESSAGE_INVALID_USER_TYPE)
    private UserTypeDTO userTypeDTO;

    @NotBlank(groups = OnCreate.class, message = MESSAGE_ADDRESS_REQUIRED_ON_CREATE)
    @Size(min = ADDRESS_MIN_LENGTH, max = ADDRESS_MAX_LENGTH, message = MESSAGE_ADDRESS_LENGTH)
    private String address;

    private Boolean firstTimeUser;
}
