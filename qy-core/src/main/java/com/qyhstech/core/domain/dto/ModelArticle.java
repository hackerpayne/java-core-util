package com.qyhstech.core.domain.dto;

import com.qyhstech.core.domain.base.QyBaseEntity;
import lombok.*;

/**
 * Created by kyle on 17/5/25.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ModelArticle extends QyBaseEntity {

    private Integer artid;

    private String title;

    private String body;

    private String author;

    private String url;

}
