package com.example.da_tantaydo.repository.customer;

import com.example.da_tantaydo.model.dto.response.CustomerProfileDTO;
import com.example.da_tantaydo.model.dto.response.EmployeeProfileDTO;
import com.example.da_tantaydo.model.dto.response.DoctorProfileDTO;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final EntityManager em;

    @Override
    public Object getProfileByEmail(String email) {
        String roleQuery = "SELECT u.ROLE_ID FROM USERS u WHERE u.GMAIL = :email";
        Object roleRaw = em.createNativeQuery(roleQuery)
                .setParameter("email", email)
                .getSingleResult();

        String role = roleRaw != null ? roleRaw.toString().trim() : "";

        return switch (role) {
            case "1", "2" -> getEmployeeProfile(email);
            case "3"           -> getCustomerProfile(email);
            case "4"           -> getDoctorProfile(email);
            default            -> throw new RuntimeException("Role not found: " + role);
        };
    }
    private EmployeeProfileDTO getEmployeeProfile(String email) {
        String sql = """
                SELECT u.GMAIL, r.ROLE_NAME,
                       e.FULL_NAME, e.PHONE, e.GENDER,
                       e.DATE, e.ADDRESS, e.CREATED_AT,
                       e.CCCD,
                       CASE
                           WHEN e.IMG IS NOT NULL AND e.IMG REGEXP '^[0-9]+$'
                               THEN ds.MEDIA_URL
                           ELSE e.IMG
                       END AS img
                FROM USERS u
                         JOIN DIM_ROLES r ON r.ROLE_CODE = u.ROLE_ID
                         LEFT JOIN EMPLOYEES e ON e.USER_ID = u.ID
                         LEFT JOIN DATA_SOUSES ds
                                   ON e.IMG IS NOT NULL
                                       AND e.IMG REGEXP '^[0-9]+$'
                                       AND ds.ID = CAST(e.IMG AS UNSIGNED)
                WHERE u.GMAIL = :email
                """;

        Object[] r = (Object[]) em.createNativeQuery(sql)
                .setParameter("email", email)
                .getSingleResult();

        return new EmployeeProfileDTO(
                (String) r[0],
                (String) r[1],
                getPermissions(email),
                r[2] != null ? (String) r[2] : null,
                r[3] != null ? (String) r[3] : null,
                r[4] != null ? (String) r[4] : null,
                toLocalDate(r[5]),
                r[6] != null ? (String) r[6] : null,
                toLocalDate(r[7]),
                r[8] != null ? (String) r[8] : null,
                r[9] != null ? (String) r[9] : null
        );
    }

    private DoctorProfileDTO getDoctorProfile(String email) {
        String sql = """
                SELECT u.GMAIL, r.ROLE_NAME,
                       m.NAME, m.PHONE,
                       m.SPECIALIZED, m.INFORMATION,
                       m.CREATED_AT, m.ADDRESS, m.LEVER,
                       CASE
                           WHEN m.IMG IS NOT NULL AND m.IMG REGEXP '^[0-9]+$'
                               THEN ds.MEDIA_URL
                           ELSE m.IMG
                       END AS img
                FROM USERS u
                         JOIN DIM_ROLES r ON r.ROLE_CODE = u.ROLE_ID
                         LEFT JOIN doctor m ON m.USER_ID = u.ID
                         LEFT JOIN DATA_SOUSES ds
                                   ON m.IMG IS NOT NULL
                                       AND m.IMG REGEXP '^[0-9]+$'
                                       AND ds.ID = CAST(m.IMG AS UNSIGNED)
                WHERE u.GMAIL = :email
                """;

        Object[] r = (Object[]) em.createNativeQuery(sql)
                .setParameter("email", email)
                .getSingleResult();

        return new DoctorProfileDTO(
                (String) r[0],
                (String) r[1],
                getPermissions(email),
                r[2] != null ? (String) r[2] : null,
                r[3] != null ? (String) r[3] : null,
                r[4] != null ? (String) r[4] : null,
                r[5] != null ? (String) r[5] : null,
                toLocalDate(r[6]),
                r[7] != null ? (String) r[7] : null,
                r[9] != null ? (String) r[9] : null,
                r[8] != null ? (String) r[8] : null
        );
    }

    private CustomerProfileDTO getCustomerProfile(String email) {
        String sql = """
                SELECT u.GMAIL, r.ROLE_NAME,
                       c.FULL_NAME, c.PHONE,
                       c.DATE, c.ADDRESS,
                       CASE
                           WHEN c.IMG IS NOT NULL AND c.IMG REGEXP '^[0-9]+$'
                               THEN ds.MEDIA_URL
                           ELSE c.IMG
                       END AS img
                FROM USERS u
                         JOIN DIM_ROLES r ON r.ROLE_CODE = u.ROLE_ID
                         LEFT JOIN CUSTOMERS c ON c.USER_ID = u.ID
                         LEFT JOIN DATA_SOUSES ds
                                   ON c.IMG IS NOT NULL
                                       AND c.IMG REGEXP '^[0-9]+$'
                                       AND ds.ID = CAST(c.IMG AS UNSIGNED)
                WHERE u.GMAIL = :email
                """;

        Object[] r = (Object[]) em.createNativeQuery(sql)
                .setParameter("email", email)
                .getSingleResult();

        return new CustomerProfileDTO(
                (String) r[0],
                (String) r[1],
                getPermissions(email),
                r[2] != null ? (String) r[2] : null,
                r[3] != null ? (String) r[3] : null,
                toLocalDate(r[4]),
                r[5] != null ? (String) r[5] : null,
                r[6] != null ? (String) r[6] : null
        );
    }

    @SuppressWarnings("unchecked")
    private List<String> getPermissions(String email) {
        String sql = """
                SELECT p.PERMISSION_CODE FROM PERMISSION p
                JOIN ROLE_PERMISSION rp ON rp.PERMISSION_ID = p.ID
                JOIN DIM_ROLES r ON r.ID = rp.ROLE_ID
                JOIN USERS u ON u.ROLE_ID = r.ROLE_CODE
                WHERE u.GMAIL = :email
                """;
        return em.createNativeQuery(sql)
                .setParameter("email", email)
                .getResultList();
    }

    private LocalDate toLocalDate(Object value) {
        if (value == null) return null;
        if (value instanceof LocalDate ld) return ld;                          // thêm case này
        if (value instanceof java.sql.Date d) return d.toLocalDate();
        if (value instanceof Timestamp ts) return ts.toLocalDateTime().toLocalDate();
        if (value instanceof String s) return LocalDate.parse(s);
        return null;
    }
}