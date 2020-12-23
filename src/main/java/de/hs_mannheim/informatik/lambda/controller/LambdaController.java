package de.hs_mannheim.informatik.lambda.controller;

import de.hs_mannheim.informatik.lambda.model.AddCartRequest;
import de.hs_mannheim.informatik.lambda.model.Clickstream;
import de.hs_mannheim.informatik.lambda.model.ShoppingCart;
import de.hs_mannheim.informatik.lambda.model.Stock;
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
		return "shopping-cart";
	}

	@GetMapping("/purchase")
	public String purchase() {
		return "shop";
	}

	@GetMapping("/add-shopping-cart")
	public String addToShoppingCart(@ModelAttribute AddCartRequest addCartRequest, HttpSession session) {  // TODO: improve read/write
		System.out.println("Added to shopping cart " + addCartRequest.getStockId());
		Stock stock = stockRepository.findById(addCartRequest.getStockId()).orElse(null);
		ShoppingCart shoppingCart = shoppingCartRepository.findById(session.getId()).orElse(new ShoppingCart(session.getId(), new ArrayList<>()));
		shoppingCart.getProducts().add(stock.getProduct());
		shoppingCartRepository.save(shoppingCart);
		return "redirect:/overview";
	}

}
