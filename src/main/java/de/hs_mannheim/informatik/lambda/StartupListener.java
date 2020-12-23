package de.hs_mannheim.informatik.lambda;

import de.hs_mannheim.informatik.lambda.model.Product;
import de.hs_mannheim.informatik.lambda.model.Stock;
import de.hs_mannheim.informatik.lambda.repository.StockRepository;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private final StockRepository stockRepository;

    public StartupListener(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        stockRepository.deleteAll();
        stockRepository.save(new Stock(null, new Product(new ObjectId().toString(), "Arduino Nano", 20, "blabla", "Arduino", "arduino.jpg"), 20));
        stockRepository.save(new Stock(null, new Product(new ObjectId().toString(), "Raspberry PI", 45, "blabla", "Raspberry", "Raspberry_Pi_4.jpg"), 50));
    }

}
