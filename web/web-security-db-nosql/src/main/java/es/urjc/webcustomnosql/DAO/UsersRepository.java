package es.urjc.webcustomnosql.DAO;

import es.urjc.webcustomnosql.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<User, String> {
    public User findByUsername(String username);

}
