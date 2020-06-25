import { fetchUtils } from 'react-admin';
import { stringify } from 'query-string';
import { properties, buildURLFor } from './config.js';

const httpClient = (url, options = {}) => {
    const token = localStorage.getItem('token');
    options.user = {
        authenticated: true,
        token: "Bearer " + token
    };
    return fetchUtils.fetchJson(url, options);
};

const getUrl = (resource) => {
    switch (resource) {
        case 'employee':
            return buildURLFor(
                properties.hr.endpoint.prefix,
                properties.hr.endpoint.host,
                properties.hr.endpoint.protocol,
                properties.hr.endpoint.port);
        case 'parcel':
            return buildURLFor(
                properties.tracking.endpoint.prefix,
                properties.tracking.endpoint.host,
                properties.tracking.endpoint.protocol,
                properties.tracking.endpoint.port);
        default:
            throw new Error("Data provider does not exist for resource " + resource +".");
    }
};


var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};

export default {
    getList: function (resource, params) {
        var _a = params.pagination, page = _a.page, perPage = _a.perPage;
        var _b = params.sort, field = _b.field, order = _b.order;
        var query = {
            sort: JSON.stringify([field, order]),
            range: JSON.stringify([(page - 1) * perPage, page * perPage - 1]),
            filter: JSON.stringify(params.filter),
        };

        var url = getUrl(resource) + "/" + resource + "?" + stringify(query);
        return httpClient(url).then(function (_a) {
            var headers = _a.headers, json = _a.json;
            if (!headers.has('content-range')) {
                throw new Error('The Content-Range header is missing in the HTTP Response. The simple REST data provider expects responses for lists of resources to contain this header with the total number of results to build the pagination. If you are using CORS, did you declare Content-Range in the Access-Control-Expose-Headers header?');
            }
            return {
                data: json,
                total: parseInt(headers
                    .get('content-range')
                    .split('/')
                    .pop(), 10),
            };
        });
    },
    getOne: function (resource, params) {
        return httpClient(getUrl(resource) + "/" + resource + "/" + params.id).then(function (_a) {
            var json = _a.json;
            return ({
                data: json,
            });
        });
    },
    getMany: function (resource, params) {
        var query = {
            filter: JSON.stringify({ id: params.ids }),
        };
        var url = getUrl(resource) + "/" + resource + "?" + stringify(query);
        return httpClient(url).then(function (_a) {
            var json = _a.json;
            return ({ data: json });
        });
    },
    getManyReference: function (resource, params) {
        var _a;
        var _b = params.pagination, page = _b.page, perPage = _b.perPage;
        var _c = params.sort, field = _c.field, order = _c.order;
        var query = {
            sort: JSON.stringify([field, order]),
            range: JSON.stringify([(page - 1) * perPage, page * perPage - 1]),
            filter: JSON.stringify(__assign(__assign({}, params.filter), (_a = {}, _a[params.target] = params.id, _a))),
        };
        var url = getUrl(resource) + "/" + resource + "?" + stringify(query);
        return httpClient(url).then(function (_a) {
            var headers = _a.headers, json = _a.json;
            if (!headers.has('content-range')) {
                throw new Error('The Content-Range header is missing in the HTTP Response. The simple REST data provider expects responses for lists of resources to contain this header with the total number of results to build the pagination. If you are using CORS, did you declare Content-Range in the Access-Control-Expose-Headers header?');
            }
            return {
                data: json,
                total: parseInt(headers
                    .get('content-range')
                    .split('/')
                    .pop(), 10),
            };
        });
    },
    update: function (resource, params) {
        return httpClient(getUrl(resource) + "/" + resource + "/" + params.id, {
            method: 'PUT',
            body: JSON.stringify(params.data),
        }).then(function (_a) {
            var json = _a.json;
            return ({ data: json });
        });
    },
    // simple-rest doesn't handle provide an updateMany route, so we fallback to calling update n times instead
    updateMany: function (resource, params) {
        return Promise.all(params.ids.map(function (id) {
            return httpClient(getUrl(resource) + "/" + resource + "/" + id, {
                method: 'PUT',
                body: JSON.stringify(params.data),
            });
        })).then(function (responses) { return ({ data: responses.map(function (_a) {
                var json = _a.json;
                return json.id;
            }) }); });
    },
    create: function (resource, params) {
        return httpClient(getUrl(resource) + "/" + resource, {
            method: 'POST',
            body: JSON.stringify(params.data),
        }).then(function (_a) {
            var json = _a.json;
            return ({
                data: __assign(__assign({}, params.data), { id: json.id }),
            });
        });
    },
    delete: function (resource, params) {
        return httpClient(getUrl(resource) + "/" + resource + "/" + params.id, {
            method: 'DELETE',
        }).then(function (_a) {
            var json = _a.json;
            return ({ data: json });
        });
    },
    // simple-rest doesn't handle filters on DELETE route, so we fallback to calling DELETE n times instead
    deleteMany: function (resource, params) {
        return Promise.all(params.ids.map(function (id) {
            return httpClient(getUrl(resource) + "/" + resource + "/" + id, {
                method: 'DELETE',
            });
        })).then(function (responses) { return ({ data: responses.map(function (_a) {
                var json = _a.json;
                return json.id;
            }) }); });
    },
};
