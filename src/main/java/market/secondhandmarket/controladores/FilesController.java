package market.secondhandmarket.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.core.io.Resource;
import market.secondhandmarket.upload.StorageService;


@Controller
public class FilesController {
    
    private final StorageService storageService;

    public FilesController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = (Resource) storageService.loadAsResource(filename);
        return ResponseEntity.ok().body(file);
    }
    

}
