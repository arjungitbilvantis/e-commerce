package com.bilvantis.ecommerce.dto.util;

public class ECommerceDataModelConstants {

    public static final String MESSAGE_ID_ON_CREATE = "Id should be null or empty";

    public static final String MESSAGE_ID_ON_UPDATE = "Id should not be null or empty";
    public static final String EMAIL_LENGTH_MESSAGE = "Email should not be more than 100 characters";
    public static final String REGEX_PATTERN_FOR_EMAIL = "^(?:[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+(?!\\.web)\\.(?:[a-zA-Z]{2,}|[0-9]{1,3}(?:\\.[0-9]{1,3}){3}))?$";
    public static final String MESSAGE_USER_FIRST_NAME = "User first name should not be null or empty";
    public static final String NAME_LENGTH_MESSAGE = "Name should not be more than 150 characters";
    public static final String REGEX_PATTERN_FOR_NAMES = "^(?!\\s)(?:[a-zA-Z&@#$%^*\\(\\)\\[\\]\\{\\}!?,\\.\\-+_=`~'\":;<>\"]|\\s(?<!^|$))*[^\\s]$";
    public static final String NAME_MESSAGE = "Numbers or Spaces are not allowed in names";
    public static final String REGEX_PATTERN_FOR_PHONE_NUMBER = "^[6-9][0-9]{9}$";
    public static final String ERROR_MESSAGE_FOR_INVALID_PHONE_NUMBER = "Please enter valid phone number";
    public static final String REGEX_PATTERN_FOR_LAST_NAME = "^(?:[a-zA-Z&@#$%^*()\\[\\]{}!?,.\\-+_=`~'\":;<>]|(?<!^|\\s)\\s)*(?<!\\s)$";
    public static final String MESSAGE_USER_PHONE_NUMBER_ON_CREATE = "User phone number should not be null or empty on create";
    public static final String MESSAGE_USER_GENDER_ON_CREATE = "User gender should not be null or empty on create";
    public static final String MESSAGE_USER_EMAIL_ON_CREATE = "User e mail should not be null or empty on create";
    public static final String REGEX_PATTERN_FOR_GENDER = "Male|Female|Other";
    public static final String MESSAGE_GENDER = "Gender must be Male, Female, or Other";
    public static final String MESSAGE_INVALID_USER_TYPE = "Invalid user type";

    public static final String MESSAGE_USER_TYPE_ON_CREATE = "User type should not be null or empty on create";
    public static final String MESSAGE_ADDRESS_REQUIRED_ON_CREATE = "Address is required on creation";
    public static final String MESSAGE_ADDRESS_LENGTH = "Address must be between 10 and 255 characters";
    public static final int ADDRESS_MIN_LENGTH = 10;
    public static final int ADDRESS_MAX_LENGTH = 255;
    public static final int NAME_MAX_LENGTH = 150;
    public static final int EMAIL_MAX_LENGTH = 100;
    public static final String MESSAGE_PRODUCT_NAME = "Product name is required and must not be blank.";
    public static final String DESCRIPTION_PRODUCT_LENGTH_MESSAGE = "Product description must not exceed 500 characters.";
    public static final String URL_LENGTH_MESSAGE = "Image URL must not exceed 255 characters.";
    public static final String MESSAGE_PRICE_REQUIRED = "Price is required and must not be null.";
    public static final String MESSAGE_PRICE_MIN = "Price must be greater than zero.";
    public static final String MESSAGE_CATEGORY_REQUIRED = "Category is required and must not be null.";
    public static final String MESSAGE_SUB_CATEGORY_REQUIRED = "Sub-Category is required and must not be null.";
    public static final int PRODUCT_DESCRIPTION = 500;
    public static final int URL_LENGTH = 255;
    public static final String MIN_PRICE = "0.0";
    public static final String CATEGORY_NAME_REQUIRED_MESSAGE = "Category name is required.";
    public static final String CATEGORY_NAME_LENGTH_MESSAGE = "Category name must not exceed 150 characters.";
    public static final String MESSAGE_USER_ID = "UserId is required and must not be blank.";
    public static final String ORDER_STATUS_NOT_BLANK = "Order status cannot be null or empty";
    public static final String PAYMENT_STATUS_NOT_BLANK = "Payment status cannot be null or empty";
    public static final String ORDER_ITEMS_NOT_NULL = "Order items cannot be null on update";
    public static final String ORDER_ITEMS_SIZE_MIN = "At least one item is required in the order";
    public static final String MESSAGE_ORDER_ID = "OrderId is required and must not be blank.";
    public static final String MESSAGE_PRODUCT_ID = "ProductId is required and must not be blank.";
    public static final int ONE = 1;
}
