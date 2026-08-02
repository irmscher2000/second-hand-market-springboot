package market.secondhandmarket.upload;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import java.io.File;
import java.util.Comparator;
import jakarta.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(MultipartFile file, long id) {
        String filename = id + "_" + file.getOriginalFilename();
        Path destination = this.rootLocation.resolve(filename);
        try {
            file.transferTo(destination);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Failed to list files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        Path file = load(filename);
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    @SuppressWarnings("null")
    @Override
    public void deleteAll() {
        try {
            Files.walk(rootLocation)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete all files", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        // Store the file using a generated unique filename.
        // We use a UUID to avoid collisions and keep the original filename
        // for reference. The method returns the stored filename.
        String originalFilename = file.getOriginalFilename();
        String uuid = java.util.UUID.randomUUID().toString();
        String filename = uuid + "_" + originalFilename;
        Path destination = this.rootLocation.resolve(filename);
        try {
            file.transferTo(destination);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public void delete(String filename) {
        String justFilename = StringUtils.getFilename(filename);
        try{
            Path file = load(justFilename);
            Files.deleteIfExists(file);
        } catch (IOException e){
            throw new StorageException("Error al eliminar un fichero", e);
        }
    }
    
}
