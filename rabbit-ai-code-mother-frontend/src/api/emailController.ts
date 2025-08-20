// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 POST /email/send */
export async function sendVerificationCode(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.sendVerificationCodeParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/email/send', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
