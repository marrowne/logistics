const dev = {
    identity: {
        endpoint: {
            check_token: "/oauth/token",
            port: 8082,
            protocol: "http",
            host: "localhost",
            client_id: "parcelsSecured",
            client_secret: "secret"
        }
    },
    hr: {
        endpoint: {
            prefix: "/hr",
            port: 8081,
            protocol: "http",
            host: "localhost"
        }
    },
    tracking: {
        endpoint: {
            prefix: "/tracking",
            courier: {
                received: "/tracking/parcel/courier/received/",
                delivered: "/tracking/parcel/courier/delivered/",
                returned: "/tracking/parcel/courier/returned/"
            },
            sorting: {
                received: "/tracking/parcel/sorting/received/",
                left: "/tracking/parcel/sorting/left/"
            },
            port: 8080,
            protocol: "http",
            host: "localhost"
        }
    }
};

const prod = {
    identity: {
        endpoint: {
            check_token: "/oauth/token",
            port: "8082",
            protocol: "http",
            host: "identity.logistics.mordawski.it",
            client_id: "parcelsSecured",
            client_secret: "secret"
        }
    },
    hr: {
        endpoint: {
            prefix: "/hr",
            port: 8081,
            protocol: "http",
            host: "hr.logistics.mordawski.it"
        }
    },
    tracking: {
        endpoint: {
            prefix: "/tracking",
            courier: {
                received: "/tracking/parcel/courier/received/",
                delivered: "/tracking/parcel/courier/delivered/",
                returned: "/tracking/parcel/courier/returned/"
            },
            sorting: {
                received: "/tracking/parcel/sorting/received/",
                left: "/tracking/parcel/sorting/left/"
            },
            port: 8080,
            protocol: "http",
            host: "tracking.logistics.mordawski.it"
        }
    }
};

export const properties = process.env.REACT_APP_STAGE === 'dev'
    ? dev
    : prod;

export function buildURLFor(template, host, protocol, port) {
    return protocol
        + "://"
        + host + ":" + port
        + template;
};