package tech.getarrays.apimanager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.apimanager.model.Product;
import tech.getarrays.apimanager.service.ProductService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.hasLength;

@RestController
@RequestMapping("/api")
@Transactional
public class ProductResource {
    private final ProductService productService;
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/Products")
    public ResponseEntity<Map<String,Object>>getAllProducts(
            @RequestParam(required=false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        try {
   List<Product> products;
   Pageable paging=PageRequest.of(page,size);
   Page<Product> pageProds ;
   if(name==null&&category==null)
        pageProds=productService.findAllProducts(paging);
   else if(name==null&&category!=null)
       pageProds=productService.findByCategory(category,paging);
   else if(name!=null&&category==null)
       pageProds=productService.findByName(name,paging);
   else
       pageProds=productService.findByNameAndCategory(name,category,paging);
   products=pageProds.getContent();
   Map<String,Object> response=new HashMap<>();
   response.put("products",products);
   response.put("currentPage",pageProds.getNumber());
   response.put("totalItems",pageProds.getTotalElements());
   response.put("totalPages",pageProds.getTotalPages());
   return new ResponseEntity<>(response,HttpStatus.OK);
            }
        catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Products/Detail/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id){
        Product product=productService.findProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("/Add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product newProduct=productService.addProduct(product);
        return new ResponseEntity<>(newProduct,HttpStatus.CREATED);
    }
    @PutMapping("/Update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product){
        Product updateProduct=productService.updateProduct((product));
        return new ResponseEntity<>(updateProduct,HttpStatus.OK);
    }
    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id")Long id){
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
 @GetMapping
    public List<Product> list(@RequestParam(required=false)String name,@RequestParam(required=false) String category,Pageable paging){
        if(!hasLength(name)&&!hasLength(category)){
            return (List<Product>) productService.findAllProducts(paging);
        }
        else if(!hasLength(name)&&hasLength(category)){
            return (List<Product>) productService.findByCategory(category,paging);
        }
        else if(hasLength(name)&&!hasLength(category)){
            return (List<Product>) productService.findByName(name,paging);
        }

       else if(hasLength(name)&&hasLength(category))
       { return (List<Product>) productService.findByNameAndCategory(category,name,paging);}
       return null;
 }
}
