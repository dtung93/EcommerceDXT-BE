package tech.getarrays.apimanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.getarrays.apimanager.exception.ProductNotFoundException;
import tech.getarrays.apimanager.payload.Products;
import tech.getarrays.apimanager.repo.ProductRepo;
import tech.getarrays.apimanager.model.Product;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product addProduct(Product product) {
        product.setProductcode(UUID.randomUUID().toString());
        return productRepo.save(product);
    }

    public Page<Product> findAllProducts(Pageable paging) {
        return (Page<Product>) productRepo.findAll(paging);
    }

    public Product updateProduct(Product product) {
        return productRepo.save(product);
    }

    public Product findProductById(Long id) {
        return productRepo.findProductById(id).orElseThrow(() -> new ProductNotFoundException("Product with id= " + id + " was not found"));
    }

    public void deleteProduct(Long id) {
        productRepo.deleteProductById(id);
    }

    public Page<Product> findByName(String name, Pageable paging) {
        return (Page<Product>) productRepo.findByNameContaining(name, paging);
    }

    public Page<Product> findByCategory(String category, Pageable paging) {
        return (Page<Product>) productRepo.findByCategoryContaining(category, paging);
    }

    public Page<Product> findByNameAndCategory(String name, String category, Pageable paging) {
        return (Page<Product>) productRepo.findByNameContainingAndCategoryContaining(name, category, paging);
    }

    public Page<Product> findAllSortedByAscendingPrice(Pageable paging) {
        return (Page<Product>) productRepo.findAllByOrderByPriceAsc(paging);
    }

    public Page<Product> findAllSortedByDescendingPrice(Pageable paging) {
        return (Page<Product>) productRepo.findAllByOrderByPriceDesc(paging);
    }

    public Page<Product> findByNameAndCategoryPriceAscending(String name, String category, Pageable paging) {
        return (Page<Product>) productRepo.findProductByNameContainingAndCategoryContainingOrderByPriceAsc(name, category, paging);
    }

    public Page<Product> findByNameAndCategoryPriceDescending(String name, String category, Pageable paging) {
        return (Page<Product>) productRepo.findProductByNameContainingAndCategoryContainingOrderByPriceDesc(name, category, paging);
    }
   public List<Product> getProducts(){
        return productRepo.findAll();
   }
    public Page<Product> getProducts(Products products, Pageable paging) {
        Page<Product> pageProds = null;
        if (products.getProductName() == null
                && products.getCategory() == null
                && products.getPage() == null
                && products.getPageSize() == null
                && products.getSort() == null
        )
            pageProds = this.findAllProducts(paging);
        else if(products.getProductName()!=null&&products.getCategory()!=null&&products.getSort()!=null&&products.getPageSize()!=null){
            if(products.getSort().equals("ascending"))
                pageProds=this.findByNameAndCategoryPriceAscending(products.getProductName(),products.getCategory(),paging);
            else if (products.getSort().equals("descending"))
                pageProds=this.findByNameAndCategoryPriceDescending(products.getProductName(), products.getCategory(), paging);
            else
                pageProds=this.findByNameAndCategory(products.getProductName(),products.getCategory(),paging);
        }
        else if (products.getProductName() != null && products.getCategory() != null)
            pageProds = this.findByNameAndCategory(products.getProductName(), products.getCategory(), paging);
        else if (products.getProductName() != null && products.getCategory() == null)
            pageProds = this.findByName(products.getProductName(), paging);
        else if (products.getProductName() == null && products.getCategory() != null)
            pageProds = this.findByCategory(products.getCategory(), paging);
        else if (products.getProductName() == null && products.getCategory() == null && products.getSort() != null) {
            if (products.getSort().equals("ascending"))
                pageProds = this.findAllSortedByAscendingPrice(paging);
            else if (products.getSort().equals("descending"))
                pageProds = this.findAllSortedByDescendingPrice(paging);
        } else if (products.getProductName() == null && products.getPageSize() != null)
            pageProds = this.findAllProducts(paging);
        else if (products.getProductName() != null && products.getPageSize() != null)
            pageProds = this.findByName(products.getProductName(), paging);
        else if (products.getProductName() != null && products.getCategory() != null && products.getSort() != null) {
            if (products.getSort().equals("ascending"))
                pageProds = this.findByNameAndCategoryPriceAscending(products.getProductName(), products.getCategory(), paging);
            else if (products.getSort().equals("descending"))
                pageProds = this.findByNameAndCategoryPriceDescending(products.getProductName(), products.getCategory(), paging);
        }
        else if (products.getProductName()!=null&&products.getPageSize()!=null)
            pageProds=this.findByName(products.getProductName(),paging);
        else if(products.getProductName()!=null&&products.getCategory()!=null&&products.getPageSize()!=null)
            pageProds=this.findByNameAndCategory(products.getProductName(),products.getCategory(),paging);

        else
            pageProds=this.findAllProducts(paging);
        return pageProds;
    }
}

