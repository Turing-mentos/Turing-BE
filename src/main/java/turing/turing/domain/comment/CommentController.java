package turing.turing.domain.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import turing.turing.domain.comment.dto.request.CommentReqDto;
import turing.turing.domain.comment.dto.response.CommentCreateResDto;

import java.net.URI;

@Tag(name = "Comment", description = "댓글")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성 (설명 참고)", description = "comment(application/json)와 file(multipart/form-data)를 전달 받습니다.")
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

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId){

        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }

}
