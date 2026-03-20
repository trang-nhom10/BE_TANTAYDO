package com.example.da_tantaydo.repository.customer;

import com.example.da_tantaydo.model.dto.response.ProfileResponseDTO;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String GET_PROFILE_BY_EMAIL_SQL = """
        SELECT 
            u.GMAIL                                     AS gmail,
            r.ROLE_NAME                                 AS role_name,
            p.PERMISSION_CODE                           AS permission_code,
            COALESCE(e.FULL_NAME, c.FULL_NAME, m.NAME) AS name,
            CASE
                WHEN COALESCE(e.IMG, c.IMG, m.IMG) REGEXP '^[0-9]+$'
                THEN ds.MEDIA_URL
                ELSE COALESCE(e.IMG, c.IMG, m.IMG)
            END                                         AS img
        FROM USERS u
        LEFT JOIN DIM_ROLES r       ON u.ROLE_ID            = r.ROLE_CODE
        LEFT JOIN ROLE_PERMISSION rp ON r.ID            = rp.ROLE_ID
        LEFT JOIN PERMISSION p     ON rp.PERMISSION_ID  = p.ID
        LEFT JOIN EMPLOYEES e      ON e.USER_ID         = u.ID
        LEFT JOIN CUSTOMERS c      ON c.USER_ID         = u.ID
        LEFT JOIN DOCTOR m         ON m.USER_ID         = u.ID
        LEFT JOIN DATA_SOUSES ds
            ON COALESCE(e.IMG, c.IMG, m.IMG) REGEXP '^[0-9]+$'
           AND ds.ID = CAST(COALESCE(e.IMG, c.IMG, m.IMG) AS UNSIGNED)
        WHERE LOWER(u.GMAIL) = LOWER(?)
        """;

    @Override
    public ProfileResponseDTO getProfileByEmail(String email) {
        Assert.hasText(email, "Email is required");

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                GET_PROFILE_BY_EMAIL_SQL, email.trim()
        );

        if (rows.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        Map<String, Object> first = rows.get(0);

        List<String> permissions = rows.stream()
                .map(r -> r.get("permission_code"))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .distinct()
                .toList();

        return new ProfileResponseDTO(
                getString(first, "gmail"),
                getString(first, "role_name"),
                getString(first, "name"),
                getString(first, "img"),
                permissions
        );
    }

    private String getString(Map<String, Object> row, String key) {
        Object val = row.get(key);
        return val != null ? val.toString() : null;
    }
}