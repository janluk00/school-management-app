package repository;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.RoleEntity;
import com.janluk.schoolmanagementapp.common.model.vo.RoleType;
import com.janluk.schoolmanagementapp.common.repository.port.RoleRepository;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryRoleRepository implements RoleRepository {

    private final Map<String, RoleEntity> roles;

    public InMemoryRoleRepository() {
        roles = Stream.of(RoleType.values())
                .collect(Collectors.toMap(Enum::name, roleType -> new RoleEntity(roleType.name())));
    }

    @Override
    public RoleEntity getById(String role) {
        RoleEntity r = roles.get(role);

        if (r == null) {
            throw new NoResultFoundException("Could not find role: %s".formatted(role));
        }

        return r;
    }
}
