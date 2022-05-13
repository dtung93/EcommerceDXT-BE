package tech.getarrays.apimanager.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CheckoutController {
    @Value("api_public_key")
    private String stripePublicKey;

//    @RequestMapping("/checkout")
//    public String checkout(Model model) {
//        model.addAttribute("amount", 50 * 100);
//        model.addAttribute("stripePublicKey", stripePublicKey);
//        model.addAttribute("currency", ChargeRequest.Currency.USD);
//        return "checkout";
//    }
}
