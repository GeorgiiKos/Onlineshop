package de.hs_mannheim.informatik.lambda.controller;

import de.hs_mannheim.informatik.lambda.model.Bill;
import de.hs_mannheim.informatik.lambda.model.Clickstream;
import de.hs_mannheim.informatik.lambda.model.Product;
import de.hs_mannheim.informatik.lambda.model.User;
import de.hs_mannheim.informatik.lambda.repository.BillRepository;
import de.hs_mannheim.informatik.lambda.repository.ClickstreamRepository;
import de.hs_mannheim.informatik.lambda.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
public class LambdaController {

	private final BillRepository billRepository;
	private final ClickstreamRepository clickstreamRepository;
	private final ProductRepository productRepository;

	public LambdaController(BillRepository billRepository, ClickstreamRepository clickstreamRepository, ProductRepository productRepository) {
		this.billRepository = billRepository;
		this.clickstreamRepository = clickstreamRepository;
		this.productRepository = productRepository;
	}

	@GetMapping("/shop")
	public String shop() {
		return "/shop";
	}

	@PostMapping("/bestellen")
	public String bestellen(@RequestParam String artikel, @RequestParam int anzahl, Model model) {
		System.out.println("Eine Bestellung...");

		if ("arduino".equals(artikel)) {
			Product product = new Product();
			product.setName("Arduino");
			product.setDescription("aslkfjsalkdjf");
			product.setInStock(50);
			product.setPrice("30$");

			User user = new User();
			user.setName("Max Mustermann");
			user.setEmail("email@domain.de");
			user.setGender("Male");

			Bill bill = new Bill();
			bill.setBillAddress("Paul Wittsack");
			bill.setPayment("Paypal");
			bill.setProduct(product);
			bill.setAmount(1);
			bill.setFinalPrice("30$");

			bill.setUser(user);

			billRepository.save(bill);

			model.addAttribute("message", anzahl + " " + artikel + " bestellt. Herzlichen Dank!");
			return "shop";
		}

		model.addAttribute("message", "Fehler bei der Bestellung.");
		return "shop";
	}

	@GetMapping("/record")
	@ResponseBody
	public String recordEvents(@RequestParam int x, @RequestParam int y, @RequestParam String src, @RequestParam String type, HttpServletRequest request) {
		System.out.println(src + ": " + x + " " + y + " " + type);
		System.out.println(request.getRemoteAddr());
		Clickstream clickstream = new Clickstream(LocalDateTime.now().toString(), request.getRemoteAddr(), x, y, type, "bestellen");

		clickstreamRepository.save(clickstream);

		return "ok";
	}

	@GetMapping("/overview")
	public String overview(ModelMap map) {
		ModelAndView mv = new ModelAndView();
		map.addAttribute("productList", productRepository.findAll());
		return "overview";
	}

	@GetMapping("/shopping-cart")
	public String shoppingCart(ModelMap map) {
		return "shopping-cart";
	}

	@GetMapping("/purchase")
	public String purchase() {
		return "shop";
	}

}
