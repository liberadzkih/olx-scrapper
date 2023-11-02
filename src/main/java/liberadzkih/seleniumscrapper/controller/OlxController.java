package liberadzkih.seleniumscrapper.controller;

import liberadzkih.seleniumscrapper.model.OlxItem;
import liberadzkih.seleniumscrapper.repository.OlxItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
class OlxController {

    private final OlxItemRepository olxItemRepository;

    @GetMapping("/olx/{id}")
    ResponseEntity<OlxItem> getItem(@PathVariable String id) {
        return ResponseEntity.ok(olxItemRepository.findById(id).get());

    }
}
