package de.hs_mannheim.informatik.lambda.repository;

import de.hs_mannheim.informatik.lambda.model.Clickstream;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickstreamRepository extends CassandraRepository<Clickstream, String> {
}
