package com.easydata.core.constants;

/**
 * 返回码常量<br />
 *
 * @author Mr.Pro
 */
public interface ReturnCode {
	
	/**
	 * 操作成功
	 */
	public static final int SUCCESS = 1;
	
	/**
	 * 主键值为空
	 */
	public static final int ID_IS_EMPTY = -1;
	
	/**
	 * 主键已存在
	 */
	public static final int ID_EXISTS=-2;
	
	/**
	 * 主键不存在
	 */
	public static final int ID_NOT_EXISTS=-3;
	
	/**
	 * AnnotationBean和java bean数据转换失败(注解类转换失败)
	 */
	public static final int PARSE_BEAN_FAILED=-4;
	
	/**
	 * 查询失败
	 */
	public static final int SEARCH_FAILED=-5;
	
	/**
	 * 更新失败
	 */
	public static final int UPDATE_FAILED=-6;
	
	/**
	 * 删除失败
	 */
	public static final int DELETE_FAILED=-7;
	
	/**
	 * 添加失败
	 */
	public static final int ADD_FAILED=-8;
	
	/**
	 * 该类缺少Id注解{@link Id}
	 */
	public static final int PARSE_BEAN_NOT_HAVE_ID = -9;
	
	/**
	 * 语句解析失败{@link QueryBuilder}
	 */
	public static final int PARSE_QUERY_FAILD = -10;
	
	/**
	 * 参数遗漏，可能存在为空的错误
	 */
	public static final int PARAMETER_MISSING = -11;
	
	/**
	 * 发送消息失败
	 */
	public static final int SEND_MESSAGE_FAILED=-201;
	
	/**
	 * 已存在该订阅器
	 */
	public static final int SUBSCRIBE_EXISTS=-202;
	
	/**
	 * 订阅失败
	 */
	public static final int SUBSCRIBE_FAILED=-203;
	
	/**
	 * 未找到该订阅器
	 */
	public static final int SUBSCRIBE_NOT_FOUND=-204;
	
	/**
	 * 取消监听失败
	 */
	public static final int UNSUBSCRIBE_FAILED=-205;
	
	/**
	 * 该队列尚未存在订阅器
	 */
	public static final int QUEUE_NOT_FOUND_SUBSCRIBE=-206;
	
	/**
	 * 清空缓存失败
	 */
	public static final int CLEAR_CACHE_FAILED=-401;
	
	/**
	 * 指定的文件不存在
	 */
	public static final int FILE_NOT_EXISTS = -501;
	
	/**
	 * 打开指定文件失败
	 */
	public static final int FILE_OPEN_FAILED = -502;
	
	/**
	 * 文件下载失败
	 */
	public static final int FILE_DOWNLOAD_FAILED = -503;
	
	/**
	 * 指定的文件或者文件夹不存在
	 */
	public static final int FILE_OR_DIRECTORY_NOT_EXISTS = -504;
	
	/**
	 * 移动失败
	 */
	public static final int MOVE_FAILED = -505;
	
	/**
	 * 指定的文件存在
	 */
	public static final int FILE_EXISTS = -506;
	
	/**
	 * 文件上传失败
	 */
	public static final int UPLOAD_FAILED = -507;
	
	/**
	 * 创建文件失败
	 */
	public static final int MAKE_FILE_FAILED = -508;
	
	/**
	 * 文件路径验证失败
	 */
	public static final int FILE_PATH_VALID_FAILED = -509;
	
	/**
	 * 文件引用失败
	 */
	public static final int FILE_LINK_FAILD = -510;
	
	/**
	 * 文件打包失败
	 */
	public static final int FILE_PACKAGE_FAILD = -511;
	
	/**
	 * 文件下载失败
	 */
	public static final int FILE_DELETE_FAILD = -512;
	
	/**
	 * 文件上传失败
	 */
	public static final int FILE_UPLOAD_FAILD = -513;

	/**
	 * 文件获取失败
	 */
	public static final int FILE_GET_FAILD = -514;
	
	/**
	 * 文件转换失败
	 */
	public static final int FILE_CONVERT_FAILD = -515;
	
	/**
	 * 打包添加文件失败
	 */
	public static final int FILE_PACKAGE_INSERT_FILES_FAILD = -516;
	
}
