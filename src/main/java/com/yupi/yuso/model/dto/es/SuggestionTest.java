package com.yupi.yuso.model.dto.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * @author WZ
 * @date 2023/4/10 22:07
 */
@Document(indexName = "suggestion_test")
@Data
public class SuggestionTest {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 标题
     */
    private String doc_name;

    /**
     * 内容
     */
    private String doc_number;

    /**
     * 标签列表
     */
    private String doc_type;

    /**
     * 创建用户 id
     */
    private String keywords;

    /**
     * 创建时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date pubdate;

    private static final long serialVersionUID = 1L;
}
