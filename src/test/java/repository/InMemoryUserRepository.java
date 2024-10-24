package repository;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.UserEntity;
import com.janluk.schoolmanagementapp.common.repository.port.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {
    private final Map<UUID, UserEntity> users = new HashMap<>();

    @Override
    public UserEntity getByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new NoResultFoundException("Could not find user with e-mail: %s".formatted(email)));
    }

    @Override
    public UserEntity getByEmailWithRoles(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new NoResultFoundException("Could not find user with e-mail: %s".formatted(email)));
    }

    @Override
    public UserEntity getByPasswordConfirmationToken(String passwordConfirmationToken) {
        return users.values().stream()
                .filter(user -> user.getPasswordConfirmationToken().equals(passwordConfirmationToken))
                .findFirst()
                .orElseThrow(() -> new NoResultFoundException(
                        "Could not find user with token: %s".formatted(passwordConfirmationToken)
                    )
                );
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.entrySet().stream()
                .anyMatch(uuidUserEntityEntry -> uuidUserEntityEntry.getValue().getEmail().equals(email));
    }

    @Override
    public UUID save(UserEntity user) {
        UUID userId = user.getId();

        users.put(userId, user);

        return userId;
    }

    public void deleteAll() {
        users.clear();
    }
}
