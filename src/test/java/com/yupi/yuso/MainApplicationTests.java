package com.yupi.yuso;


import com.yupi.yuso.config.WxOpenConfig;
import com.yupi.yuso.model.dto.post.PostEsDTO;
import lombok.Data;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.suggest.response.CompletionSuggestion;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;

import javax.annotation.Resource;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 主类测试
 *
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() {
        System.out.println(wxOpenConfig);
    }

    @Test
    public void test() {
        SuggestBuilder suggestBuilder = new SuggestBuilder()
                .addSuggestion("title_suggest", new CompletionSuggestionBuilder("title_suggest").prefix("mon"));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withSuggestBuilder(suggestBuilder).build();

        SearchHits<PostEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, PostEsDTO.class);
        return;
    }

    @Test
    public void suggestionTest(){
        @Data
        class Temp{
            Float score;
            String text;

            public Temp(Float score, String text) {
                this.score = score;
                this.text = text;
            }
        }
        SuggestBuilder suggestBuilder = new SuggestBuilder()
                .addSuggestion("suggestionTitle", new CompletionSuggestionBuilder("titleSuggestion").prefix("企业"));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withSuggestBuilder(suggestBuilder).build();

        TreeSet<Temp> temps = new TreeSet<>((s1, s2) -> Math.round(s2.score - s1.score));
        SearchHits<PostEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, PostEsDTO.class);
        List<Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>>> suggestions = searchHits.getSuggest().getSuggestions();
        // suggestions包含了我们add的所有suggestion，这里只有一个（suggestionTitle），因此自己清楚地情况下直接去索引为0的suggestion也可以
        for (int i = 0; i < suggestions.size(); i++) {
            CompletionSuggestion<CompletionSuggestion.Entry<CompletionSuggestion.Entry.Option>> suggestion = (CompletionSuggestion<CompletionSuggestion.Entry<CompletionSuggestion.Entry.Option>>) suggestions.get(i);
            // 一个entry对应一个suggestion中一个关键词（也就是一个prefix）的搜索结果，只有一个的情况下可以直接取
            List<CompletionSuggestion.Entry<CompletionSuggestion.Entry<CompletionSuggestion.Entry.Option>>> entries = suggestion.getEntries();
            for (CompletionSuggestion.Entry<CompletionSuggestion.Entry<CompletionSuggestion.Entry.Option>> entry : entries) {
                // options保存的是最终的结果
                List<CompletionSuggestion.Entry.Option<CompletionSuggestion.Entry<CompletionSuggestion.Entry.Option>>> options = entry.getOptions();
                for (CompletionSuggestion.Entry.Option<CompletionSuggestion.Entry<CompletionSuggestion.Entry.Option>> option : options) {
                    float score = option.getScore();
                    String text = option.getText();
                    Temp temp = new Temp(score, text);

                    temps.add(temp);

                }
            }
        }
        System.out.println(temps);
        List<String> suggestionText = temps.stream().map(Temp::getText).collect(Collectors.toList());

        suggestions.get(0).getEntries().get(0).getOptions().get(0);
    }
}
