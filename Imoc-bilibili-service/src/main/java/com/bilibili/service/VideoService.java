package com.bilibili.service;

import com.bilibili.dao.VideoMapper;
import com.bilibili.domain.PageResult;
import com.bilibili.domain.Video;
import com.bilibili.domain.VideoLike;
import com.bilibili.domain.VideoTag;
import com.bilibili.domain.exception.ConditionException;
import com.bilibili.service.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private FastDFSUtil fastDFSUtil;

    @Autowired
    private UserService userService;


    @Autowired
    private FileService fileService;

    private static final int FRAME_NO = 256;

    @Transactional
    public void addVideos(Video video) {
        Date now = new Date();
        video.setCreateTime(new Date());
        videoMapper.addVideos(video);
        Long videoId = video.getId();
        List<VideoTag> tagList = video.getVideoTagList();
        tagList.forEach(item -> {
            item.setCreateTime(now);
            item.setVideoId(videoId);
        });
        videoMapper.batchAddVideoTags(tagList);
    }

    public PageResult<Video> pageListVideos(Integer size, Integer no, String area) {
        if (size == null || no == null) {
            throw new ConditionException("参数异常！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("start", (no - 1) * size);
        params.put("limit", size);
        params.put("area", area);
        List<Video> list = new ArrayList<>();
        Integer total = videoMapper.pageCountVideos(params);
        if (total > 0) {
            list = videoMapper.pageListVideos(params);
        }
        return new PageResult<>(total, list);
    }

    public void viewVideoOnlineBySlices(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String url) {
        try {
            fastDFSUtil.viewVideoOnlineBySlices(request, response, url);
        } catch (Exception ignored) {
        }
    }

    public void addVideoLike(Long videoId, Long userId) {
        Video video = videoMapper.getVideoById(videoId);
        if (video == null) {
            throw new ConditionException("非法视频！");
        }
        VideoLike videoLike = videoMapper.getVideoLikeByVideoIdAndUserId(videoId, userId);
        if (videoLike != null) {
            throw new ConditionException("已经赞过！");
        }
        videoLike = new VideoLike();
        videoLike.setVideoId(videoId);
        videoLike.setUserId(userId);
        videoLike.setCreateTime(new Date());
        videoMapper.addVideoLike(videoLike);
    }

    public void deleteVideoLike(Long videoId, Long userId) {
        videoMapper.deleteVideoLike(videoId, userId);
    }

    public Map<String, Object> getVideoLikes(Long videoId, Long userId) {
        Long count = videoMapper.getVideoLikes(videoId);
        VideoLike videoLike = videoMapper.getVideoLikeByVideoIdAndUserId(videoId, userId);
        boolean like = videoLike != null;
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("like", like);
        return result;
    }

}
