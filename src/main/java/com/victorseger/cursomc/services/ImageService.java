package com.victorseger.cursomc.services;

import com.victorseger.cursomc.services.exceptions.FileException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {

    public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
        String extension = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());

        if(!"png".equals(extension) && !"jpg".equals(extension)) {
            throw new FileException("Somente imagens PNG e JPG são permitidas");
        }

        try {
            BufferedImage image = ImageIO.read(uploadedFile.getInputStream());

            if ("png".equals(extension)) {
                image = pngToJpg(image);
            }
            return image;
        } catch (IOException e) {
            throw new FileException("Erro ao ler arquivo");
        }

    }

    //conversor de png para jpg
    private BufferedImage pngToJpg(BufferedImage imagePng) {
        BufferedImage jpg = new BufferedImage(imagePng.getWidth(), imagePng.getHeight(), BufferedImage.TYPE_INT_RGB);
        jpg.createGraphics().drawImage(imagePng,0,0, Color.WHITE, null);
        return jpg;
    }

    //método que faz upload para o S3 da AWS precisa de um inputstream, por isso precisamos obter o inputstream a partir de um bufferedimage
    public InputStream getInputStream(BufferedImage image, String extension) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image,extension,outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new FileException("Erro ao ler arquivo");
        }
    }


}
