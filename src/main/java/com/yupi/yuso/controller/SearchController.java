package com.yupi.yuso.controller;

import com.yupi.yuso.common.BaseResponse;
import com.yupi.yuso.common.ResultUtils;
import com.yupi.yuso.manager.SearchFacade;
import com.yupi.yuso.model.dto.post.PostEsDTO;
import com.yupi.yuso.model.dto.search.SearchRequest;
import com.yupi.yuso.model.vo.SearchVO;
import com.yupi.yuso.service.PictureService;
import com.yupi.yuso.service.PostService;
import com.yupi.yuso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 图片接口
 *
 */
@RestController
@RequestMapping("/search")
@CrossOrigin
@Slf4j
public class SearchController {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private PictureService pictureService;

    @Resource
    private SearchFacade searchFacade;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        return ResultUtils.success(searchFacade.searchAll(searchRequest, request));
    }

    @GetMapping("/getSearchPrompt")
    public BaseResponse getSearchPrompt(String keyword){

        List<String> searchPrompt = postService.getSearchPrompt(keyword);

        return ResultUtils.success(searchPrompt);
    }

}
