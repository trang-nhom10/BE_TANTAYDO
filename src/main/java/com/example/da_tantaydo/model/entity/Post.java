package com.example.da_tantaydo.model.entity;

import com.example.da_tantaydo.model.enums.PostType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "POSTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post  extends BaseCreatedEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", columnDefinition = "LONGTEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PostType status;

    @ManyToOne
    @JoinColumn(name ="CATEGORY_ID")
    private  Category category;

    @Column(name ="PUBLISHED_AT")
    private LocalDateTime publishedAt;

    @Column(name ="IMG")
    private String img;


}