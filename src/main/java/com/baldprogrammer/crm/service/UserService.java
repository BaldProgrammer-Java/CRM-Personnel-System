package com.baldprogrammer.crm.service;


import com.baldprogrammer.crm.base.BaseService;
import com.baldprogrammer.crm.dao.RoleMapper;
import com.baldprogrammer.crm.dao.UserMapper;
import com.baldprogrammer.crm.dao.UserRoleMapper;
import com.baldprogrammer.crm.model.UserModel;
import com.baldprogrammer.crm.utils.AssertUtil;
import com.baldprogrammer.crm.utils.Md5Util;
import com.baldprogrammer.crm.utils.PhoneUtil;
import com.baldprogrammer.crm.utils.UserIDBase64;
import com.baldprogrammer.crm.vo.User;
import com.baldprogrammer.crm.vo.UserRole;
import freemarker.template.TemplateModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;


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
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)), "原始密码不正确！");
        //判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd), "新密码不能为空！");
        //判断新密码是否与原始密码一致
        AssertUtil.isTrue(oldPwd.equals(newPwd), "新密码不能与原始密码相同！");
        //判断确认密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd), "确认密码不能为空！");
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


    /**
     * 添加用户
     *
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) {
        //参数校验
        checkUserParams(user.getUserName(), user.getEmail(), user.getPhone(), null);
        //设置默认参数
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //设置默认password
        user.setUserPwd(Md5Util.encode("123456"));

        //执行添加操作，判断受影响行数
        AssertUtil.isTrue(userMapper.insertSelective(user) < 1, "用户添加失败！");

        //用户角色关联
        relationUserRole(user.getId(), user.getRoleIds());

    }

    /**
     * 用户角色关联
     *
     * @param userId
     * @param roleIds
     */
    private void relationUserRole(Integer userId, String roleIds) {
        Integer count = userRoleMapper.countUserRoleByUserId(userId);

        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "用户角色分配失败！");
        }
        if (StringUtils.isNotBlank(roleIds)) {
            List<UserRole> userRoles = new ArrayList<UserRole>();

            for (String s : roleIds.split(",")) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoles.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles) < userRoles.size(), "用户角色分配失败！");
        }

    }


    /**
     * 更新用户
     *
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        //判断用户ID非空 且数据存在
        AssertUtil.isTrue(null == user.getId(), "待更新记录不存在！");
        //通过id查询数据
        User temp = userMapper.selectByPrimaryKey(user.getId());
        //判断是否存在
        AssertUtil.isTrue(null == temp, "待更新记录不存在！");
        //参数校验
        checkUserParams(user.getUserName(), user.getEmail(), user.getPhone(),user.getId());
        //设置默认值
        user.setUpdateDate(new Date());
        //执行更新
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1, "用户更新失败！");

        relationUserRole(user.getId(), user.getRoleIds());
    }


    /**
     * 添加用户参数校验
     *
     * @param userName
     * @param email
     * @param phone
     */
    private void checkUserParams(String userName, String email, String phone, Integer userId) {
        //判断用户名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空！");
        //判断用户的唯一性
        //通过用户名查询用户对象
        User temp = userMapper.queryUserByName(userName);
        //如果用户对象为空,则表示用户名可用，如果用户对象不为空，则表示用户名不可用
        //如果是添加操作，数据库中无数据，只要通过名称查到数据，则表示用户名被占用
        //如果是修改操作，数据库中有对应记录，通过用户名查到数据，可能是当前记录本身，也可能是别的记录
        //如果用户名存在，且与当前修改记录不是同一个，则表示其他记录占用了该用户名，不可用
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(userId)), "用户名已存在，请重新输入！");
        //email not null
        AssertUtil.isTrue(StringUtils.isBlank(email), "用户邮箱不能为空！");
        //phone not null
        AssertUtil.isTrue(StringUtils.isBlank(phone), "用户手机号不能为空！");
        //phone format
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "用户手机号格式不正确！");
    }


    /**
     * 用户删除
     *
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids) {
        //判断ids是否为空，长度是否大于0
        AssertUtil.isTrue(ids == null || ids.length == 0, "待删除记录不存在！");
        //执行删除操作，判断受影响行数
        AssertUtil.isTrue(userMapper.deleteBatch(ids) != ids.length, "用户删除失败");

        //遍历用户Id的数组
        for (Integer userId : ids) {
            Integer count = userRoleMapper.countUserRoleByUserId(userId);

            if (count > 0) {
                AssertUtil.isTrue(userMapper.deleteByPrimaryKey(userId) != count, "删除用户失败！");
            }
        }
    }


}
