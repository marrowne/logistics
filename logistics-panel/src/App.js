import React from 'react'
import {Admin, AppBar, Layout, MenuItemLink, Resource, UserMenu} from 'react-admin';
import Dashboard from './Dashboard';
import authProvider from './authProvider';
import dataProvider from './dataProviders';
import {ParcelCreate, ParcelFilter, ParcelList, ParcelShow} from "./parcels";
import {EmployeeCreate, EmployeeList, EmployeeShow} from "./employees";
import PersonIcon from '@material-ui/icons/Person';
import UserIcon from '@material-ui/icons/Group';
import LocalShippingIcon from '@material-ui/icons/LocalShipping';

const displayUsername = () => {
        if (localStorage.getItem('fullName') === null) {
                return localStorage.getItem('username');
        } else {
                return localStorage.getItem('fullName') + " (" + localStorage.getItem('username') + ")";
        }
};

const MenuWithId = props => (
    <UserMenu {...props}>
            <MenuItemLink
                to=""
                primaryText={displayUsername()}
                leftIcon={<PersonIcon />}
            />
    </UserMenu>
);

const CustomAppBar = props => <AppBar {...props} userMenu={<MenuWithId />} />;

const CustomLayout = props => <Layout {...props} appBar={CustomAppBar} />;

const App = () => (
        <Admin title="Parcels Monitoring" dashboard={Dashboard} authProvider={authProvider} dataProvider={dataProvider} appLayout={CustomLayout}>
            {permissions => [
                (permissions === 'ADMINISTRATOR' || permissions === 'COURIER')
                    ? <Resource name="parcel" show={ParcelShow} list={ParcelList} filter={ParcelFilter} create={ParcelCreate} permissions={localStorage.getItem('authority')}  icon={LocalShippingIcon} />
                    : <Resource name="parcel" show={ParcelShow} list={ParcelList} filter={ParcelFilter} permissions={localStorage.getItem('authority')}  icon={LocalShippingIcon} />,
                permissions === 'ADMINISTRATOR'
                    ? <Resource name="employee" show={EmployeeShow} list={EmployeeList} create={EmployeeCreate} permissions={localStorage.getItem('authority')} icon={UserIcon} />
                    : null,
            ]}
        </Admin>
);

export default App;