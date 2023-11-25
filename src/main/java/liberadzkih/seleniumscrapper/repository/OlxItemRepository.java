package liberadzkih.seleniumscrapper.repository;

import liberadzkih.seleniumscrapper.model.OlxItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OlxItemRepository extends JpaRepository<OlxItem, String> {

}
