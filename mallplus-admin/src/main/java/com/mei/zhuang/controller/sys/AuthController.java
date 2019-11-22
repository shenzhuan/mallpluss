package com.mei.zhuang.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.constant.CommonConstant;
import com.mei.zhuang.entity.sys.FrontUser;
import com.mei.zhuang.jwt.JWTInfo;
import com.mei.zhuang.jwt.JwtAuthenticationRequest;
import com.mei.zhuang.service.sys.AuthService;
import com.mei.zhuang.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(value = "权限验证", description = "权限验证", tags = {"权限验证"})
@RestController
@RequestMapping("jwt")
public class AuthController {

    @Value("${auth.user.token-header}")
    private String tokenHeader;

    private final AuthService authService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "创建用户token")
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public JSONObject createAuthenticationToken(JwtAuthenticationRequest authenticationRequest) throws Exception {
        return authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    }

    @ApiOperation(value = "没有验证直接生成token")
    @RequestMapping(value = "/genToken", method = RequestMethod.POST)
    public String genToken(@RequestBody  JwtAuthenticationRequest authenticationRequest) throws Exception {

        return  jwtTokenUtil.generateToken(new JWTInfo(authenticationRequest.getUsername(), authenticationRequest.getPassword().toString(), authenticationRequest.getUsername()));
    }

    @ApiOperation("核实token有效性")
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public ResponseEntity<?> verify(String token) throws Exception {
        authService.validate(token);
        return ResponseEntity.ok(true);
    }

    @ApiOperation("检查token无效")
    @RequestMapping(value = "/invalid", method = RequestMethod.POST)
    public ResponseEntity<?> invalid(@ApiParam("用户token") @RequestHeader("access-token") String token){
        authService.invalid(token);
        return ResponseEntity.ok(true);
    }

    @ApiOperation("获取平台用户信息")
    @RequestMapping(value = "/platformUser", method = RequestMethod.POST)
    public ResponseEntity<?> getPlatformUser(@ApiParam("用户token") String token) throws Exception {
        FrontUser userInfo = authService.getPlatformUserInfo(token);
        if(userInfo==null) {
            return ResponseEntity.status(401).body(false);
        }else {
            return ResponseEntity.ok(userInfo);
        }
    }

    @ApiOperation(value = "获取具体的租户的用户信息")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<?> getUserInfo(HttpServletRequest request, @ApiParam("用户token") String token) throws Exception {
        String tenantId = request.getHeader(CommonConstant.CURR_TENANT_ID);
        FrontUser userInfo = authService.getUserInfo(token,tenantId);
        if(userInfo==null)
            return ResponseEntity.status(401).body(false);
        else
            return ResponseEntity.ok(userInfo);
    }




}
