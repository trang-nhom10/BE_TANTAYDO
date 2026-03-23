package com.example.da_tantaydo.controller;
import com.example.da_tantaydo.model.dto.request.PostRequestDTO;
import com.example.da_tantaydo.model.dto.response.ResponseDTO;
import com.example.da_tantaydo.model.enums.PostType;
import com.example.da_tantaydo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/getall")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(Map.of(
                "message", "Get all success",
                "data", postService.getAll()
        ));
    }

    @PostMapping( value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasAuthority('ADMIN_MANAGE_WEB')")
    public ResponseEntity<?> create(@RequestPart PostRequestDTO request,
                                    @RequestPart(required = false)  MultipartFile img) {
        postService.create(request,img);
        return  ResponseEntity.ok("create success");
    }

    @PostMapping( value = "/update/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasAuthority('ADMIN_MANAGE_WEB')")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestPart PostRequestDTO request,
            @RequestPart(required = false)  MultipartFile img) {
        postService.update(id,request,img);
         return  ResponseEntity.ok(" update success");
    }

    @PostMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('ADMIN_MANAGE_WEB')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok("delete success");
    }
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) PostType status) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("oke")
                .code(200)
                .message("search success")
                .data(postService.search(title,status))
                .build());
    }
}