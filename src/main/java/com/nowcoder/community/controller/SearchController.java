package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.SearchResult;
import com.nowcoder.community.service.ElasticsearchService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nowcoder.community.util.CommunityConstant.ENTITY_TYPE_POST;

@Controller
public class SearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    // search?keyword=xxx
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model) {
        // 搜索帖子
        try {
            SearchResult searchResult = elasticsearchService.searchDiscussPost(keyword, (page.getCurrent() - 1) * 10, page.getLimit());
            List<Map<String, Object>> discussPosts = new ArrayList<>();
            List<DiscussPost> list = searchResult.getList();
            if (list != null) {
                for (DiscussPost post : list) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("post", post);
                    map.put("user", userService.findUserById(post.getUserId()));
                    map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));

                    discussPosts.add(map);
                }
            }
            model.addAttribute("discussPosts", discussPosts);
            model.addAttribute("keyword", keyword);
            page.setPath("/search?keyword=" + keyword);
            page.setRows(searchResult.getTotal() == 0 ? 0 : (int) searchResult.getTotal());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "/site/search";
    }

}
