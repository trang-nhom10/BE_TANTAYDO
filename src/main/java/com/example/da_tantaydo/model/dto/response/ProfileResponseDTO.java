package com.example.da_tantaydo.model.dto.response;

import com.example.da_tantaydo.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ProfileResponseDTO {
    private String gmail;
    private String role;
    private  String name;
    private String img;
    private List<String> permissions;

}