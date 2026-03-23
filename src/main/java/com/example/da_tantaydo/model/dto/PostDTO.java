package com.example.da_tantaydo.model.dto;

import com.example.da_tantaydo.model.entity.BaseCreatedEntity;
import com.example.da_tantaydo.model.entity.Category;
import com.example.da_tantaydo.model.entity.Post;
import com.example.da_tantaydo.model.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
@Setter
@Getter
@AllArgsConstructor
public class PostDTO  {

    private String title;
    private String content;
    private PostType status;
    private Category category;
    private LocalDateTime publishedAt;
    private String img;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    public PostDTO(Post post){
        this.title =post.getTitle();
        this.content = post.getContent();
        this.status =post.getStatus();
        this.category=post.getCategory();
        this.publishedAt=post.getPublishedAt();
        this.img=post.getImg();
        this.createAt= post.getCreatedAt();
        this.updateAt=post.getUpdateAt();
    }

}
