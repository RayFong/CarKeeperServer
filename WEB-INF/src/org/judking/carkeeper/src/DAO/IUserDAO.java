package org.judking.carkeeper.src.DAO;

import org.apache.ibatis.annotations.Param;
import org.judking.carkeeper.src.model.UserModel;

public interface IUserDAO {
    public int insertUser(UserModel userModel);

    public UserModel selectUser(@Param("user_name") String user_name);

    public UserModel verifyUser(@Param("user_name") String user_name,
                                @Param("user_passwd") String user_passwd);

    public UserModel verifyUserName(@Param("user_name") String user_name,
                                    @Param("private_token") String private_token);

    public Integer CheckUserVin(@Param("user_id") String user_id,
                                @Param("vin") String vin);

    public Integer insertUserVin(@Param("user_id") String user_id,
                                 @Param("vin") String vin);

}
