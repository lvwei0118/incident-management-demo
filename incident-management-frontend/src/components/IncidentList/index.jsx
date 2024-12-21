import React, {useEffect, useState} from 'react'
import {Table, Button, Avatar, Popconfirm, Modal, Select, Pagination, message} from "antd";
import axios from "axios";
import "./incident-list.css";
import IncidentForm from '../IncidentForm';
import {Link} from 'react-router-dom';
import {updDataValidate} from '../../Util';

const {Option} = Select;

var HTTP = axios.create({
    // baseURL:'http://localhost:8080/', //这是基础url
    headers: {'Content-Type': 'application/json'},
    // transformRequest: [function (data) {
    //     // Do whatever you want to transform the data
    //     let ret = ''
    //     for (let it in data) {
    //         ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
    //     }
    //     return ret
    // }]
});

export default function IncidentList(props) {
    // define dataSource && some states
    const [dataSource, setDataSource] = useState([]);
    const [updVal, setUpdVal] = useState([]);
    const [isAddModalVisible, setIsAddModalVisible] = useState(false);
    const [isUpdModalVisible, setIsUpdModalVisible] = useState(false);
    const [searchText, setSearchText] = useState(undefined);
    const [searchData, setSearchData] = useState([]);
    const [page, setPage] = useState(1);
    const [size, setSize] = useState(10);
    const [total, setTotal] = useState(100);

    // utils
    const delFromArrayByItemElm = (arr, id) => {
        for (let i = 0; i < arr.length; i++) {
            if (arr[i].id === id) return i;
        }
    }

    /**
     *
     * @param {Array} arr
     * @param {Object} item
     * @returns
     */
    const updArrayByItem = (arr, item) => {
        let newArr = arr.map((arrItem) => {
            if (arrItem.id === item.id) {
                return item;
            } else {
                return arrItem;
            }
        });
        return newArr;
    }

    /**
     * Query by name
     * @param {string} key
     * @param {Array} arr
     */
    const fuzzyQuery = (arr, key) => {
        let fuzzyArr = [];
        arr.forEach(element => {
            if (element.name.indexOf(key) >= 0) {
                fuzzyArr.push(element);
            }
        });
        return fuzzyArr;
    }

    /**
     *
     * @returns {Array} newArr 一个新的已被更新的数组
     * @param page
     * @param size
     */
    const getList = (page, size) => {
        HTTP.get("/api/list", {params: {page: page - 1, size: size}})
            .then((rsp) => {
                console.log(rsp);
                setDataSource(rsp.data.content);
                setTotal(rsp.data.totalElements);
            })
            .catch((error) => {
                console.log(error)
            })
    }

    // index data
    useEffect(() => {
        getList(page, size);
    }, [page, size]);

    // CRUD -> D
    const handleDelete = (index) => {
        HTTP.delete('/api/deleteById/' + index.id)
            .then((rsp) => {
                let tmpData = [...dataSource];
                let i = delFromArrayByItemElm(tmpData, index.id);
                tmpData.splice(i, 1);
                //  console.log(tmpData)
                setDataSource(tmpData)
            })
            .catch((error) => {
                console.log(error)
            })
    }

    // CRUD -> C
    const handleAdd = (value) => {
        HTTP.post('/api/save/', value)
            .then((rsp) => {
                // let tmpData = [...dataSource];
                console.log(rsp.data);
                if (rsp.data === 'success') {
                    message.success('add success');
                    getList(page, size);
                } else {
                    message.error(rsp.data.message);
                }
                // setDataSource(tmpData);
            })
            .catch((error) => {
                console.log(error)
            })
    }

    // CRUD -> U
    const handleUpd = (value) => {
        HTTP.put('/api/update/', value)
            .then((rsp) => {
                // 替换原来 dataSource 中的item
                let tmpData = updArrayByItem([...dataSource], value);
                setDataSource(tmpData);
            })
            .catch((error) => {
                console.log(error)
            })
    }

    const onUpdClick = index => {
        // 处理特殊数据
        // index.department = [index.department]
        // index.joinDate = moment(index.joinDate, 'YYYY/MM')
        // index.gender = [index.gender];
        let data = updDataValidate(index);
        // console.log("index data: ",index);
        setIsUpdModalVisible(true);
        setUpdVal(data);
    }

    // CRUD -> R
    const onSearch = value => {
        if (value) {
            setSearchText(value);
            let tmpData = fuzzyQuery(dataSource, value);
            setSearchData(tmpData);
        }
    }

    const onClickSearchItem = value => {
        let path = "/profile/" + value;
        props.history.push(path);
        setSearchData([]);
    }

    // table header
    const columns = [
        {
            title: 'Code',
            dataIndex: 'code',
            key: 'code',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Type',
            dataIndex: 'type',
            key: 'type',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Operation',
            dataIndex: 'operation',
            key: 'operation',
            render: (_, index) =>
                dataSource.length >= 1 ? (
                    <div className="del-update-container">
                        <Button size="small" type="primary" onClick={() => onUpdClick(index)}>Update</Button>
                        <Popconfirm title="Sure to delete?" onConfirm={() => handleDelete(index)}>
                            <Button style={{marginLeft: 5}} size="small" danger type="primary">Delete</Button>
                        </Popconfirm>
                    </div>
                ) : null
        }
    ]

    const onChange = (pageNumber) => {
        setPage(pageNumber);
    }
    const onShowSizeChange = (current, pageSize) => {
        setPage(current);
        setSize(pageSize);
    }

    const onAddSubmit = () => {
        setIsAddModalVisible(false)
    }

    const onUpdSubmit = () => {
        setIsUpdModalVisible(false)
    }

    return (
        <div className="incident-list">
            <div className="add-search-container">
                <Button
                    type="primary"
                    onClick={() => setIsAddModalVisible(true)}
                >
                    添加事件
                </Button>
                <Select
                    style={{width: 200}}
                    placeholder="input search text"
                    showSearch
                    showArrow={false}
                    filterOption={false}
                    notFoundContent="输入查询条件"
                    value={searchText}
                    onSearch={onSearch}
                    onChange={onClickSearchItem}
                >
                    {searchData.map(d => (
                        <Option key={d.id}>{d.name}</Option>
                    ))
                    }
                </Select>
            </div>

            <Modal
                style={{display: "flex", justifyContent: "center"}}
                destroyOnClose={true}
                title="Add a incident"
                visible={isAddModalVisible}
                footer={[]}
                onCancel={() => setIsAddModalVisible(false)}
            >
                <IncidentForm handleAdd={handleAdd} onAddSubmit={onAddSubmit}/>
            </Modal>

            <Modal
                style={{display: "flex", justifyContent: "center"}}
                destroyOnClose={true}
                title="Update a incident"
                visible={isUpdModalVisible}
                footer={[]}
                onCancel={() => setIsUpdModalVisible(false)}
            >
                <IncidentForm handleUpd={handleUpd} values={updVal} onUpdSubmit={onUpdSubmit}/>
            </Modal>

            <Table
                columns={columns}
                rowKey={(record) => {
                    return record.id
                }}
                dataSource={dataSource}
                scroll={{y: "470px"}}
            />

            <Pagination current={page} pageSize={size} total={total} onChange={onChange} showSizeChanger
                        onShowSizeChange={onShowSizeChange}/>
        </div>
    )
}
