package com.baldprogrammer.crm.service;


import com.baldprogrammer.crm.base.BaseService;
import com.baldprogrammer.crm.dao.UserMapper;
import com.baldprogrammer.crm.model.UserModel;
import com.baldprogrammer.crm.utils.AssertUtil;
import com.baldprogrammer.crm.utils.Md5Util;
import com.baldprogrammer.crm.utils.UserIDBase64;
import com.baldprogrammer.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService extends BaseService<User, Integer> {

    @Autowired
    private UserMapper userMapper;


    /**
     * 用户登陆
     *
     * @param userName
     * @param userPwd
     */
    public UserModel userLogin(String userName, String userPwd) {
        // 参数判断，判断用户名  用户密码非空
        checkLoginParams(userName, userPwd);

        //调用dao层 通过用户名查询用户记录  返回用户对象
        User user = userMapper.queryUserByName(userName);

        //判断用户对象是否为空
        AssertUtil.isTrue(user == null, "用户名不存在");

        //判断密码是否正确  比较客户端传递的用户密码与数据库中的查询的用户对象的用户密码
        checkUserPwd(userPwd, user.getUserPwd());

        //返回构建的用户对象
        return buildUserInfo(user);
    }

    /**
     * 修改密码
     *
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(Integer userId, String oldPwd, String newPwd, String repeatPwd) {
        //通过用户ID查询用户记录，返回用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        //判断用户记录是否存在
        AssertUtil.isTrue(null == user, "待更新记录不存在！");
        //参数校验
        checkPasswordParams(user, oldPwd, newPwd, repeatPwd);
        //设置用户的新密码
        user.setUserPwd(Md5Util.encode(newPwd));
        //执行更新，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "修改密码失败");
    }

    /**
     * 修改密码的参数校验
     *
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        //判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd), "原始密码不能为空！");
        //判断原始密码是否正确 (查询的用户对象中的用户密码是否一致)
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不正确！");
        //判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd), "新密码不能为空！");
        //判断新密码是否与原始密码一致
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原始密码相同！");
        //判断确认密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"确认密码不能为空！");
        //判断确认密码是否与新密码一致
        AssertUtil.isTrue(!newPwd.equals(repeatPwd), "确认密码与新密码不一致！");
    }


    /**
     * 构建返回客户端的用户对象
     *
     * @param user
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        //userModel.setUserId(user.getId());
        //机密用户Id
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 密码判断
     *
     * @param userPwd
     * @param userPwd
     */
    private void checkUserPwd(String userPwd, String pwd) {
        //将客户端通过注定方式加密
        userPwd = Md5Util.encode(userPwd);
        //判断密码是否相等
        AssertUtil.isTrue(!userPwd.equals(pwd), "用户密码不正确");
    }

    /**
     * 用户判断
     *
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        //验证用户名
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空");
        //验证用户密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "用户密码不能为空");
    }

}
