package de.hs_mannheim.informatik.lambda.repository;

import de.hs_mannheim.informatik.lambda.model.ShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {
}
