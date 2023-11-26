package liberadzkih.seleniumscrapper.repository;

import java.util.List;

import liberadzkih.seleniumscrapper.model.OlxItem;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OlxItemRepository extends JpaRepository<OlxItem, String> {

    @Cacheable(cacheNames = "olx_item_find_all")
    List<OlxItem> findAll();

    @Caching(evict = {@CacheEvict(cacheNames = "olx_item_find_all", allEntries = true)})
    OlxItem save(final OlxItem olxItem);
}
