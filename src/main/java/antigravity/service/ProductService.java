package antigravity.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;
    
    @Transactional
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        // System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");
        int productId = request.getProductId();
        System.out.println("productId: " + productId);
        Product product = repository.getProduct(productId);
        List<PromotionProducts> promotionProducts = repository.getPromotionProducts(productId);

        if(product != null && promotionProducts != null) {
            
            String name = product.getName();
            int originPrice = product.getPrice();

            List<Promotion> promotions = new ArrayList<>();
            for (PromotionProducts pp : promotionProducts) {
                promotions.add(repository.getPromotion(pp.getPromotionId()));
            }
            
            // 할인이 적용된 가격
            int discountPrice = calculateDiscountPrice(originPrice, promotions);
            // 최종 상품 금액은 천단위 절삭
            int finalPrice = truncateToThousands(discountPrice);

            return ProductAmountResponse.builder()
                .name(name)
                .originPrice(originPrice)
                .discountPrice(discountPrice)
                .finalPrice(finalPrice)
                .build();
        }
        return null;
    }

    public int calculateDiscountPrice(int originPrice, List<Promotion> promotions) {
        int coupon = 0;
        int finalPrice = originPrice;
        if(promotions.size() > 0)   {
            for (Promotion promotion : promotions ) {
                String promotionType = promotion.getPromotionType();
                int discountValue = promotion.getDiscountValue();
                
                if(promotionType.equals("CODE")) {
                    finalPrice *= (1 - discountValue * 0.01);
                } else if(promotionType.equals("COUPON")) {
                    coupon += discountValue;
                }
            }
        }
        System.out.println("finalPrice: " + finalPrice);
        return finalPrice - coupon;
    }

    public int truncateToThousands(int price) {
        return (int) (Math.floor(price / 1000) * 1000);
    }
}
