import React from 'react'
import { Form, Input, Cascader, Button, Col, Row, Switch, DatePicker, Rate, Slider, Checkbox } from "antd";
import { dataValidate } from '../../Util';
const FormItem = Form.Item;


export default function IncidentForm(props) {
    const typeOps = [
        {
            label: "授信",
            value: "1",
        },
        {
            label: "放款",
            value: "2"
        },
        {
            label: "还款",
            value: "3"
        },
        {
            label: "贷后",
            value: "4"
        },
    ]

    const onSwitchChange = (checked) => {
        console.log(checked);
    }

    const onFinish = (values) => {
        // 如果没有表单数据传过来 => add
        let data = dataValidate(values);
        if(!props.values) {
            props.handleAdd(data);
            props.onAddSubmit();
        } else {
            // 有表单数据传来 => update
            // 将数据返回给父组件
            data["id"] = props.values.id;
            data["avatar"] = props.values.avatar;
            console.log(data);
            props.handleUpd(values);
            props.onUpdSubmit();
        }
    }

    const layout = {
        labelCol: { span: 8 },
        wrapperCol: { span: 16 },
    };

    const gutter = {
        xs: 8,
        sm: 16,
        md: 24,
        lg: 32,
    }

    return (
        <div>
            <Form
                {...layout}
                initialValues={props.values}
                style={{ width: 650 }}
                onFinish={ onFinish }>
                    <Row gutter={gutter}>
                        <Col span={12}>
                            <FormItem
                                className="code"
                                label="Code"
                                name="code"
                                rules={[
                                    { required: true, message: 'Please input incident code!' },
                                    { required: true, max: 8, whitespace: true, message: '编码不能超过8个字符长度！'}
                                ]}
                            >
                                <Input placeholder="code" allowClear />
                            </FormItem>
                        </Col>
                        <Col span={12}>
                            <FormItem
                                className="name"
                                label="Name"
                                name="name"
                                rules={[
                                    { required: true, message: 'Please input incident name!' }
                                ]}
                            >
                                <Input placeholder="name" allowClear />
                            </FormItem>
                        </Col>
                    </Row>

                    <Row gutter={gutter}>
                        <Col span={12}>
                            <FormItem
                                className="type"
                                label="Type"
                                name="type"
                                rules={[{ required: true, message: 'Please choose incident type!' }]}
                            >
                                <Cascader options={typeOps} />
                            </FormItem>
                        </Col>
                        <Col span={12}>
                            <FormItem
                                className="IsCache"
                                label="Is Cache"
                                name="isCache"
                                valuePropName="checked"
                            >
                                <Switch checkedChildren="0"
                                        unCheckedChildren="1"
                                        onChange={onSwitchChange}
                                />
                            </FormItem>
                        </Col>
                    </Row>

                    <Row gutter={gutter}>
                        <Col span={16}>
                            <FormItem
                                className="desc"
                                label="Description"
                                name="description"
                                rules={[
                                    { required: true, message: 'Please input incident description!' },
                                ]}
                            >
                                <Input.TextArea allowClear />
                            </FormItem>
                        </Col>
                    </Row>

                    <FormItem>
                        <Button style={{ width: "650px" }} htmlType="submit" type="primary">
                            Submit
                        </Button>
                    </FormItem>
            </Form>
        </div>
    )
}
