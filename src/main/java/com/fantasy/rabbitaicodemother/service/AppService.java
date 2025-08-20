package com.fantasy.rabbitaicodemother.service;

import com.fantasy.rabbitaicodemother.model.dto.app.AppAddRequest;
import com.fantasy.rabbitaicodemother.model.dto.app.AppQueryRequest;
import com.fantasy.rabbitaicodemother.model.entity.User;
import com.fantasy.rabbitaicodemother.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.fantasy.rabbitaicodemother.model.entity.App;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/777nx">Fantasy</a>
 */
public interface AppService extends IService<App> {

    /**
     * 通过对话生成应用代码
     *
     * @param appId     应用 ID
     * @param message   提示词
     * @param loginUser 登录用户
     * @return 应用代码
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * 创建应用
     *
     * @param appAddRequest 应用创建请求
     * @param loginUser     登录用户
     * @return 应用 ID
     */
    Long createApp(AppAddRequest appAddRequest, User loginUser);

    /**
     * 应用部署
     * @param appId 应用 ID
     * @param loginUser 登录用户
     * @return 可访问的部署地址
     */
    String deployApp(Long appId, User loginUser);

    /**
     * 异步生成应用截图并更新封面
     *
     * @param appId  应用 ID
     * @param appUrl 应用访问 URL
     */
    void generateAppScreenshotAsync(Long appId, String appUrl);

    /**
     * 获取应用封装类
     *
     * @param app 应用
     * @return 应用封装类
     */
    AppVO getAppVO(App app);

    /**
     * 获取应用封装列表
     *
     * @param appList 应用列表
     * @return 应用封装列表
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 构造应用查询条件
     *
     * @param appQueryRequest 应用查询请求
     * @return 应用查询条件
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 删除 AI 生成文件
     *
     * @param appId 应用 ID
     * @param codeGenType 代码生成类型
     */
    void deleteGeneratedFiles(Long appId, String codeGenType, String deployKey);
}
