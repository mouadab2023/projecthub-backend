package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.CommentDto;
import org.example.projecthubbackend.entities.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public CommentDto toDTO(Comment comment){
        return CommentDto.builder()
                .comment(comment.getComment())
                .commentedAt(comment.getCommentedAt())
                .author(comment.getAuthor()!=null?comment.getAuthor().getId():null)
                .task(comment.getTask()!=null?comment.getTask().getId():null)
                .build();
    }

    public Comment toEntityBasics(CommentDto commentDto){
        return Comment.builder()
                .comment(commentDto.getComment())
                .commentedAt(commentDto.getCommentedAt())
                .build();
    }
}
