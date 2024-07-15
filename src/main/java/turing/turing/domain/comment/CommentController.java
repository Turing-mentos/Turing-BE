package turing.turing.domain.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import turing.turing.domain.comment.dto.request.CommentReqDto;
import turing.turing.domain.comment.dto.response.CommentCreateResDto;

import java.net.URI;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/questions/{questionId}/comments")
    public ResponseEntity<Long> createComment(
            @PathVariable Long questionId,
            @RequestPart(value = "comment") CommentReqDto commentReqDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        // 추후 Role 가져와 선생인지 학생인지 전달 필요 !
        CommentCreateResDto commentCreateResDto = commentService.createComment("TEACHER", questionId, commentReqDto, file);

        Long commentId = commentCreateResDto.commentId();
        URI location = URI.create("/api/comments/" + commentId);

        return ResponseEntity.created(location).body(commentId);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId){

        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }

}
