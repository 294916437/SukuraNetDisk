package easypan.mappers;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:分享信息mapper
 * @Author: 春的樱树
 * @Date: 2024-04-29
 */
public interface FileShareMapper<T,P> extends BaseMapper {
	/**
	 *根据ShareId查询
	 */
	T selectByShareId(@Param("shareId") String shareId);

	/**
	 *根据ShareId更新
	 */
	Integer updateByShareId(@Param("bean") T t, @Param("shareId") String shareId);

	/**
	 *根据ShareId删除
	 */
	Integer deleteByShareId(@Param("shareId") String shareId);

	Integer deleteFileShareBatch(@Param("shareIds") String[] shareIds, @Param("userId") String userId);

	void updateViewShareCount(@Param("shareId") String shareId);


}