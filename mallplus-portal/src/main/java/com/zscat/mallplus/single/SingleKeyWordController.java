package com.zscat.mallplus.single;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.sys.entity.MallplusKeyword;
import com.zscat.mallplus.sys.entity.MallplusSearchHistory;
import com.zscat.mallplus.sys.mapper.MallplusKeywordMapper;
import com.zscat.mallplus.sys.mapper.MallplusSearchHistoryMapper;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索服务
 * <p>
 * 注意：目前搜索功能非常简单，只是基于关键字匹配。
 */
@RestController
@RequestMapping("/api/single/search")
@Validated
public class SingleKeyWordController {

    @Autowired
    private MallplusKeywordMapper keywordsService;
    @Autowired
    private MallplusSearchHistoryMapper searchHistoryService;
    @Autowired
    private IUmsMemberService memberService;

    /**
     * 搜索页面信息
     * <p>
     * 如果用户已登录，则给出用户历史搜索记录；
     * 如果没有登录，则给出空历史搜索记录。
     *
     * @return 搜索页面信息
     */
    @GetMapping("index")
    public Object index() {
        //取出输入框默认的关键词
        MallplusKeyword defaultKeyword = keywordsService.selectOne(new QueryWrapper<MallplusKeyword>().eq("is_default", true).eq("deleted", false));
        //取出热闹关键词
        List<MallplusKeyword> hotKeywordList = keywordsService.selectList(new QueryWrapper<MallplusKeyword>().eq("is_hot", true).eq("deleted", false));

        List<MallplusSearchHistory> historyList = null;
        UmsMember member = memberService.getNewCurrentMember();
        if (member != null && member.getId() > 0) {
            //取出用户历史关键字
            historyList = searchHistoryService.selectList(new QueryWrapper<MallplusSearchHistory>().eq("user_id", member.getId()));
        } else {
            historyList = new ArrayList<>(0);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("defaultKeyword", defaultKeyword);
        data.put("historyKeywordList", historyList);
        data.put("hotKeywordList", hotKeywordList);
        return new CommonResult().success(data);
    }

    /**
     * 关键字提醒
     * <p>
     * 当用户输入关键字一部分时，可以推荐系统中合适的关键字。
     *
     * @param keyword 关键字
     * @return 合适的关键字
     */
    @GetMapping("helper")
    public Object helper(@NotEmpty String keyword,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "10") Integer limit) {
        List<MallplusKeyword> keywordsList = keywordsService.selectList(new QueryWrapper<>(new MallplusKeyword()).like("keyword", keyword));

        String[] keys = new String[keywordsList.size()];
        int index = 0;
        for (MallplusKeyword key : keywordsList) {
            keys[index++] = key.getKeyword();
        }
        return new CommonResult().success(keys);
    }

    /**
     * 清除用户搜索历史
     *
     * @return 清理是否成功
     */
    @PostMapping("clearhistory")
    public Object clearhistory() {
        UmsMember member = memberService.getNewCurrentMember();
        if (member != null && member.getId() > 0) {
            searchHistoryService.delete(new QueryWrapper<MallplusSearchHistory>().eq("user_id", member.getId()));
            return new CommonResult().success();
        }
        return new CommonResult().fail(100);

    }
}
