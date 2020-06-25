import React from 'react'
import {
    CardActions,
    Create,
    CreateButton,
    Datagrid,
    DeleteButton,
    List,
    SelectInput,
    ShowController,
    ShowView,
    SimpleForm,
    SimpleShowLayout,
    TextField,
    TextInput
} from 'react-admin';

const PostActions = ({ basePath }) => (
    <CardActions>
        <CreateButton basePath={basePath} />
    </CardActions>
);

export const EmployeeList = ({ permissions, ...props }) => (
    <List {...props} title="Employees View" actions={<PostActions />}>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="fullName.firstName" label="First Name" />
            <TextField source="fullName.lastName" label="Last Name" />
            <TextField source="position" label="Position" />
        </Datagrid>
    </List>
);

export const EmployeeShow = ({ permissions, ...props }) => (
    <ShowController {...props}>
        {controllerProps =>
            <ShowView {...props} {...controllerProps}>
                <SimpleShowLayout>
                    <TextField source="id" />
                    <TextField source="fullName.firstName" label="First Name" />
                    <TextField source="fullName.lastName" label="Last Name" />
                    <TextField source="position" label="Position" />
                    <TextField source="mobile.number" label="Mobile" />
                    <DeleteButton/>
                </SimpleShowLayout>
            </ShowView>
        }
    </ShowController>
);

const required = (message = 'Required') =>
    value => value ? undefined : message;

export const EmployeeCreate = (props) => (
    <Create {...props}>
        <SimpleForm>
            <TextInput source="fullName.firstName" label="First Name" validate={required()}/>
            <TextInput source="fullName.lastName" label="Last Name" validate={required()}/>
            <SelectInput source="position" validate={required()} choices={[
                { id: 'ADMINISTRATOR', name: 'Admin' },
                { id: 'COURIER', name: 'Courier' },
                { id: 'SORTING', name: 'Sorting station employee' },
            ]} />
            <TextInput source="mobile.number" label="Mobile Number"/>
        </SimpleForm>
    </Create>
);