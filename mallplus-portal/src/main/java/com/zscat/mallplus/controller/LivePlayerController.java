package com.zscat.mallplus.controller;


import com.zscat.mallplus.sms.service.LivePlayerService;
import com.zscat.mallplus.vo.sms.LivePlayerRequest;
import com.zscat.mallplus.vo.sms.RoomInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序直播列表控制器
 *
 * @author Caizize mojin on 2020/4/27
 */
@RestController
@RequestMapping("/api/single/sms")
public class LivePlayerController {

    @Autowired
    private LivePlayerService livePlayerService;


    /**
     * 获取小程序直播房间列表
     *
     * @param livePlayerRequest 小程序直播房间列表请求实体
     * @return 小程序直播房间列表
     */

    @RequestMapping("/queryliveplayerroomlist")
    @ResponseBody
    @ApiOperation(value = "获取小程序直播房间列表", notes = "获取小程序直播房间列表（不需要认证）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "小程序直播房间列表", response = RoomInfo.class)
    })
    public Object queryLivePlayerRoomList(LivePlayerRequest livePlayerRequest) {
        return livePlayerService.queryLivePlayerList(livePlayerRequest);
    }

}
