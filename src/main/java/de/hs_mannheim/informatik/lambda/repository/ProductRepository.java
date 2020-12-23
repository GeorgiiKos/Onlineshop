package de.hs_mannheim.informatik.lambda.repository;

import de.hs_mannheim.informatik.lambda.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}
