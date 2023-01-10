package com.example.hw3_4.school.Controllr;

import com.example.hw3_4.school.Model.Avatar;
import com.example.hw3_4.school.Service.AvatarService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() >= 1024 * 300) {
            return  ResponseEntity.badRequest().body("File is too big");
        }
        avatarService.uploadAvatar(studentId,avatar);
        return  ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar-from-db")
    public  ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id){
        Avatar avatar = avatarService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar-from-file")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException{
        Avatar avatar = avatarService.findAvatar(id);

        Path path= Path.of(avatar.getFilePath());
        try(InputStream is = Files.newInputStream(path);
            OutputStream os = response.getOutputStream()) {
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int)avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping("avatars/byPage")
    public List<Avatar> getAllAvatars(@RequestParam("page") Integer pageNumber, @RequestParam("size") Integer pageSize){
        return avatarService.getAllAvatars(pageNumber,pageSize);
    }
    @GetMapping(params = {"studentId"})
    public Avatar findAvatar(@RequestParam Long studentId) {
        return avatarService.findAvatar(studentId);
    }

}
