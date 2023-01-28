import {
  ProColumns,
  ProTable,
} from '@ant-design/pro-components';
import { Modal } from 'antd';
import React from 'react';

export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<API.RuleListItem>;

export type Props = {
  columns: ProColumns<API.InterfaceInfo>[];
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: API.InterfaceInfo) => Promise<void>;
  updateModalOpen: boolean;
};

const CreateModal: React.FC<Props> = (props) => {
  const {updateModalOpen,columns,onCancel,onSubmit} = props;
  return <Modal footer={null} open={updateModalOpen} onCancel={() => onCancel?.()}>
    <ProTable type='form' columns={columns} onSubmit={async (values) => {
      onSubmit?.(values);
        }}></ProTable>
  </Modal>
};

export default CreateModal;
