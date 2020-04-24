package com.zscat.mallplus.build.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.build.BuildHomeResult;
import com.zscat.mallplus.build.CommunityTree;
import com.zscat.mallplus.build.entity.*;
import com.zscat.mallplus.build.mapper.*;
import com.zscat.mallplus.build.service.IBuildingCommunityService;
import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.oms.vo.ActivityVo;
import com.zscat.mallplus.sys.entity.SysShop;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.entity.SysUserRole;
import com.zscat.mallplus.sys.mapper.SysUserMapper;
import com.zscat.mallplus.sys.mapper.SysUserRoleMapper;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 小区 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-11-27
 */
@Service
public class BuildingCommunityServiceImpl extends ServiceImpl<BuildingCommunityMapper, BuildingCommunity> implements IBuildingCommunityService {

    @Resource
    private BuildingCommunityMapper communityMapper;
    @Resource
    private BuildingFloorMapper buildingFloorMapper;
    @Resource
    private BuildingRoomMapper buildingRoomMapper;
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private SysUserRoleMapper userRoleMapper;
    @Resource
    private IUmsMemberService memberService;
    @Resource
    private BuildNoticeMapper noticeMapper;
    @Resource
    private BuildAdvMapper advMapper;

    @Override
    public boolean saveCommunity(BuildingCommunity entity) {
        //1 创建小区
        entity.setCreateTime(new Date());
        communityMapper.insert(entity);
        // 2 创建物业公司账号
        SysUser user = new SysUser();
        user.setUsername(entity.getName());
        SysUser umsAdminList = userMapper.selectByUserName(entity.getName());
        if (umsAdminList != null) {
            throw new ApiMallPlusException("此小区已存在");
        }
        user.setStatus(1);
        //  user.setStoreId(entity.getId());
        user.setPassword(passwordEncoder.encode("123456"));
        user.setCreateTime(new Date());
        user.setSupplyId(0L);
        user.setNote("小区账户：小区ID=" + entity.getName() + "," + entity.getId());
        user.setNickName(entity.getName());
        userMapper.insert(user);

        // 3 分配物业公司角色
        SysUserRole userRole = new SysUserRole();
        userRole.setAdminId(user.getId());
        userRole.setRoleId(5L);
        userRoleMapper.insert(userRole);
        return true;
    }

    @Override
    public Object allCommunity() {
        List<CommunityTree> resultList = new ArrayList<>();

        List<BuildingCommunity> list = communityMapper.selectList(new QueryWrapper<BuildingCommunity>().select(ConstansValue.sampleCommunityList));
        for (BuildingCommunity community : list) {
            CommunityTree communityTree = new CommunityTree();
            communityTree.setCode("C" + community.getId());
            communityTree.setName(community.getName());
            communityTree.setParentCode("0");
            resultList.add(communityTree);
        }

        List<BuildingFloor> floorList = buildingFloorMapper.selectList(new QueryWrapper<>());
        for (BuildingFloor community : floorList) {
            CommunityTree communityTree = new CommunityTree();
            communityTree.setCode("F" + community.getId());
            communityTree.setName(community.getName());
            communityTree.setParentCode("C" + community.getCommunityId());
            resultList.add(communityTree);
        }

        List<BuildingRoom> roomList = buildingRoomMapper.selectList(new QueryWrapper<>());
        for (BuildingRoom community : roomList) {
            CommunityTree communityTree = new CommunityTree();
            communityTree.setCode("R" + community.getId());
            communityTree.setName(community.getRoomNum());
            communityTree.setParentCode("F" + community.getFloorId());
            resultList.add(communityTree);
        }
        return resultList;
    }

    @Override
    @Transactional
    public Object bindCommunity(String selectedMore2) {
        UmsMember member = memberService.getNewCurrentMember();
        if (member == null) {
            return new CommonResult().fail(100);
        }
        if (ValidatorUtils.empty(selectedMore2)) {
            return new CommonResult().failed("请选择绑定数据");
        }
        String[] split = selectedMore2.split(",");
        if (split.length == 3) {
            String rommIds = selectedMore2.replaceAll("C", "").replaceAll("F", "").replaceAll("R", "");
            BuildingRoom room = buildingRoomMapper.selectById(split[2].replaceAll("R", ""));
            if (room == null) {
                return new CommonResult().failed("房间不存在");
            }
            member.setRoomNums(rommIds);
            member.setRoomDesc(room.getRoomDesc());
            memberService.updateById(member);
            return new CommonResult().success(member);
        } else {
            return new CommonResult().failed("请选择绑定房间");
        }

    }

    @Override
    public BuildHomeResult singleContent(Long communityId) {
        BuildHomeResult result = new BuildHomeResult();
        List<BuildAdv> buildAdvs = new ArrayList<>();
        List<BuildNotice> buildNotices = new ArrayList<>();
        BuildingCommunity community = new BuildingCommunity();
        if (ValidatorUtils.notEmpty(communityId) && communityId > 0) {
            community = communityMapper.selectById(communityId);
        } else {
            UmsMember member = memberService.getNewCurrentMember();
            if (member == null) {
                throw new ApiMallPlusException("100");
            }
            if (ValidatorUtils.notEmpty(member.getRoomNums())) {
                String[] split = member.getRoomNums().split(",");
                community = communityMapper.selectById(split[0]);
            }

        }
        buildAdvs = advMapper.selectList(new QueryWrapper<BuildAdv>().eq("community_id", community.getId()));
        buildNotices = noticeMapper.selectList(new QueryWrapper<BuildNotice>().eq("community_id", community.getId()));
        if (Double.valueOf(community.getLatitude())>0){
            List<BuildingCommunity> communityList = this.selectNearCommunity(40,Double.valueOf(community.getLatitude()),Double.valueOf(community.getLongitude()),10);
            result.setCommunityList(communityList);
        }
        result.setCommunity(community);
        result.setActivityList(getActivityList());
        result.setSubjectList(buildNotices);
        result.setAdvertiseList(buildAdvs);
        return result;
    }

    @Override
    public List<BuildingCommunity> selectNearCommunity(Integer distance, double latitude, double longitude, Integer pageSize) {
        return communityMapper.selectNearCommunity(distance,latitude,longitude,pageSize);
    }

    public List<ActivityVo> getActivityList() {
        List<ActivityVo> activityList = new ArrayList<>();
        activityList.add(new ActivityVo("优惠多多", "/pages/activity/goods_combination/index", "http://datong.crmeb.net/public/uploads/attach/2019/03/28/5c9ccf7e9f4d0.jpg", "一起来拼团", "/activity/group"));
        activityList.add(new ActivityVo("新能源汽车火热销售", "/pages/activity/goods_seckill/index", "http://datong.crmeb.net/public/uploads/attach/2019/03/28/5c9ccf7e97660.jpg", "秒杀专区", "/activity/goods_seckill"));

        activityList.add(new ActivityVo("呼朋唤友来砍价~~~", "/pages/activity/goods_bargain/index", "http://datong.crmeb.net/public/uploads/attach/2019/03/28/5c9ccfc86a6c1.jpg", "砍价活动", "/activity/bargain"));

        return activityList;
    }
}
