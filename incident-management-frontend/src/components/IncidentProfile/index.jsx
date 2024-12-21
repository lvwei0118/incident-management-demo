/* eslint-disable react-hooks/exhaustive-deps */
import React, {useEffect, useState} from 'react'
import axios from "axios";
import { Image, Descriptions, Divider } from "antd";
import { GithubOutlined, MailOutlined, UserOutlined } from "@ant-design/icons"
import "./incident-profile.css";
import IncidentList from "../IncidentList";

const DescriptionsItem = Descriptions.Item;

export default function IncidentProfile(props) {
    const [incident, setIncident] = useState([]);
    const id = props.id;
    const labelStyle = {
        fontWeight: "600",
        fontFamily: "monospace",
    }
    useEffect(() => {
        axios.get("http://localhost:8080/api/findById/" + id)
             .then((rsp) => {
                 setIncident(rsp.data);
             })
             .catch((error) => {
                 console.log(error);
             })
    }, []);
    return (
        <div className="outer-wrapper">
            <div className="inner-contianer">
                <div>
                    <Image className="incident-avatar" src={ incident.avatar } alt={ incident.name } width={180} />
                    <div className="incident-code">
                        { incident.code }
                    </div>
                    <div className="incident-name">
                        { incident.name }
                    </div>
                    <div className="incident-type">
                        { incident.type }
                    </div>
                    <div className="incident-description">
                        <div>{ incident.description }</div>
                    </div>
                </div>
                <Divider type="vertical" style={{ height: "460px", marginTop: "10px", marginLeft: "25px" }} />
            </div>
        </div>
    )
}
