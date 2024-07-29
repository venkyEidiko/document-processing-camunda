import React, { useCallback, useEffect, useState } from 'react';
import Base from './Base';
import './usertask.css';
import MUIDataTable from 'mui-datatables';
import axios from 'axios';
import VisibilityIcon from '@mui/icons-material/Visibility';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
const Task = () => {
    const [taskData, setTaskData] = useState([]);
    const [tasktype, setTaskType] = useState('unasign');
    const [claimUnClaim, setClaimUnClaim] = useState('Claim');
    const url = `http://localhost:8085/`

    const navigate = useNavigate();

    const fetchData = useCallback(async () => {
        try {
            const response = await axios.get(tasktype === 'unasign' ? `${url}getUnassignTask` : `${url}getAssignTask`);
            setTaskData(response.data);
            console.log('response : ', response.data);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    }, [tasktype, url]);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    const columns = [
        { name: "businessKey", label: "Business Key", },
        { name: "fileName", label: "File Name", },
        { name: "fileSize", label: "File Size", },
        { name: "fileExtension", label: "File Extension", },
        {
            name: "Actions",
            options: {
                customBodyRender: (value, tableMeta) => (
                    <button className='table-btn' onClick={() => handleButtonClick(tableMeta.rowData)}>
                        {claimUnClaim}
                    </button>
                ),
            },
        },
    ];
    if (tasktype === 'asign') {
        columns.splice(1, 0, {
            name: 'view',
            options: {
                customBodyRender: (value, tableMeta) => (
                    <span className='eye-button' onClick={() => { handleEyeButtonClick(tableMeta.rowData) }}><VisibilityIcon /></span>
                )
            }
        })
    }

    const options = {
        filterType: 'checkbox',
        selectableRows: 'none',
        textLabels: {
            body: {
                noMatch: "No tasks available",
            },
        },
    };

    const handleButtonClick = (rowData) => {
        console.log("Button clicked for row:", rowData);
        taskData.forEach(data => {
            console.log("if block in handleButtonClick : ", rowData[0], data.bussinessKey);
            if (rowData[0] === data.businessKey) {
                let taskId = data.taskId;
                console.log("taskId : ", taskId);
                if (claimUnClaim === 'Claim') {
                    fetch(`${url}claimTask?taskId=${taskId}`).then(res => {
                        console.log("Claim response : ", res);
                        fetchData()
                        toast.success('Task claimed sucessfully !')
                    }).catch(error => {
                        console.log("Claim failed error", error)
                        toast.error('Failed task claim !')
                    });
                } else if (claimUnClaim === 'UnClaim') {
                    fetch(`${url}unClaimTask?taskId=${taskId}`).then(res => {
                        console.log("UnClaim response : ", res);
                        fetchData()
                        toast.success('Task claimed sucessfully !')
                    }).catch(error => {
                        console.log("UnClaim failed error", error)
                        toast.error('Failed task unclaim !')
                    })
                }
            };
        });
    };

    const handleAsignUnAsign = (event) => {
        setTaskType(event.tasktype);
        setClaimUnClaim(event.claimUnClaim);
    };

    const handleEyeButtonClick = (rowData) => {

        console.log("Eye Profile data", rowData)
        taskData.forEach(data => {
            console.log("if block in handleEyeButtonClick : ", rowData[0], data.bussinessKey);
            if (rowData[0] === data.businessKey) {
                console.log("Eye Profile  full data", data)
                navigate('/profile', { state: { taskData: data } });

            }
        });
    }
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
                        data={taskData}
                        columns={columns}
                        options={options}
                    />
                </div>
            </div>
        </Base>
    );
};

export default Task;