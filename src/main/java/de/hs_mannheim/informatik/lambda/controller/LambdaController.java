package de.hs_mannheim.informatik.lambda.controller;

import de.hs_mannheim.informatik.lambda.model.*;
import de.hs_mannheim.informatik.lambda.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Controller
public class LambdaController {

    private final BillRepository billRepository;
    private final ClickstreamRepository clickstreamRepository;
    private final StockRepository stockRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserHitRepository userHitRepository;
    private final HashMap<String, String> locationData;

    public LambdaController(BillRepository billRepository, ClickstreamRepository clickstreamRepository, StockRepository stockRepository, ShoppingCartRepository shoppingCartRepository, UserHitRepository userHitRepository) {
        this.billRepository = billRepository;
        this.clickstreamRepository = clickstreamRepository;
        this.stockRepository = stockRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.userHitRepository = userHitRepository;

        locationData = new LinkedHashMap<>();
        locationData.put("u33dc0cpnp45", "Germany");
        locationData.put("dr5rtwccpbpb", "USA");
//        locationData.put("", "China");
//        locationData.put("", "Brazil");
//        locationData.put("", "Canada");
//        locationData.put("", "South Africa");
    }

    @GetMapping("/")
    public String redirect(HttpServletRequest request) {
        System.out.println("redirected");
        String geohash = (String) locationData.keySet().toArray()[(int) (Math.random() * locationData.size())];
        System.out.println(geohash);
        userHitRepository.save(new UserHit(LocalDateTime.now().toString(), request.getRemoteAddr(), geohash, locationData.get(geohash)));
        return "redirect:/overview";
    }

    @GetMapping("/record")
    @ResponseBody
    public String recordEvents(@RequestParam String sourceElement, @RequestParam String webpage, HttpServletRequest request) {
        Clickstream clickstream = new Clickstream(LocalDateTime.now().toString(), request.getRemoteAddr(), sourceElement, webpage);

        clickstreamRepository.save(clickstream);

        return "ok";
    }

    @GetMapping("/overview")
    public String overview(ModelMap map, HttpSession session) {
        map.addAttribute("stockList", stockRepository.findAll());
        map.addAttribute("addCartRequest", new AddCartRequest());
        return "overview";
    }

    @GetMapping("/shopping-cart")
    public String shoppingCart(ModelMap map, HttpSession session) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(session.getId()).orElse(new ShoppingCart(session.getId(), new ArrayList<>()));
        map.addAttribute("shoppingCart", shoppingCart);
        map.addAttribute("paymentInfo", new PaymentInfo());
        return "shopping-cart";
    }

    @GetMapping("/purchase")
    public String purchase(@ModelAttribute PaymentInfo paymentInfo, HttpSession session) {
        // find shopping cart
        ShoppingCart shoppingCart = shoppingCartRepository.findById(session.getId()).orElse(new ShoppingCart(session.getId(), new ArrayList<>()));
        // save form
        for (ProductAmount productAmount : shoppingCart.getProductAmounts()) {
            Stock stock = stockRepository.findByProductId(productAmount.getProduct().getId());
            stock.setInStock(stock.getInStock() - productAmount.getAmount());
            if (stock.getInStock() < 0) {
                System.out.println("Sold too many products");
            }
            stockRepository.save(stock);
        }
        billRepository.save(new Bill(null, paymentInfo, shoppingCart.getProductAmounts(), shoppingCart.getTotalCost()));
        // remove shopping cart
        shoppingCartRepository.delete(shoppingCart);

        return "redirect:/overview";
    }

    @PostMapping("/add-shopping-cart")
    public String addToShoppingCart(@ModelAttribute AddCartRequest addCartRequest, HttpSession session, Model model) {
        // find product stock
        Stock stock = stockRepository.findById(addCartRequest.getStockId()).orElseThrow(() -> new RuntimeException("Product not found"));
        // find shopping cart
        ShoppingCart shoppingCart = shoppingCartRepository.findById(session.getId()).orElse(new ShoppingCart(session.getId(), new ArrayList<>()));
        // add product to the shopping cart
        boolean present = false;
        for (ProductAmount productAmount : shoppingCart.getProductAmounts()) {
            if (productAmount.getProduct().getId().equals(stock.getProduct().getId())) {
                productAmount.setAmount(productAmount.getAmount() + addCartRequest.getAmount());
                present = true;
            }
        }
        if (!present) {
            shoppingCart.getProductAmounts().add(new ProductAmount(stock.getProduct(), addCartRequest.getAmount()));
        }
        shoppingCartRepository.save(shoppingCart);

        model.addAttribute("message", addCartRequest.getAmount() + " " + stock.getProduct().getName() + " bestellt. Herzlichen Dank!");
        return "redirect:/overview";
    }

}
