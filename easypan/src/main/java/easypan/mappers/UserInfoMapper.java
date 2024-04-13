package easypan.mappers;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:存储用户信息mapper
 * @Author: 春的樱树
 * @Date: 2024-02-23
 */
public interface UserInfoMapper<T,P> extends BaseMapper {
	/**
	 *根据NumberAndUserId查询
	 */
	T selectByNumberAndUserId(@Param("number") Integer number, @Param("userId") String userId);

	/**
	 *根据NumberAndUserId更新
	 */
	Integer updateByNumberAndUserId(@Param("bean") T t, @Param("number") Integer number, @Param("userId") String userId);

	/**
	 *根据NumberAndUserId删除
	 */
	Integer deleteByNumberAndUserId(@Param("number") Integer number, @Param("userId") String userId);

	/**
	 *根据UserId查询
	 */
	T selectByUserId(@Param("userId") String userId);

	/**
	 *根据UserId更新
	 */
	Integer updateByUserId(@Param("bean") T t, @Param("userId") String userId);

	/**
	 *根据UserId删除
	 */
	Integer deleteByUserId(@Param("userId") String userId);

	/**
	 *根据Number查询
	 */
	T selectByNumber(@Param("number") Integer number);

	/**
	 *根据Number更新
	 */
	Integer updateByNumber(@Param("bean") T t, @Param("number") Integer number);

	/**
	 *根据Number删除
	 */
	Integer deleteByNumber(@Param("number") Integer number);

	/**
	 *根据Email查询
	 */
	T selectByEmail(@Param("email") String email);

	/**
	 *根据Email更新
	 */
	Integer updateByEmail(@Param("bean") T t, @Param("email") String email);

	/**
	 *根据Email删除
	 */
	Integer deleteByEmail(@Param("email") String email);

	/**
	 *根据NickName查询
	 */
	T selectByNickName(@Param("nickName") String nickName);

	/**
	 *根据NickName更新
	 */
	Integer updateByNickName(@Param("bean") T t, @Param("nickName") String nickName);

	/**
	 *根据NickName删除
	 */
	Integer deleteByNickName(@Param("nickName") String nickName);
	
	Integer updateUserSpace(@Param("userId") String userId,@Param("useSpace") Long useSpace,@Param("totalSpace") Long totalSpace);

}