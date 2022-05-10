package com.example.demo11.product;

import com.example.demo11.order.Order;
import com.example.demo11.order.OrderService;
import com.example.demo11.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/products")
public class ProductApi {

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<Product> getList(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "1") int limit) {
        return productService.findAll(page, limit);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/get-all")
    public List<Product> getList() {
        return productService.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Product save(@RequestBody Product product) {
        productService.save(product);
        return product;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public Product getDetail(@PathVariable int id) {
        return productService.findById(id).get();
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public boolean delete(@PathVariable int id) {
        productService.deleteById(id);
        return true;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public Product update(@PathVariable int id, @RequestBody Product updateProduct) {
        Product existing = productService.findById(id).get();
        existing.setName(updateProduct.getName());
        existing.setDescription(updateProduct.getDescription());
        existing.setPrice(updateProduct.getPrice());
        productService.save(existing);
        return updateProduct;
    }

    @RequestMapping(method = RequestMethod.GET, path = "add-order")
    public ResponseEntity<?> addProductToOrder(
            @RequestParam int orderId,
            @RequestParam int productId) {
        // check sự tồn tại cùa product
        Optional<Product> optionalProduct = productService.findById(productId) ;
        // check sự tồn tại của order
        Optional<Order> optionalOrder = orderService.findById(orderId) ;
        // trả về not found nếu một trong 2 trường hợp không tìm thấy
        if (!optionalProduct.isPresent() || !optionalOrder.isPresent()){
            return new ResponseEntity<>( "Not found", HttpStatus.NOT_FOUND) ;
        }
        boolean resutlt = productService.addProductToClass(optionalProduct.get(), optionalOrder.get());
        if (!resutlt) {
            return new ResponseEntity<>("Add product error!", HttpStatus.INTERNAL_SERVER_ERROR) ;
        }
        return new ResponseEntity<>( "Add product success!", HttpStatus.OK);
    }
}
