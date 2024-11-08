package com.example.survey.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.survey.data_transfer_objects.RegisterUser;

public class User implements UserDetails {
    private long user_id;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String email;
    private Integer phone_code;
    private String phone_number;
    private Date dob;
    private String gender;
    private String passkey;
    private Integer user_role;

    // Extra attribute
    private Role role;

    // private final RoleRepository roleRepository;
    // private RoleRepository roleRepository = new RoleRepository(jdbcTemplate);

    // @Autowired
    // private RoleRepository roleRepository;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User() {
    }

    public User(long user_id, String first_name, String middle_name, String last_name, String email, Integer phone_code,
            String phone_number, Date dob, String gender, String passkey, Integer user_role) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_code = phone_code;
        this.phone_number = phone_number;
        this.dob = dob;
        this.gender = gender;
        this.passkey = passkey;
        this.user_role = user_role;
    }

    public User(RegisterUser registerUser) {

        this.first_name = registerUser.getFirst_name();
        this.middle_name = registerUser.getMiddle_name();
        this.last_name = registerUser.getLast_name();
        this.email = registerUser.getEmail();
        this.phone_code = registerUser.getPhone_code();
        this.phone_number = registerUser.getPhone_number();
        this.dob = registerUser.getDob();
        this.gender = registerUser.getGender();
        this.passkey = registerUser.getPassword();
    }

    public Integer getUser_role() {
        return user_role;
    }

    public void setUser_role(Integer user_role) {
        this.user_role = user_role;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(Integer phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            throw new IllegalStateException("Role not assigned to user!");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (role.isCan_respond()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_RESPOND"));
        }
        if (role.isCan_author_form()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_AUTHOR_FORM"));
        }
        if (role.isCan_author_group()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_AUTHOR_GROUP"));
        }
        if (role.isCan_manage_forms()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_MANAGE_FORMS"));
        }
        if (role.isCan_manage_groups()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_MANAGE_GROUPS"));
        }
        if (role.isCan_manage_roles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_MANAGE_ROLES"));
        }
        if (role.isCan_manage_users()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_MANAGE_USERS"));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return passkey; // Map the password field to the passkey
    }

    @Override
    public String getUsername() {
        return email; // You can use email or any other field as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Return true if the account is not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Return true if the account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Return true if the credentials (password) are not expired
    }

    @Override
    public boolean isEnabled() {
        return true; // Return true if the account is enabled
    }
}
