package tech.getarrays.apimanager.controller;

import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.apimanager.exception.StatusCode;
import tech.getarrays.apimanager.model.Product;
import tech.getarrays.apimanager.payload.Products;
import tech.getarrays.apimanager.exception.ResponseError;
import tech.getarrays.apimanager.payload.ResponseData;
import tech.getarrays.apimanager.service.ProductService;
import tech.getarrays.apimanager.service.UserService;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@Transactional
public class ProductController {
    private ProductService productService;
    private UserService userService;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }
    @PostMapping("/products")
    public ResponseEntity<?> getProducts(@RequestBody Products products){
        ResponseData responseData=new ResponseData();
        try {
            List<Product> listproducts;
            Pageable paging = PageRequest.of(
                    (products.getPage()!=null)?products.getPage():0,
                    (products.getPageSize()!=null)? products.getPageSize() : 8);
            Page<Product> pageProds;
            pageProds = productService.getProducts(products, paging);
            listproducts = pageProds.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("products", listproducts);
            response.put("currentPage", pageProds.getNumber());
            response.put("totalItems", pageProds.getTotalElements());
            response.put("totalPages", pageProds.getTotalPages());
            responseData.setStatusCode(StatusCode.SuccessfulRequest);
            responseData.setMapData("response",response);
        } catch (Exception e) {
            ResponseError error=new ResponseError();
            error.setErrorCode(StatusCode.InternalError);
            error.setErrorMessage(e.getMessage());
            error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
            logger.error(e.getMessage(),e);
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }



    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Product product = productService.findProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("/product/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product newProduct = productService.addProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/product/update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        Product updateProduct = productService.updateProduct((product));
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
   public ResponseEntity<?> deleteProduct(@PathVariable("id")Long id){
        ResponseData responseData=new ResponseData();
      try {
          productService.deleteProduct(id);
          responseData.setStatusCode(StatusCode.SuccessfulRequest);
          responseData.setMapData("response",1);
      }
      catch(Exception e){
          ResponseError responseError=new ResponseError();
          responseError.setStatusCode(StatusCode.InternalError);
          responseError.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
          logger.error(e.getMessage(),e);
          responseError.setErrorMessage(e.getMessage());
      }
      return new ResponseEntity<>(responseData,HttpStatus.OK);
   }
}
