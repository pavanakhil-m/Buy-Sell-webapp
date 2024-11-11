package com.labweek.menumate.controller;

import com.labweek.menumate.dto.NewProductDto;
import com.labweek.menumate.entity.NewProductEntity;
import com.labweek.menumate.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService.S3Service s3Service;

    @Autowired
    private ProductService productService; // private ProductService productAddService;

    // CREATING!!
    @PostMapping("/add")
    public ResponseEntity<NewProductDto> addProduct(
            @RequestParam("ntId") String ntId,
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("purchaseDate") String purchaseDate,
            @RequestParam(value = "dateListed", required = false) String dateListed,
            @RequestParam("price") Double price,  // Change to Double
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile image) {

        System.out.println("CREATING");



        if (dateListed == null || dateListed.isEmpty()) {
            dateListed = LocalDateTime.now().toString();  // This will include date and time with seconds
        }


        String imageUrl = null;
        try {
            // Upload the image to S3 and get the URL
            imageUrl = s3Service.uploadFile(image);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);  // Handle error if image upload fails
        }

        // Create DTO with image bytes
        NewProductDto newProductDto = NewProductDto.builder()
                .ntId(ntId)
                .productName(productName)
                .description(description)
                .purchaseDate(purchaseDate)
                .dateListed(dateListed)
                .price(price)
                .image(imageUrl)// Add image data
                .category(category)
                .build();

        // Call the service to add the product
        NewProductDto addedProduct = productService.addProduct(newProductDto);

        return ResponseEntity.ok(addedProduct);
    }

    // READING!!!
    @GetMapping("/{productId}")
    public ResponseEntity<NewProductDto> getProductById(@PathVariable Long productId) {
        NewProductDto product = productService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // UPDATING!!!!

  //  @PutMapping("/{productId}")
  @PutMapping("/{productId}")
    public ResponseEntity<NewProductDto> updateProduct(
            @PathVariable Long productId,
            @RequestParam("ntId") String ntId,
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("purchaseDate") String purchaseDate,
            @RequestParam(value = "dateListed", required = false) String dateListed,
            @RequestParam("price") Double price,
            @RequestParam("category") String category,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        System.out.println("UPDATING");

      if (dateListed == null || dateListed.isEmpty()) {
          dateListed = LocalDateTime.now().toString();  // This will include date and time with seconds
      }


      String imageUrl = null;
      try {
          // Upload the image to S3 and get the URL
          imageUrl = s3Service.uploadFile(image);
      } catch (Exception e) {
          e.printStackTrace();
          return ResponseEntity.status(500).body(null);  // Handle error if image upload fails
      }

        // Create DTO with image bytes
        NewProductDto updatedProductDto = NewProductDto.builder()
                .ntId(ntId)
                .productName(productName)
                .description(description)
                .purchaseDate(purchaseDate)
                .dateListed(dateListed)
                .price(price)
                .category(category)
                .image(imageUrl)
                .build();

        // Call the service to update the product
        NewProductDto updatedProduct = productService.updateProduct(productId, updatedProductDto);

        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETING!!!!

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId)
    {
        System.out.println("DELETING");
        boolean isDeleted = productService.deleteProductById(productId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //FILTERING

    @GetMapping("/category/{category}")
    public ResponseEntity<List<NewProductEntity>> getProductsByCategory(@PathVariable String category) {
        System.out.println("get category");
        // Call the service to get products by category
        List<NewProductEntity> products = productService.getProductsByCategory(category);
        // Check if products were found for the category
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if no products found
            }
        // Return the list of products found for the category
        return ResponseEntity.ok(products);}


    // New endpoint to get recently listed products
    @GetMapping("/recent")
    public ResponseEntity<List<NewProductEntity>> getRecentlyListedProducts() {
        List<NewProductEntity> recentProducts = productService.getRecentlyListedProducts();
        return recentProducts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(recentProducts);
    }

    // New endpoint for searching products by keyword
    @GetMapping("/search")
    public ResponseEntity<List<NewProductEntity>> searchProducts(@RequestParam("keyword") String keyword) {
        List<NewProductEntity> searchResults = productService.searchProducts(keyword);
        return searchResults.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(searchResults);
    }

    @GetMapping("/sort/price")
    public ResponseEntity<List<NewProductEntity>> getProductsSortedByPrice(
            @RequestParam(value = "order", defaultValue = "asc") String order) {
        List<NewProductEntity> products;

        if ("desc".equalsIgnoreCase(order)) {
            products = productService.getProductsSortedByPriceDesc();
        } else {
            products = productService.getProductsSortedByPriceAsc();
        }

        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }



}