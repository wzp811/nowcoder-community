package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping(path = "/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403,"你还没有登录!");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setContent(content);
        discussPost.setTitle(title);
        discussPost.setCreateTime(new Date());
        discussPost.setUserId(user.getId());
        discussPostService.addDiscussPost(discussPost);

        // 报错情况将来统一处理
        return CommunityUtil.getJSONString(0,"发布成功!");
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        // 帖子和作者
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("post",discussPost);
        model.addAttribute("user",user);

        // 评论的分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/"+discussPostId);
        page.setRows(discussPost.getCommentCount());

        // 评论： 帖子的评论
        // 回复： 评论的评论
        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST,discussPost.getId(),page.getOffset(),page.getLimit());

        // 评论View Object列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();

        for(Comment comment: commentList){
            // 评论VO
            Map<String,Object> commentVo = new HashMap<>();
            // 评论
            commentVo.put("comment",comment);
            // 评论的作者
            commentVo.put("user",userService.findUserById(comment.getUserId()));

            // 回复列表
            List<Comment> replyList = commentService.findCommentsByEntity(
                    ENTITY_TYPE_COMMENT,comment.getId(),0,Integer.MAX_VALUE);

            // 回复VO列表
            List<Map<String,Object>> replyVoList = new ArrayList<>();

            for(Comment reply: replyList){
                Map<String,Object> replyVo = new HashMap<>();
                // 回复
                replyVo.put("reply",reply);
                // 作者
                replyVo.put("user",userService.findUserById(reply.getUserId()));
                // 回复目标
                User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                replyVo.put("target",target);

                replyVoList.add(replyVo);
            }

            commentVo.put("replys",replyVoList);

            // 回复数量
            int replyCount = commentService.findCommentCountByEntity(ENTITY_TYPE_COMMENT,comment.getId());
            commentVo.put("replyCount",replyCount);

            commentVoList.add(commentVo);
        }

        model.addAttribute("comments",commentVoList);

        return "/site/discuss-detail";
    }



}
