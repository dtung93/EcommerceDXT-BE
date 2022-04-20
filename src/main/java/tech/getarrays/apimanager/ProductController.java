package tech.getarrays.apimanager;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.apimanager.model.Product;
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

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getAllProducts(

            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        try {
            List<Product> products;
            Pageable paging = PageRequest.of(page, size);
            Page<Product> pageProds;
            if (name == null && category == null)
                pageProds = productService.findAllProducts(paging);
            else if (name == null && category != null)
                pageProds = productService.findByCategory(category, paging);
            else if (name != null && category == null)
                pageProds = productService.findByName(name, paging);
            else
                pageProds = productService.findByNameAndCategory(name, category, paging);
            products = pageProds.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("products", products);
            response.put("currentPage", pageProds.getNumber());
            response.put("totalItems", pageProds.getTotalElements());
            response.put("totalPages", pageProds.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
       productService.deleteProduct(id);
       return new ResponseEntity<>(HttpStatus.OK);
   }
    @GetMapping("/products/sort-by-price")
    public ResponseEntity<Map<String, Object>> sortProduct(
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        try {
            List<Product> products;
            Pageable paging = PageRequest.of(page, size);
            Page<Product> pageProds;
            if (value.equals("ascending")) {
                pageProds = productService.findAllSortedByAscendingPrice(paging);
            } else if (value.equals("descending")) {
                pageProds = productService.findAllSortedByDescendingPrice(paging);
            } else {
                pageProds = productService.findAllProducts(paging);
            }
            products = pageProds.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("products", products);
            response.put("currentPage", pageProds.getNumber());
            response.put("totalItems", pageProds.getTotalElements());
            response.put("totalPages", pageProds.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
