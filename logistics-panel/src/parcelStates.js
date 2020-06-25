import React from 'react'
import {Component} from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import {buildURLFor, properties} from "./config";


class CourierReceivedButton extends Component {
    handleClick = () => {
        const { record } = this.props;
        fetch(buildURLFor(
                properties.tracking.endpoint.courier.received,
                properties.tracking.endpoint.host,
                properties.tracking.endpoint.protocol,
                properties.tracking.endpoint.port) + `${record.id}`,
            { method: 'PATCH', headers: {'Authorization': 'Bearer ' + localStorage.getItem('token') } })
            .then(() => {
            })
            .catch((e) => {
                console.error(e);
            });
    };

    render() {
        const { record } = this.props;
        const is_state_incorrect = record.status !== 'CREATED'
            && record.status !== 'DEST_SORTING'
            && record.status !== 'RET_SRC_SORTING';
        return  <Button variant="contained" color="primary" disabled={is_state_incorrect} onClick={this.handleClick}>
                    Received
                    {/* This Button uses a Font Icon, see the installation instructions in the docs. */}
                </Button>

    }
}

CourierReceivedButton.propTypes = {
    push: PropTypes.func,
    record: PropTypes.object,
    showNotification: PropTypes.func,
};

class SortingReceivedButton extends Component {
    handleClick = () => {
        const { record } = this.props;
        fetch(buildURLFor(
                properties.tracking.endpoint.sorting.received,
                properties.tracking.endpoint.host,
                properties.tracking.endpoint.protocol,
                properties.tracking.endpoint.port) + `${record.id}`,
            { method: 'PATCH', headers: {'Authorization': 'Bearer ' + localStorage.getItem('token') } })
            .then(() => {
            })
            .catch((e) => {
                console.error(e);
            });
    };

    render() {
        const { record } = this.props;
        const is_state_incorrect = record.status !== 'PICKUP'
            && record.status !== 'TRANSIT'
            && record.status !== 'RETURNED'
            && record.status !== 'RET_TRANSIT';
        return  <Button variant="contained" color="secondary" disabled={is_state_incorrect} onClick={this.handleClick}>
            Received
            {/* This Button uses a Font Icon, see the installation instructions in the docs. */}
        </Button>

    }
}

SortingReceivedButton.propTypes = {
    push: PropTypes.func,
    record: PropTypes.object,
    showNotification: PropTypes.func,
};

class SortingLeftButton extends Component {
    handleClick = () => {
        const { record } = this.props;
        fetch(buildURLFor(
                properties.tracking.endpoint.sorting.left,
                properties.tracking.endpoint.host,
                properties.tracking.endpoint.protocol,
                properties.tracking.endpoint.port) + `${record.id}`,
            { method: 'PATCH', headers: {'Authorization': 'Bearer ' + localStorage.getItem('token') } })
            .then(() => {
            })
            .catch((e) => {
                console.error(e);
            });
    };

    render() {
        const { record } = this.props;
        const is_state_incorrect = record.status !== 'SRC_SORTING'
                && record.status !== 'RET_DEST_SORTING';
        return  <Button variant="contained" color="primary" disabled={is_state_incorrect} onClick={this.handleClick}>
            Left
        </Button>

    }
}

SortingLeftButton.propTypes = {
    push: PropTypes.func,
    record: PropTypes.object,
    showNotification: PropTypes.func,
};

class CourierDeliveredButton extends Component {
    handleClick = () => {
        const { record } = this.props;
        fetch(buildURLFor(
                properties.tracking.endpoint.courier.delivered,
                properties.tracking.endpoint.host,
                properties.tracking.endpoint.protocol,
                properties.tracking.endpoint.port) + `${record.id}`,
            { method: 'PATCH', headers: {'Authorization': 'Bearer ' + localStorage.getItem('token') } })
            .then(() => {
            })
            .catch((e) => {
                console.error(e);
            });
    };

    render() {
        const { record } = this.props;
        const is_state_incorrect = record.status !== 'IN_DELIVERY'
                && record.status !== 'RET_IN_DELIVERY';
        return  <Button variant="contained" color="secondary" disabled={is_state_incorrect} onClick={this.handleClick}>
            Delivered
        </Button>

    }
}

CourierDeliveredButton.propTypes = {
    push: PropTypes.func,
    record: PropTypes.object,
    showNotification: PropTypes.func,
};

class CourierReturnedButton extends Component {
    handleClick = () => {
        const { record } = this.props;
        fetch(buildURLFor(
            properties.tracking.endpoint.courier.returned,
            properties.tracking.endpoint.host,
            properties.tracking.endpoint.protocol,
            properties.tracking.endpoint.port) + `${record.id}`,
            { method: 'PATCH', headers: {'Authorization': 'Bearer ' + localStorage.getItem('token') } })
            .then(() => {
            })
            .catch((e) => {
                console.error(e);
            });
    };

    render() {
        const { record } = this.props;
        const is_state_incorrect = record.status !== 'IN_DELIVERY'
            && record.status !== 'RET_IN_DELIVERY';
        return  <Button variant="contained" color="secondary" disabled={is_state_incorrect} onClick={this.handleClick}>
            Returned
            </Button>

    }
}

CourierReturnedButton.propTypes = {
    push: PropTypes.func,
    record: PropTypes.object,
    showNotification: PropTypes.func,
};

export { CourierDeliveredButton, CourierReceivedButton, CourierReturnedButton, SortingReceivedButton, SortingLeftButton };
