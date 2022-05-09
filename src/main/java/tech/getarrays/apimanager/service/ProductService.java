package tech.getarrays.apimanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tech.getarrays.apimanager.exception.ProductNotFoundException;
import tech.getarrays.apimanager.model.Cart;
import tech.getarrays.apimanager.repo.ProductRepo;
import tech.getarrays.apimanager.model.Product;

import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepo productRepo;
    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }
    public Product addProduct(Product product){
        product.setProductcode(UUID.randomUUID().toString());
        return productRepo.save(product);
    }
    public Page<Product> findAllProducts( Pageable paging){
    return (Page<Product>) productRepo.findAll(paging);
    }
    public Product updateProduct(Product product){
        return productRepo.save(product);
    }
    public Product findProductById(Long id){
        return  productRepo.findProductById(id).orElseThrow(()->new ProductNotFoundException("Product with id= "+id +" was not found"));
}
    public void deleteProduct(Long id){
        productRepo.deleteProductById(id);
    }
    public Page<Product> findByName(String name, Pageable paging){
        return (Page<Product>) productRepo.findByNameContaining(name,paging);
    }
    public Page<Product> findByCategory(String category,Pageable paging){
        return (Page<Product>) productRepo.findByCategoryContaining(category, paging);
    }
    public Page<Product> findByNameAndCategory(String category,String name,Pageable paging){
        return (Page<Product>) productRepo.findProductByNameContainingAndCategoryContaining(category,name,paging);
    }
    public Page<Product> findAllSortedByAscendingPrice(Pageable paging){
        return (Page<Product>) productRepo.findAllByOrderByPriceAsc(paging);
    }
    public Page<Product> findAllSortedByDescendingPrice(Pageable paging){
        return (Page<Product>) productRepo.findAllByOrderByPriceDesc(paging);
    }

    }

