package guru.qa.rococo.controller;

import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.service.api.GrpcArtistClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    private final GrpcArtistClient grpcArtistClient;

    @Autowired
    public ArtistController(GrpcArtistClient grpcArtistClient) {
        this.grpcArtistClient = grpcArtistClient;
    }

    @GetMapping
    public Page<ArtistJson> getArtist(@RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                      @RequestParam(name = "size", required = false, defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return grpcArtistClient.getArtist(pageable, name);
    }

    @GetMapping("/{id}")
    public ArtistJson getArtisById(@PathVariable("id") String id) {
        return grpcArtistClient.getArtistById(id);
    }

    @PostMapping
    public ArtistJson createArtist(@RequestBody ArtistJson artist) {
        return grpcArtistClient.createArtist(artist);
    }

    @PatchMapping
    public ArtistJson updateArtist(@RequestBody ArtistJson artist) {
        return grpcArtistClient.updateArtist(artist);
    }
}
