package org.smartx.summer.bean;


import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * A DTO representing a user's credentials
 */
public class LoginDTO {

    @NotBlank
    @Size(min = 1, max = 15)
    private String userPhone;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;


    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "password='" + password + '\'' +
                ", userPhone='" + userPhone +
                '}';
    }
}
