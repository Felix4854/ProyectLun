package pe.isil.luna_2618.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import pe.isil.luna_2618.exception.FileNotFoundException;
import pe.isil.luna_2618.exception.StorageException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService {

    //mapear donde vamos guardar los archivos
    @Value("${storage.location}")
    private String storageLocation;

    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(Paths.get(storageLocation));//creando el directorio en la ruta especifica
        }catch (IOException e){
            throw new StorageException("No se puede inicializar el Storage Location", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String filename = file.getOriginalFilename();

        if (file.isEmpty()){
            throw new StorageException("Falló al guardar, el archivo esta vacio: " + filename);
        }

        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, Paths.get(storageLocation).resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new StorageException("Falló en guardar el archivo: " + filename, e);
        }

        return filename;
    }

    @Override
    public Path load(String filename) {
        return Paths.get(storageLocation).resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try{
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()){
                return resource;
            }else{
                throw new FileNotFoundException("No se encuentra o no se puede leer el archivo: " + filename);
            }
        }catch (MalformedURLException e){
            throw new FileNotFoundException("No se encuentra o no se puede leer el archivo: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path file = load(filename);
            FileSystemUtils.deleteRecursively(file);
        }catch (IOException e){
            throw new StorageException("No se puede eliminar el archivo: " + filename, e);
        }
    }
}
