import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Base from './Base';
import './profile.css';
import ProfileImage from './profileImg.webp';
import { useLocation, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const Profile = () => {
    const [userData, setUserData] = useState(null);
    const [base64String, setBase64String] = useState('');
    const navigate = useNavigate();
    const url = `http://10.0.0.96:8085/`;

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
                    setUserData(user[0]);
                    setBase64String(user.aadhaarImage)
                    console.log('response : ', response.data);
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
                                <p>Email: {userData.email ?? '-Not Set-'}</p>
                                <p>Aadhaar Number: {userData.aadhaarNumber ?? '-Not Set-'}</p>
                                <p>D.O.B: {userData.dateOfBirth ?? 'Not set'}</p>
                                <p>Gender: {userData.gender ?? '-Not Set-'}</p>
                                <p>Address: {userData.address ?? '-Not Set-'}</p>
                            </div>
                        ) : (
                            <div>
                                <h2>-Not Set-</h2>
                                <p>Email: -Not Set-</p>
                                <p>Aadhaar Number: -Not Set-</p>
                                <p>D.O.B: -Not Set-</p>
                                <p>Gender: -Not Set-</p>
                                <p>Address: -Not Set-</p>
                            </div>
                        )}
                    </div>
                    <div className='select-btn'>
                        <button onClick={() => completeTask()}>Select</button>
                    </div>
                </div>
                <div className='image-file'>
                    {base64String ? (
                        <img src={base64String} alt='File not found' />
                    ) : (
                        <img src={ProfileImage} alt='File not found' />
                    )}
                </div>
            </div>
        </Base>
    );
}

export default Profile;