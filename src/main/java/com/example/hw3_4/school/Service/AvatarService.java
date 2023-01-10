package com.example.hw3_4.school.Service;

import com.example.hw3_4.school.Model.Avatar;
import com.example.hw3_4.school.Model.Student;
import com.example.hw3_4.school.repositories.AvatarRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

   private final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${student.avatar.dir.path}")
    private String avatarsDir;
    private final AvatarRepo avatarRepo;
    private final StudentService studentService;

    public AvatarService(AvatarRepo avatarRepo, StudentService studentService) {
        this.avatarRepo = avatarRepo;
        this.studentService = studentService;
    }
    /* Загрузка изображения */
    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.debug("Was invoked method for uploadAvatar");
        Student student = studentService.findStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateImagePreview(filePath));
        avatarRepo.save(avatar);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Avatar findAvatar(Long studentId) {
        logger.debug("Was invoked method for findAvatar");
        return avatarRepo.findByStudentId(studentId).orElse(new Avatar());
    }
/* Поулчить превью картинки */
    public byte[] generateImagePreview(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();
            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();

        }
    }

    public List<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize){
        logger.debug("Was invoked method for  getAllAvatars");
        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
        return avatarRepo.findAll(pageRequest).getContent();
    }

}
