package easypan.mappers;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:mapper
 * @Author: 春的樱树
 * @Date: 2024-02-23
 */
public interface EmailCodeMapper<T,P> extends BaseMapper {
	/**
	 *根据EmailAndCode查询
	 */
	T selectByEmailAndCode(@Param("email") String email, @Param("code") String code);

	/**
	 *根据EmailAndCode更新
	 */
	Integer updateByEmailAndCode(@Param("bean") T t, @Param("email") String email, @Param("code") String code);

	/**
	 *根据EmailAndCode删除
	 */
	Integer deleteByEmailAndCode(@Param("email") String email, @Param("code") String code);
	/**
	 *取消邮箱验证码资格
	 */
	void disableEmailCode(@Param("email") String email);


}