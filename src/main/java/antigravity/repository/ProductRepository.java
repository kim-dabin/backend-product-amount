package antigravity.repository;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ProductRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Promotion getPromotion(int id) {
        String query = "SELECT * FROM PROMOTION WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(
            query,
            params,
            (rs, rowNum) -> Promotion.builder()
                    .id(rs.getInt("id"))
                    .promotionType(rs.getString("promotion_type"))
                    .name(rs.getString("name"))
                    .discountType(rs.getString("discount_type"))
                    .discountValue(rs.getInt("discount_value"))
                    .useStartedAt(rs.getDate("use_started_at"))
                    .useEndedAt(rs.getDate("use_ended_at"))
                    .build()
        );
    }

    public List<PromotionProducts> getPromotionProducts(int productId) {
        String query = "SELECT * FROM PROMOTION_PRODUCTS WHERE product_id = :productId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        return namedParameterJdbcTemplate.query(
                query,
                params,
                (rs, rowNum) -> PromotionProducts.builder()
                        .id(rs.getInt("id"))
                        .promotionId(rs.getInt("promotion_id"))
                        .productId(rs.getInt("product_id"))
                        .build()
        );
    }

    public Product getProduct(int id) {
        String query = "SELECT * FROM `product` WHERE id = :id ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(
                query,
                params,
                (rs, rowNum) -> Product.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .price(rs.getInt("price"))
                        .build()
        );
    }
}
