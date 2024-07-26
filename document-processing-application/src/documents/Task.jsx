import React, { useEffect, useState } from 'react';
import Base from './Base';
import './usertask.css';
import MUIDataTable from 'mui-datatables';
import axios from 'axios';

const Task = () => {
    const [taskData, setTaskData] = useState(null);
    const [tasktype, setTaskType] = useState('unasign');
    const [claimUnClaim, setClaimUnClaim] = useState('Claim');

    useEffect(() => {
        const fetchData = async () => {
            const url = tasktype === 'unasign' ? 'http://10.0.0.96:8085/getUnassignTask' : 'http://10.0.0.96:8085/getAssignTask';
            
            try {
                const response = await axios.get(url);
                setTaskData(response.data);
                console.log('response : ', response.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, [tasktype]);

    const columns = [
        "business Key",
        "file Name",
        "State",
        "file Extension",
        {
            name: "Actions",
            options: {
                customBodyRender: (value, tableMeta, updateValue) => (
                    <button className='table-btn' onClick={() => handleButtonClick(tableMeta.rowData)}>
                        {claimUnClaim}
                    </button>
                ),
            },
        },
    ];

    const options = {
        filterType: 'checkbox',
        selectableRows: 'none',
        customBodyRender: (value, tableMeta, updateValue) => {
            if (taskData && taskData.length === 0) {
                return (
                    <div>No tasks available</div>
                );
            }
            return null;
        }
    };

    const handleButtonClick = (rowData) => {
        console.log("Button clicked for row:", rowData);
    };

    const handleAsignUnAsign = (event) => {
        setTaskType(event.tasktype);
        setClaimUnClaim(event.claimUnClaim);
    };

    // Prepare data for MUIDataTable
    const preparedData = taskData && taskData.length > 0 ? taskData : [{}, {}, {}, {}]; // Placeholder empty rows

    return (
        <Base>
            <div className='userTask-container'>
                <div className='userTask-button'>
                    <div className='unasign-btn'>
                        <button onClick={() => handleAsignUnAsign({ tasktype: 'unasign', claimUnClaim: 'Claim' })}>UnAssign</button>
                    </div>
                    <div className='asign-btn'>
                        <button onClick={() => handleAsignUnAsign({ tasktype: 'asign', claimUnClaim: 'UnClaim' })}>Assign</button>
                    </div>
                </div>
                <div className='table-data'>
                    <MUIDataTable
                        title={"Task List"}
                        data={preparedData}
                        columns={columns}
                        options={options}
                    />
                </div>
            </div>
        </Base>
    );
};

export default Task;
