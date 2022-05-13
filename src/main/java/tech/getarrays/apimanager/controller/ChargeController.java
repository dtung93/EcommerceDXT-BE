package tech.getarrays.apimanager.controller;

import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.apimanager.payload.ChargeRequest;
import tech.getarrays.apimanager.payload.ChargeResponse;
import tech.getarrays.apimanager.service.CartService;
import tech.getarrays.apimanager.service.ProductService;
import tech.getarrays.apimanager.service.StripeService;

import java.util.Date;

@RestController
@RequestMapping("/api/payment")
public class ChargeController {

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private StripeService stripeService;

    @PostMapping("/charge")
    public ResponseEntity<ChargeResponse> chargePayment(@RequestBody ChargeRequest chargeRequest) throws Exception {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.updateProductStock(username);
        chargeRequest.setDescription("TEST");
        chargeRequest.setCurrency(ChargeRequest.Currency.USD);
        chargeRequest.setStripeToken(chargeRequest.getStripeToken());
        chargeRequest.setAmount(chargeRequest.getAmount());
        Charge charge=stripeService.charge(chargeRequest);
        ChargeResponse chargeResponse=new ChargeResponse();
        chargeResponse.setId(charge.getId());
        chargeResponse.setOrderDate(new Date());
        chargeResponse.setStatus(charge.getStatus());
        chargeResponse.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        chargeResponse.setTransactionId(charge.getBalanceTransaction());
        return new ResponseEntity<ChargeResponse>(chargeResponse,HttpStatus.OK);
    }

    @ExceptionHandler(StripeException.class)
    public String handleStripeError(Model model,StripeException stripeException){
        model.addAttribute("Error",stripeException.getMessage());
        return "result";
    }
}
