import React from 'react'
import {
    Create,
    Datagrid,
    Filter,
    List,
    NumberField,
    NumberInput,
    SearchInput,
    ShowController,
    ShowView,
    SimpleForm,
    SimpleShowLayout,
    TextField,
    TextInput
} from 'react-admin';
import { makeStyles, Chip } from '@material-ui/core';
import {CourierDeliveredButton, CourierReceivedButton, CourierReturnedButton, SortingLeftButton, SortingReceivedButton} from './parcelStates';

const useQuickFilterStyles = makeStyles(theme => ({
    chip: {
        marginBottom: theme.spacing(1),
    },
}));
const QuickFilter = ({ label }) => {
    const classes = useQuickFilterStyles();
    return <Chip className={classes.chip} label="Show only available parcels"  />;
};

export const ParcelFilter = (props) => (
    <Filter {...props}>
        <SearchInput source="q" alwaysOn />
        <QuickFilter source="available" label="Show only available parcels" defaultValue={true} />
    </Filter>
);

export const ParcelList = ({ permissions, ...props }) => (
    <List {...props} filters={<ParcelFilter />} filterDefaultValues={{ available: true }} bulkActionButtons={false} title="Parcels Monitoring View">
        <Datagrid rowClick="show">
            <TextField source="id" />
            { (permissions === 'ADMINISTRATOR' || permissions === 'COURIER') && <TextField source="receiver.address.street" label="Destination Street" /> }
            <TextField source="receiver.address.city" label="Destination City" />
            <TextField source="receiver.address.postalCode" label="Destination Postal Code" />
            <TextField source="status" />
            <TextField source="modified_by" label="Last modified by" />
            { (permissions === 'ADMINISTRATOR' || permissions === 'COURIER') && <CourierReceivedButton /> }
            { (permissions === 'ADMINISTRATOR' || permissions === 'SORTING') && <SortingReceivedButton /> }
            { (permissions === 'ADMINISTRATOR' || permissions === 'SORTING') && <SortingLeftButton /> }
            { (permissions === 'ADMINISTRATOR' || permissions === 'COURIER') && <CourierDeliveredButton /> }
            { (permissions === 'ADMINISTRATOR' || permissions === 'COURIER') && <CourierReturnedButton /> }
        </Datagrid>
    </List>
);

export const ParcelShow = ({ permissions, ...props }) => (
    <ShowController {...props}>
        {controllerProps =>
            <ShowView {...props} {...controllerProps}>
                <SimpleShowLayout>
                    {console.log(controllerProps.record) }
                    <TextField source="id" label="ID" />
                    { (permissions === 'ADMINISTRATOR'
                        || (permissions === 'COURIER' && controllerProps.record && controllerProps.record.status === 'CREATED'))
                        && <TextField source="sender.name.firstName" label="Sender First Name" /> }
                    { (permissions === 'ADMINISTRATOR'
                        || (permissions === 'COURIER' && controllerProps.record && controllerProps.record.status === 'CREATED'))
                        && <TextField source="sender.name.lastName" label="Sender Last Name" /> }
                    { (permissions === 'ADMINISTRATOR'
                        || (permissions === 'COURIER' && controllerProps.record && controllerProps.record.status === 'CREATED'))
                        && <TextField source="sender.address.street" label="Source Street" /> }
                    { (permissions === 'ADMINISTRATOR'
                        || (permissions === 'COURIER' && controllerProps.record && controllerProps.record.status === 'CREATED'))
                        && <TextField source="sender.address.city" label="Source City" /> }
                    { (permissions === 'ADMINISTRATOR'
                        || (permissions === 'COURIER' && controllerProps.record && controllerProps.record.status === 'CREATED'))
                        && <TextField source="sender.address.postalCode" label="Source Postal Code" /> }
                    { (permissions === 'ADMINISTRATOR'
                        || (permissions === 'COURIER' && controllerProps.record && controllerProps.record.status === 'CREATED'))
                        && <TextField source="sender.address.country" label="Source Country" /> }
                    { (permissions === 'ADMINISTRATOR' || permissions === 'COURIER') && <TextField source="receiver.name.firstName" label="Receiver First Name" /> }
                    { (permissions === 'ADMINISTRATOR' || permissions === 'COURIER') && <TextField source="receiver.name.lastName" label="Receiver Last Name" /> }
                    <TextField source="receiver.address.street" label="Destination Street" />
                    <TextField source="receiver.address.city" label="Destination City" />
                    <TextField source="receiver.address.postalCode" label="Destination Postal Code" />
                    <TextField source="receiver.address.country" label="Destination Country" />
                    <TextField source="status" />
                    <TextField source="modified_by" label="Last modified by" />
                    <NumberField source="weight" label="Weight (kg)" />
                    { (permissions === 'ADMINISTRATOR' || permissions === 'COURIER') && <CourierReceivedButton /> }
                    { (permissions === 'ADMINISTRATOR' || permissions === 'SORTING') && <SortingReceivedButton /> }
                    { (permissions === 'ADMINISTRATOR' || permissions === 'SORTING') && <SortingLeftButton /> }
                    { (permissions === 'ADMINISTRATOR' || permissions === 'COURIER') && <CourierDeliveredButton /> }
                    { (permissions === 'ADMINISTRATOR' || permissions === 'COURIER') && <CourierReturnedButton /> }
                </SimpleShowLayout>
            </ShowView>
        }
    </ShowController>
);

const required = (message = 'Required') =>
    value => value ? undefined : message;

export const ParcelCreate = (props) => (
    <Create {...props}>
        <SimpleForm>
            <TextInput disabled source="id" label="ID (automatically generated)" />
            <TextInput source="sender.name.firstName" label="Sender First Name" validate={required()} />
            <TextInput source="sender.name.lastName" label="Sender Last Name" validate={required()} />
            <TextInput source="sender.phone.number" label="Sender Phone Number" validate={required()} />
            <TextInput source="sender.address.street" label="Sender Street" validate={required()} />
            <TextInput source="sender.address.city" label="Sender City" validate={required()} />
            <TextInput source="sender.address.postalCode" label="Sender Postal Code" validate={required()} />
            <TextInput source="sender.address.country" label="Sender Country" validate={required()} />
            <TextInput source="receiver.name.firstName" label="Receiver First Name" validate={required()} />
            <TextInput source="receiver.name.lastName" label="Receiver Last Name" validate={required()} />
            <TextInput source="receiver.phone.number" label="Receiver Phone Number" validate={required()} />
            <TextInput source="receiver.address.street" label="Receiver Street" validate={required()} />
            <TextInput source="receiver.address.city" label="Receiver City" validate={required()} />
            <TextInput source="receiver.address.postalCode" label="Receiver Postal Code" validate={required()} />
            <TextInput source="receiver.address.country" label="Receiver Country" validate={required()} />
            <NumberInput source="weight" label="Weight (kg)" />
        </SimpleForm>
    </Create>
);