package dummy.actual.entity.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dummy.actual.entity.Possession;

@Repository
@Transactional
public interface PossessionRepositry extends JpaRepository<Possession, Long> {

}
