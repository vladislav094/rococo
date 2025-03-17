package guru.qa.rococo.controller;

import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.service.api.GrpcMuseumClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/museum")
public class MuseumController {

    private final GrpcMuseumClient grpcMuseumClient;

    @Autowired
    public MuseumController(GrpcMuseumClient grpcMuseumClient) {
        this.grpcMuseumClient = grpcMuseumClient;
    }

    @GetMapping
    public Page<MuseumJson> getAllMuseums(@RequestParam(name = "title", required = false) String title,
                                          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                          @RequestParam(name = "size", required = false, defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return grpcMuseumClient.getAllMuseums(title, pageable);
    }

    @GetMapping("/{id}")
    public MuseumJson getMuseumById(@PathVariable("id") String id) {
        return grpcMuseumClient.getMuseumById(id);
    }

    @PostMapping
    public MuseumJson createMuseum(@RequestBody MuseumJson museum) {
        return grpcMuseumClient.createMuseum(museum);
    }

    @PatchMapping
    public MuseumJson updateMuseum(@RequestBody MuseumJson museum) {
        return grpcMuseumClient.updateMuseum(museum);
    }
}
