package com.fantasy.rabbitaicodemother.service;

import com.fantasy.rabbitaicodemother.model.dto.app.AppQueryRequest;
import com.fantasy.rabbitaicodemother.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.fantasy.rabbitaicodemother.model.entity.App;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/777nx">Fantasy</a>
 */
public interface AppService extends IService<App> {

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
}
