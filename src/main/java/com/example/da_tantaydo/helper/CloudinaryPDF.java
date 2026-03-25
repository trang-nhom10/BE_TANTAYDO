package com.example.da_tantaydo.helper;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CloudinaryPDF {

    private final Cloudinary cloudinary;

    public String uploadPdf(byte[] fileBytes, String fileName) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String publicId = "PDF/" + timestamp + "_" + fileName.replace(".pdf", "");

            Map<?, ?> result = cloudinary.uploader().upload(fileBytes, Map.of(
                    "resource_type", "raw",
                    "folder", "PDF",
                    "public_id", publicId,
                    "format", "pdf"
            ));

            String url = (String) Optional.ofNullable(result.get("secure_url"))
                    .orElseThrow(() -> new RuntimeException("Cloudinary không trả về secure_url"));

            return url.endsWith(".pdf") ? url : url + ".pdf";

        } catch (IOException e) {
            throw new RuntimeException("Upload PDF to Cloudinary failed: " + e.getMessage(), e);
        }
    }
}