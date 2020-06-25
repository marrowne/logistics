import {AUTH_CHECK, AUTH_GET_PERMISSIONS, AUTH_LOGIN, AUTH_LOGOUT} from 'react-admin';
import dataProvider from './dataProviders';
import { properties, buildURLFor } from './config.js';

const FetchUsername = (userId) => {
    dataProvider.getOne("employee", {id: "me"})
        .then((response) => {
            const userName = response.data.fullName.firstName.concat(' ').concat(response.data.fullName.lastName);
            const position = response.data.position;
            localStorage.setItem('fullName', userName);
            localStorage.setItem('position', position);
        });
};

export default (type, params) => {
    if (type === AUTH_LOGIN) {
        const { username, password } = params;
        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('username', username);
        urlSearchParams.append('password', password);
        urlSearchParams.append('grant_type','password');
        const request = new Request(
            buildURLFor(
                properties.identity.endpoint.check_token,
                properties.identity.endpoint.host,
                properties.identity.endpoint.protocol,
                properties.identity.endpoint.port), {
            method: 'POST',
            body: urlSearchParams.toString(),
            headers: new Headers({'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
                'Authorization': 'Basic '+btoa("parcelsSecured:secret")}),
        })

        return fetch(request)
            .then(response => {
                if (response.status < 200 || response.status >= 300) {
                    throw new Error(response.statusText);
                }
                return response.json();
            })
            .then(({ access_token, authority }) => {
                localStorage.setItem('token', access_token);
                localStorage.setItem('username', username);
                localStorage.setItem('authority', authority);
                FetchUsername();
            });
    }
    if (type === AUTH_CHECK) {
        return localStorage.getItem('token') ? Promise.resolve() : Promise.reject();
    }
    if (type === AUTH_LOGOUT) {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('authority');
        localStorage.removeItem('fullName');
        localStorage.removeItem('position');
        return Promise.resolve();
    }
    if (type === AUTH_GET_PERMISSIONS) {
        const authority = localStorage.getItem('authority');
        return authority ? Promise.resolve(authority) : Promise.reject();
    }
    return Promise.resolve();
}
