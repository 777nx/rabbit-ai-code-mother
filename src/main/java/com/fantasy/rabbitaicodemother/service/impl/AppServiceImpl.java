package com.fantasy.rabbitaicodemother.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.fantasy.rabbitaicodemother.model.entity.App;
import com.fantasy.rabbitaicodemother.mapper.AppMapper;
import com.fantasy.rabbitaicodemother.service.AppService;
import org.springframework.stereotype.Service;

/**
 * 应用 服务层实现。
 *
 * @author <a href="https://github.com/777nx">Fantasy</a>
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService{

}
