package market.secondhandmarket.upload;

import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import java.nio.file.Path;

public interface StorageService {
    
    void init();
    String store(MultipartFile file);
    Stream <Path> loadAll();
    Path load(String filename);
    Resource loadAsResource(String filename);
    void delete(String filename);
    void deleteAll();
    String store(MultipartFile file, long id);
}
