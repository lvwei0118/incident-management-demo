import React from 'react'
import { Layout } from "antd";
import IncidentList from '../../components/IncidentList'
import FooterGroup from '../../components/FooterGroup';
import "./main.css"

const { Footer, Content } = Layout;

export default function Main(props) {
    return (
        <Layout style={{ height: "100vh", backgroundColor: "white" }}>
            <Content style={{ alignSelf: "center" }}>
                <IncidentList history={ props.history } />
            </Content>
            <Footer style={{ textAlign: "center", padding: 0, backgroundColor: "white" }}><FooterGroup /></Footer>
        </Layout>
    )
}
