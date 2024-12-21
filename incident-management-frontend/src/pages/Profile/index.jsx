import Layout, { Content, Footer } from 'antd/lib/layout/layout';
import React from 'react'
import FooterGroup from '../../components/FooterGroup';
import IncidentProfile from '../../components/IncidentProfile'
import "./profile.css";
export default function Profile(props) {
    return (
        <Layout style={{ height: "100vh", backgroundColor: "white" }}>
            <Content style={{ alignSelf: "center" }}>
                <IncidentProfile id={props.match.params.id} />
            </Content>
            <Footer style={{ textAlign: "center", backgroundColor: "white" }}>
                <FooterGroup />
            </Footer>
        </Layout>
    )
}
