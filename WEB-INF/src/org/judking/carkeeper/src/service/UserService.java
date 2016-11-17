package org.judking.carkeeper.src.service;

import org.judking.carkeeper.src.DAO.IUserDAO;
import org.judking.carkeeper.src.model.MyUserDetails;
import org.judking.carkeeper.src.model.UserModel;
import org.judking.carkeeper.src.util.DbHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service("userService")
public class UserService implements UserDetailsService {
    @Autowired
    @Qualifier("IUserDAO")
    private IUserDAO iUserDAO;
    @Autowired
    @Qualifier("transactionManager")
    private DataSourceTransactionManager dataSourceTransactionManager;

    public static final String USER_EXIST = "*user_exist";
    public static final String DB_FAIL = "*database_fail";
    public static final String USER_NOT_EXIST = "*user_not_exist";
    public static final String VERIFY_FAIL = "*verify_fail";

    /**
     * user register post
     *
     * @param username
     * @param passwd
     * @return 三种返回值：注册成功；用户已存在；数据库写入出错。
     */
    public UserModel registry(String username, String passwd) {
        UserModel userModel = new UserModel(username, passwd);
        String privateToken = genPrivateToken();
        userModel.setPrivate_token(privateToken);

        //判断用户名数据库中是否存在
        UserModel existUserModel = iUserDAO.selectUser(userModel.getUser_name());
        if (existUserModel != null) {
            userModel.setPrivate_token(USER_EXIST);
            return userModel;
        }

        // begin a transaction for registering to DB
        TransactionStatus status = dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            DbHelper.assertGtZero(iUserDAO.insertUser(userModel));

            dataSourceTransactionManager.commit(status);
        } catch (RuntimeException e) {
            dataSourceTransactionManager.rollback(status);
            userModel.setPrivate_token(DB_FAIL);
            return userModel;
        }

        return userModel;
    }

    private String genPrivateToken() {
        return new BigInteger(32, new Random(System.currentTimeMillis())).toString(32);
    }

    /**
     * user login post
     *
     * @param username
     * @param passwd
     * @return 三种返回值：登陆成功；用户不存在；密码错误
     */
    public String login(String username, String passwd) {
        UserModel userModel = iUserDAO.selectUser(username);
        if (userModel == null) {
            return USER_NOT_EXIST;
        }
        userModel = iUserDAO.verifyUser(username, passwd);
        if (userModel == null) {
            return VERIFY_FAIL;
        }
        return userModel.getPrivate_token();
    }

    /**
     * get current Login user's UserInfoBasicModel.
     * if not login, return null
     *
     * @return
     */
    public UserModel tryGetCurrentLoginUserModel() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails) {
            MyUserDetails userDetails = (MyUserDetails) principal;
            if (userDetails.getUserModel() == null) {
                throw new RuntimeException("judking## : MyUserDetails object is in security context but UserModel binding to it is null");
            }
            return userDetails.getUserModel();
        } else {
            return null;
        }
    }

    /**
     * update current Login user's UserInfoBasicModel
     * if not login, throw new runtime exception
     */
    public void updateCurrentLoginUserInfoBasicModel() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails) {
            MyUserDetails userDetails = (MyUserDetails) principal;
            UserModel userModel = iUserDAO.selectUser(userDetails.getUsername());
            //update user basic info model into current myUserDetails
            userDetails.setUserModel(userModel);
        } else {
            throw new RuntimeException("judking## : call on UserService.updateCurrentLoginUserInfoBasicModel() before login");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserModel userModel = iUserDAO.selectUser(username);
        if (userModel == null) {
            throw new UsernameNotFoundException("username: " + username + ", not exist");
        }

        //get access authority of current user
        List<GrantedAuthority> authsList = new ArrayList<>();
        if (username.equals("qqq")) {
            authsList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        authsList.add(new SimpleGrantedAuthority("ROLE_USER"));

        //create UserDetails
        UserDetails userDetails = new MyUserDetails(
                username, userModel.getUser_passwd(), true, true, true, true,
                authsList, userModel);

        return userDetails;
    }

}
