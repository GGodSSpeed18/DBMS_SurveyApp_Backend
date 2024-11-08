package com.example.survey.data_access_layers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.survey.data_transfer_objects.UserDTO;
import com.example.survey.entities.Role;
import com.example.survey.entities.User;
import com.example.survey.mappers.UserDTOWithRoleRowMapper;
import com.example.survey.mappers.UserWithRoleRowMapper;
import com.example.survey.utilities.TableNames;

@Repository
public class RoleRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResourceAvailabilityRepository resourceAvailabilityRepository;

    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Role> getSubsetRoles(Integer roleid) {
        if (roleid == null) {
            throw new IllegalArgumentException("Can't check for subsets when role_id is null!");
        }
        resourceAvailabilityRepository.checkRoleExistence(roleid);
        String sql = String.format(
                "SELECT r2.* FROM %s r1 JOIN %s r2 ON r1.role_id = ? WHERE (r2.can_respond <= r1.can_respond) AND (r2.can_author_form <= r1.can_author_form) AND (r2.can_author_group <= r1.can_author_group) AND (r2.can_manage_forms <= r1.can_manage_forms) AND (r2.can_manage_groups <= r1.can_manage_groups) AND (r2.can_manage_roles <= r1.can_manage_roles) AND (r2.can_manage_users <= r1.can_manage_users) AND NOT ((r2.can_respond = r1.can_respond) AND (r2.can_author_form = r1.can_author_form) AND (r2.can_author_group = r1.can_author_group) AND (r2.can_manage_forms = r1.can_manage_forms) AND (r2.can_manage_groups = r1.can_manage_groups) AND (r2.can_manage_roles = r1.can_manage_roles) AND (r2.can_manage_users = r1.can_manage_users))",
                TableNames.roleT, TableNames.roleT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Role.class), new Object[] { roleid });
    }

    public boolean isSubset(Role parentRole, Role childRole) {
        if (!parentRole.isCan_author_form() && childRole.isCan_author_form()) {
            return false;
        }
        if (!parentRole.isCan_author_group() && childRole.isCan_author_group()) {
            return false;
        }
        if (!parentRole.isCan_manage_forms() && childRole.isCan_manage_forms()) {
            return false;
        }
        if (!parentRole.isCan_author_form() && childRole.isCan_author_form()) {
            return false;
        }
        if (!parentRole.isCan_manage_groups() && childRole.isCan_manage_groups()) {
            return false;
        }
        if (!parentRole.isCan_manage_roles() && childRole.isCan_manage_roles()) {
            return false;
        }
        if (!parentRole.isCan_respond() && childRole.isCan_respond()) {
            return false;
        }
        if (!parentRole.isCan_manage_users() && childRole.isCan_manage_users()) {
            return false;
        }

        if (parentRole.isCan_author_form() == childRole.isCan_author_form()
                && parentRole.isCan_author_group() == childRole.isCan_author_group()
                && parentRole.isCan_manage_forms() == childRole.isCan_manage_forms()
                && parentRole.isCan_manage_groups() == childRole.isCan_manage_groups()
                && parentRole.isCan_manage_roles() == childRole.isCan_manage_roles()
                && parentRole.isCan_manage_users() == childRole.isCan_manage_users()
                && parentRole.isCan_respond() == childRole.isCan_respond()) {
            return false;
        }

        return true;
    }

    public List<User> getUserbyRole(Integer roleid) {
        resourceAvailabilityRepository.checkRoleExistence(roleid);
        String sql = String.format("SELECT u.*, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_role=?",
                TableNames.userT, TableNames.roleT);
        return jdbcTemplate.query(sql, new UserWithRoleRowMapper(), new Object[] { roleid });
    }

    public List<UserDTO> getUserDTObyRole(Integer roleid) {
        resourceAvailabilityRepository.checkRoleExistence(roleid);
        String sql = String.format("SELECT u.*, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_role=?",
                TableNames.userT, TableNames.roleT);
        return jdbcTemplate.query(sql, new UserDTOWithRoleRowMapper(), new Object[] { roleid });
    }

    public Role findRoleByName(String roleName) {
        String sql = String.format("SELECT * FROM %s WHERE role_name='%s'", TableNames.roleT, roleName);
        try {

            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Role.class));
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Role name does not exist: " + roleName);
        }
    }

    public Role findRoleById(Integer roleid) {
        String sql = String.format("SELECT * FROM %s WHERE role_id=?", TableNames.roleT);
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Role.class), roleid);
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Invalid role ID: " + roleid, ex);
        }
    }

    public void addRole(Role newrole) {
        String sql = String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, ?, ?)", TableNames.roleT);
        // Integer role = newrole.getRole_id();
        jdbcTemplate.update(sql, new Object[] {
                newrole.getRole_name(),
                newrole.isCan_respond(),
                newrole.isCan_author_form(),
                newrole.isCan_author_group(),
                newrole.isCan_manage_forms(),
                newrole.isCan_manage_groups(),
                newrole.isCan_manage_roles(),
                newrole.isCan_manage_users()
        });
    }

    public void dropRole(Integer roleid) {
        if (roleid == null) {
            throw new IllegalArgumentException("Can't delete when role_id is null!");
        }
        resourceAvailabilityRepository.checkRoleExistence(roleid);
        String sql = String.format("DELETE FROM %s WHERE role_id=?", TableNames.roleT);
        try {
            jdbcTemplate.update(sql, new Object[] { roleid });
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Invalid Role ID: " + roleid, e);
        }
    }

    public List<Role> getAllRoles() {
        String sql = String.format("SELECT * FROM %s", TableNames.roleT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Role.class));
    }
}
