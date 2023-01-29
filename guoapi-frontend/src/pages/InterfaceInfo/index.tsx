import {
  getInterfaceInfoByIdUsingGET,
  invokeInterfaceInfoUsingPOST,
} from '@/services/guoapi-backend/interfaceInfoController';
import { PageContainer } from '@ant-design/pro-components';
import { useParams } from '@umijs/max';
import { Button, Card, Descriptions, Form, Input, message, Spin } from 'antd';
import DescriptionsItem from 'antd/es/descriptions/Item';
import React, { useEffect, useState } from 'react';

/**
 * 主页
 * @returns
 */
const Index: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<API.InterfaceInfo>();
  const [invokeRes, setInvokeRes] = useState<any>();
  const [invokeLoading, setinvokeLoading] = useState(false);
  
  const params = useParams();

  /**
   * 测试调用接口
   * @param values
   * @returns
   */
  const onFinish = async (values: any) => {
    if (!params.id) {
      message.error('接口不存在');
      return;
    }
    setinvokeLoading(true);
    try {
      const res = await invokeInterfaceInfoUsingPOST({
        id: params.id,
        ...values,
      });
      setInvokeRes(res.data);
      message.success('请求成功');
    } catch (error: any) {
      message.error('操作失败：' + error.message);
    }
    setinvokeLoading(false);
  };

  /**
   * 初始加载数据
   * @returns
   */
  const loadData = async () => {
    if (!params.id) {
      message.error('参数不存在');
      return;
    }
    setLoading(true);
    try {
      const res = await getInterfaceInfoByIdUsingGET({
        id: Number(params.id),
      });
      setData(res.data);
    } catch (error: any) {
      message.error('请求失败：' + error.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, []);

  return (
    <PageContainer title="查看接口文档">
      <Card>
        {data ? (
          <Descriptions title={data.name} column={1}>
            <DescriptionsItem label="描述">{data.description}</DescriptionsItem>
            <DescriptionsItem label="接口状态">{data.status ? '正常' : '关闭'}</DescriptionsItem>
            <DescriptionsItem label="请求地址">{data.url}</DescriptionsItem>
            <DescriptionsItem label="请求方法">{data.method}</DescriptionsItem>
            <DescriptionsItem label="请求参数">{data.requestParams}</DescriptionsItem>
            <DescriptionsItem label="请求头">{data.requestHeader}</DescriptionsItem>
            <DescriptionsItem label="响应头">{data.responseHeader}</DescriptionsItem>
            <DescriptionsItem label="创建时间">{data.createTime}</DescriptionsItem>
            <DescriptionsItem label="更新时间">{data.updateTime}</DescriptionsItem>
          </Descriptions>
        ) : (
          <>接口不存在</>
        )}
      </Card>
      <Card title="在线测试">
        <Form name="invoke" layout="vertical" onFinish={onFinish} >
          <Form.Item label="请求参数：" name="userRequestParams">
            <Input.TextArea />
          </Form.Item>

          <Form.Item wrapperCol={{ span: 16 }}>
            <Button type="primary" htmlType="submit">调用</Button>
          </Form.Item>
        </Form>
      </Card>
      <Card title="返回结果" loading={invokeLoading}>
        { invokeRes }
      </Card>
    </PageContainer>
  );
};

export default Index;
