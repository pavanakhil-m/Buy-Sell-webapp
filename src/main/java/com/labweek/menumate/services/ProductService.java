package com.labweek.menumate.services;

import com.labweek.menumate.dto.NewProductDto;
import com.labweek.menumate.entity.NewProductEntity;
import com.labweek.menumate.exceptions.NoProductFoundException;
import com.labweek.menumate.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Service
    public class S3Service {

        private static final String BUCKET_NAME = "comcastbuysell";  // Replace with your S3 bucket name

        private final S3Client s3Client;

        public S3Service() {
            this.s3Client = S3Client.builder()
                    .region(Region.AP_SOUTH_1)  // Choose the appropriate region
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                            "AKIATCKATV5MXBB72BLS",  // Replace with your access key
                            "Uw9JiE2f71y+XsssgVoJkSU1SZI+cp3AuWOPLS24"  // Replace with your secret key
                    )))
                    .build();
        }

        public String uploadFile(MultipartFile file) throws Exception {
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            try (InputStream inputStream = file.getInputStream()) {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(fileName)
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

                // Return the file URL
                return "https://" + BUCKET_NAME + ".s3.amazonaws.com/" + fileName;
            }
        }
    }



    @Transactional
    public NewProductDto updateProduct(Long productId, NewProductDto updatedProductDto) {
        Optional<NewProductEntity> existingProductOpt = productRepository.findById(productId);

        if (existingProductOpt.isPresent()) {
            NewProductEntity existingProduct = existingProductOpt.get();

            // Update fields

            existingProduct.setNtId(updatedProductDto.getNtId());
            existingProduct.setProductName(updatedProductDto.getProductName());
            existingProduct.setDescription(updatedProductDto.getDescription());
            existingProduct.setPurchaseDate(updatedProductDto.getPurchaseDate());
            existingProduct.setDateListed(LocalDateTime.now());
            existingProduct.setPrice(updatedProductDto.getPrice());
            existingProduct.setCategory(updatedProductDto.getCategory());

            // Update image if provided
            if (updatedProductDto.getImage() != null) {
                existingProduct.setImage(updatedProductDto.getImage());
            }

            productRepository.save(existingProduct);

            return updatedProductDto;
        } else {
            return null; // Product not found
        }
    }

    @Transactional
    public boolean deleteProductById(Long productId) {
        // Check if the product exists
        Optional<NewProductEntity> product = productRepository.findById(productId);
        if (product.isPresent()) {
            // If the product exists, delete it
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }


    @Transactional
    public NewProductDto addProduct(NewProductDto newProductDto) {
        // Convert NewProductDto to NewProductEntity
        NewProductEntity productEntity = NewProductEntity.builder()
                .productName(newProductDto.getProductName())
                .description(newProductDto.getDescription())
                .purchaseDate(newProductDto.getPurchaseDate())
                .ntId(newProductDto.getNtId())
                .price(newProductDto.getPrice())  // Use Double for price
                .dateListed(LocalDateTime.now())
                .image(newProductDto.getImage())
                .category(newProductDto.getCategory())
                .build();

        // Save to database
        NewProductEntity savedProduct = productRepository.save(productEntity);

        // Convert saved entity back to NewProductDto
        NewProductDto savedProductDto = new NewProductDto();
        savedProductDto.setProductName(savedProduct.getProductName());
        savedProductDto.setDescription(savedProduct.getDescription());
        savedProductDto.setNtId(savedProduct.getNtId());
        savedProductDto.setPrice(savedProduct.getPrice());  // Use Double for price
        savedProductDto.setPurchaseDate(savedProduct.getPurchaseDate());
        savedProductDto.setDateListed(savedProduct.getDateListed().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        savedProductDto.setImage(savedProduct.getImage());// Add image field in the DTO
        savedProductDto.setCategory(savedProduct.getCategory());

        return savedProductDto;
    }

    @Transactional
    public List<NewProductEntity> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);}


    @Transactional(readOnly = true)
    public NewProductDto getProductById(Long productId) {
        Optional<NewProductEntity> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            NewProductEntity productEntity = productOpt.get();
            return NewProductDto.builder()
                    .ntId(productEntity.getNtId())
                    .productName(productEntity.getProductName())
                    .description(productEntity.getDescription())
                    .purchaseDate(productEntity.getPurchaseDate())
                //    .dateListed(productEntity.getDateListed())
                    .price(productEntity.getPrice())
                    .category(productEntity.getCategory())
                    .image(productEntity.getImage())
                    .build();
        } else {
            return null; // Product not found
        }

}
    // New method to get recently listed products
    @Transactional
    public List<NewProductEntity> getRecentlyListedProducts() {
        return productRepository.findAllByOrderByDateListedDesc();
    }

    // Search for products by a keyword
    @Transactional
    public List<NewProductEntity> searchProducts(String keyword) {
        List<NewProductEntity> products = productRepository.searchProductsByName(keyword);

        if (products.isEmpty()) {
            throw new NoProductFoundException("No products exist with the provided keyword: " + keyword);
        }

        return products;
    }

    // Method to get products sorted by price (low to high)
    @Transactional
    public List<NewProductEntity> getProductsSortedByPriceAsc() {
        return productRepository.findAllByOrderByPriceAsc();  // Ascending order by price
    }

    // Method to get products sorted by price (high to low)
    @Transactional
    public List<NewProductEntity> getProductsSortedByPriceDesc() {
        return productRepository.findAllByOrderByPriceDesc();  // Descending order by price
    }
}