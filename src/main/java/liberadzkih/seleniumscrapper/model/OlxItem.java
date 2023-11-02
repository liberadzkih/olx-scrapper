package liberadzkih.seleniumscrapper.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "OLX_ITEM")
public class OlxItem {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "URL")
    private String url;

    @Column(name = "SEARCH_URL")
    private String searchUrl;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "ADDRESS")
    private String address;
}
