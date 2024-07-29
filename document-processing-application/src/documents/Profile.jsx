import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Base from './Base';
import './profile.css';
import ProfileImage from './profileImg.webp';
import { useLocation, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const Profile = () => {
    const [userData, setUserData] = useState(null);
    const [base64String, setBase64String] = useState(null);
    const navigate = useNavigate();
    const url = `http://localhost:8085/`;

    const location = useLocation();
    const { taskData } = location.state || {};
    var businessKey = 'DOC3885'
    if (taskData) {
        businessKey = taskData.businessKey
    }

    useEffect(() => {
        const fetchData = async () => {
            try {
                await axios.get(`${url}getByBusinesskey/${businessKey}`).then(response => {
                    let user = response.data;
                    setUserData(user);

                    console.log('response : ', response.data);
                    if (user.aadhaarImage) {
                        setBase64String(user.aadhaarImage);
                    } else {
                        // Handle case where aadhaarImage is null or undefined
                        setBase64String(null); // Set base64String to null if image data is missing
                    }
                    console.log('response : ', response.data.aadhaarImage);
                });
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, [url, businessKey]);

    const completeTask = async () => {
        try {
            console.log("Task Id in profile : ", taskData);
            await axios.get(`${url}completeTask?taskId=${taskData.taskId}`).then(response => {
                console.log("call to complete task : ", response)
                toast.success("Task completed sucessfully !")
                navigate('/task')
                console.log('response : ', response.data);
            });
        } catch (error) {
            console.error('Error fetching data:', error);
            toast.error("Failed to complete the task !")
        }
    };
const backToTask=()=>{
    navigate('/task');
}
    return (
        <Base>
            <div className="profile-container">
                <div className='profile'>
                    <div className="profile-header">
                        <img
                            className="profile-image"
                            src={ProfileImage}
                            alt="Profile"
                        />
                    </div>
                    <div className="profile-details">
                        {userData ? (
                            <div>
                                <h2>{userData.name ?? '-Not Set-'}</h2>

                                <p>Aadhaar Number: {userData.aadhaarNumber ?? '-Not Set-'}</p>
                                <p>D.O.B: {userData.dateOfBirth ?? 'Not set'}</p>
                                <p>Gender: {userData.gender ?? '-Not Set-'}</p>
                                <p>Address: {userData.address ?? '-Not Set-'}</p>
                            </div>
                        ) : (
                            <div>
                                <h2>-Not Set-</h2>

                                <p>Aadhaar Number: -Not Set-</p>
                                <p>D.O.B: -Not Set-</p>
                                <p>Gender: -Not Set-</p>
                                <p>Address: -Not Set-</p>
                            </div>
                        )}
                    </div>
                    <div className='profile-btn'>
                        <div className='select-btn'>
                            <button onClick={() => completeTask()}>Completed</button>
                        </div>
                        <div className='back-btn'>
                            <button onClick={() => backToTask()}>Back</button>
                        </div>
                    </div>
                </div>
                <div className='image-file'>
                    {base64String ? (
                        <img src={`data:image/png;base64,${base64String}`} alt='User Aadhaar' width="500px" height="600px" />
                    ) : (
                        <img src={ProfileImage} alt='Default Profile' />
                    )}
                </div>
            </div>
        </Base>
    );
}

export default Profile;