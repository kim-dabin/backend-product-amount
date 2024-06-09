package antigravity.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.model.request.ProductInfoRequest;
import antigravity.repository.ProductRepository;

@SpringBootTest
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetProductAmount() {
        // Given
        int productId = 1;
        int[] coupons = {1, 2};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(coupons)
            .build();

        Product product = Product.builder()
            .id(productId)
            .name("Test Product")
            .price(100000)
            .build();

        Promotion promotion1 = Promotion.builder()
            .id(1)
            .promotionType("CODE")
            .discountValue(15)
            .build();

        Promotion promotion2 = Promotion.builder()
            .id(2)
            .promotionType("COUPON")
            .discountValue(5000)
            .build();

        List<PromotionProducts> promotionProducts = Arrays.asList(
            PromotionProducts.builder()
                .id(1)
                .productId(productId)
                .promotionId(1)
                .build(), 
            PromotionProducts.builder()
                .id(2)
                .productId(productId)
                .promotionId(2)
                .build()
        );

        // When
        int discountPrice = productService.calculateDiscountPrice(product.getPrice(), Arrays.asList(promotion1, promotion2));
        int finalPrice = productService.truncateToThousands(discountPrice);


        // Then
        assertEquals(80000, discountPrice); // 15% discount and 5000 coupon
        assertEquals(80000, finalPrice); // Final price truncated to thousands
    }
}