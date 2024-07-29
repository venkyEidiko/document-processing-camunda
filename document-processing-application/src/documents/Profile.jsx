import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Base from './Base';
import './profile.css';
import ProfileImage from './profileImg.webp';
import { useLocation, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const Profile = () => {
    const [userData, setUserData] = useState(null);
    const navigate = useNavigate();
    const url = `http://localhost:8085/`;
    const location = useLocation();
    const { taskData } = location.state || {}; 
    useEffect(() => {
        const fetchData = async () => {
            const adhaar = 828487733013;
            try {
                await axios.get(`${url}getByAadherNumber/${adhaar}`).then(response => {
                    let user = response.data;
                    setUserData(user[0]);
                    console.log('response : ', response.data);
                });
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, [url]);

    const completeTask = async() => {
            try {
                console.log("Task Id in profile : ",taskData);
                await axios.get(`${url}completeTask?taskId=${taskData.taskId}`).then(response => {
                    console.log("call to complete task : ",response)
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
                        {userData && (
                            <div>
                                <h2>{userData.name}</h2>
                                <p>Email: johndoe@example.com</p>
                                <p>Aadhaar Number: {userData.aadhaarNumber}</p>
                                <p>D.O.B: {userData.dateOfBirth}</p>
                                <p>Gender: {userData.gender}</p>
                                <p>Address: {userData.address}</p>
                            </div>

                        )}

                    </div>
                    <div className='select-btn'>
                        <button onClick={() => completeTask()}>Select</button>
                    </div>
                </div>
                <div className='image-file'>
                    <img src={ProfileImage} alt='file not found' />
                </div>
            </div>
        </Base>
    );
}

export default Profile;