package de.hs_mannheim.informatik.lambda.repository;

import de.hs_mannheim.informatik.lambda.model.UserHit;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHitRepository extends CassandraRepository<UserHit, String> {
}
