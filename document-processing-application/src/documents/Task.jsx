import React, { useCallback, useEffect, useState } from 'react';
import Base from './Base';
import './usertask.css';
import MUIDataTable from 'mui-datatables';
import axios from 'axios';
import VisibilityIcon from '@mui/icons-material/Visibility';
import {useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
const Task = () => {
    
    const [taskData, setTaskData] = useState([]);
    
    const [tasktype, setTaskType] = useState('unasign');
    

    const [claimUnClaim, setClaimUnClaim] = useState('Claim');
    const [title,setTitle]=useState("Unasign Task List")
    const url = process.env.REACT_APP_API_URL;

    const navigate = useNavigate();

    const fetchData = useCallback(async () => {
       
        try {  
            const response = await axios.get(tasktype === 'unasign' ? 
                `${url+process.env.REACT_APP_GET_UNASIGN_TASK_ENDPOINT}` :
                 `${url+process.env.REACT_APP_GET_ASIGN_TASK_ENDPOINT}`);
            setTaskData(response.data);
           
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
    
        taskData.forEach(data => {
                     if (rowData[0] === data.businessKey) {
                let taskId = data.taskId;  
                if (claimUnClaim === 'Claim') {
                    fetch(`${url}claimTask?taskId=${taskId}`).then(res => {  
                        fetchData()
                        toast.success('Task claimed sucessfully !')
                    }).catch(error => {          
                        toast.error('Failed task claim !')
                    });
                } else if (claimUnClaim === 'UnClaim') {
                    fetch(`${url}unClaimTask?taskId=${taskId}`).then(res => {
                     
                        fetchData()
                        toast.success('Task claimed sucessfully !')
                    }).catch(error => {  
                        toast.error('Failed task unclaim !')
                    })
                }
            };
        });
    };

    const handleAsignUnAsign = (event) => {
        setTaskType(event.tasktype);
        setClaimUnClaim(event.claimUnClaim);
        setTitle(event.title);
        
     
       
    };

    const handleEyeButtonClick = (rowData) => {
        taskData.forEach(data => {
            if (rowData[0] === data.businessKey) {
                navigate('/profile', { state: { taskData: data } });

            }
        });
    }

  
    return (
        <Base>
            <div className='userTask-container'>
                <div className='userTask-button'>
                    <div className='unasign-btn'>
                        <button className={tasktype ==='unasign' ? 'active' : ''} onClick={() => handleAsignUnAsign({ tasktype: 'unasign', claimUnClaim: 'Claim',title:'Unasign Task List' })}>UnAssigned</button>
                    </div>
                    <div className='asign-btn'>
                        <button className={tasktype ==='asign'  ? 'active' : '' } onClick={() => handleAsignUnAsign({ tasktype: 'asign', claimUnClaim: 'UnClaim',title:'Asigned Task List'  })}>Assigned</button>
                    </div>
                </div>
                <div className='table-data'>
                    <MUIDataTable
                        title={title}
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