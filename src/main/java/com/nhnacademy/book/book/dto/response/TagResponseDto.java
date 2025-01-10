package com.nhnacademy.book.book.dto.response;

import lombok.*;

@Data
@Getter
@RequiredArgsConstructor
@Setter
@AllArgsConstructor
public class TagResponseDto {

    private Long tagId;
    private String tagName;
}
