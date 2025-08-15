package com.fantasy.rabbitaicodemother.core.saver;

import cn.hutool.core.util.StrUtil;
import com.fantasy.rabbitaicodemother.ai.model.HtmlCodeResult;
import com.fantasy.rabbitaicodemother.exception.BusinessException;
import com.fantasy.rabbitaicodemother.exception.ErrorCode;
import com.fantasy.rabbitaicodemother.model.enums.CodeGenTypeEnum;

/**
 * HTML 代码文件保存器
 *
 * @author Fantasy
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        // HTML 代码不能为空
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML 代码不能为空");
        }
    }
}
