package dummy.actual.entity.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dummy.actual.entity.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

}
