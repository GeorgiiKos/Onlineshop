package de.hs_mannheim.informatik.lambda.repository;

import de.hs_mannheim.informatik.lambda.model.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends MongoRepository<Bill, String> {
}
