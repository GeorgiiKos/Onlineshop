package de.hs_mannheim.informatik.lambda.controller;

import de.hs_mannheim.informatik.lambda.model.*;
import de.hs_mannheim.informatik.lambda.repository.BillRepository;
import de.hs_mannheim.informatik.lambda.repository.ClickstreamRepository;
import de.hs_mannheim.informatik.lambda.repository.ShoppingCartRepository;
import de.hs_mannheim.informatik.lambda.repository.StockRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
public class LambdaController {

    private final BillRepository billRepository;
    private final ClickstreamRepository clickstreamRepository;
    private final StockRepository stockRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public LambdaController(BillRepository billRepository, ClickstreamRepository clickstreamRepository, StockRepository stockRepository, ShoppingCartRepository shoppingCartRepository) {
        this.billRepository = billRepository;
        this.clickstreamRepository = clickstreamRepository;
        this.stockRepository = stockRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @GetMapping("/")
    public String redirect() {
        return "redirect:/overview";
    }

    @GetMapping("/record")
    @ResponseBody
    public String recordEvents(@RequestParam int x, @RequestParam int y, @RequestParam String src, @RequestParam String type, @RequestParam String webpage, HttpServletRequest request) {
        System.out.println(src + ": " + x + " " + y + " " + type);
        System.out.println(request.getRemoteAddr());
        Clickstream clickstream = new Clickstream(LocalDateTime.now().toString(), request.getRemoteAddr(), x, y, type, webpage);

        clickstreamRepository.save(clickstream);

        return "ok";
    }

    @GetMapping("/overview")
    public String overview(ModelMap map, HttpSession session) {
        System.out.println(session.getId());
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
        // TODO: update products
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

    @GetMapping("/add-shopping-cart")
    public String addToShoppingCart(@ModelAttribute AddCartRequest addCartRequest, HttpSession session) {
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
        return "redirect:/overview";
    }

}
