package guru.qa.rococo.controller;

import guru.qa.rococo.model.PaintingJson;
import guru.qa.rococo.service.api.GrpcPaintingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/painting")
public class PaintingController {

    private final GrpcPaintingClient grpcPaintingClient;

    @Autowired
    public PaintingController(GrpcPaintingClient grpcPaintingClient) {
        this.grpcPaintingClient = grpcPaintingClient;
    }

    @GetMapping
    public Page<PaintingJson> getPaintings(@RequestParam(name = "title", required = false) String title,
                                           @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                           @RequestParam(name = "size", required = false, defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return grpcPaintingClient.getPaintingsPage(pageable, title);
    }

    @GetMapping("/{id}")
    public PaintingJson getPaintingById(@PathVariable("id") String id) {
        return grpcPaintingClient.getPaintingById(id);
    }

    @GetMapping("/author/{id}")
    public PaintingJson getPaintingByAuthorId(@PathVariable("id") String id) {
        return grpcPaintingClient.getPaintingById(id);
    }

    @PostMapping
    public PaintingJson createPainting(@RequestBody PaintingJson painting) {
        return grpcPaintingClient.createPainting(painting);
    }
}
