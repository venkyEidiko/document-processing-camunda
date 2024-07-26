import React, { useEffect, useState } from 'react'
import axios from 'axios'
import Base from './Base'
import './profile.css'
import ProfileImage from './profileImg.webp';

const Profile = () => {
    const [userData, setUserData] = useState(null);
    
    useEffect(() => {
        const fetchData = async () => {
            const adhaar = 828487733013;
            try {
                 await axios.get(`http://10.0.0.96:8085/getByAadherNumber/${adhaar}`).then(response=>{
                    let user=response.data;
                    setUserData(user[0]);
                    console.log('response : ', response.data)
                 }); 
              } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, [0]); // Dependency array ensures useEffect runs only when adhaar changes

    return (
        <>
            <Base>
                <div className="profile-container">
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
                                <p>Email : johndoe@example.com</p>
                                <p>AadherNumber : {userData.aadhaarNumber}</p>
                                <p>D.O.B : {userData.dateOfBirth}</p>
                                <p>Gender : {userData.gender}</p>
                                <p>Address : {userData.address}</p>
                            </div>
                        )}

                    </div>
                </div>
            </Base>
        </>
    )
}

export default Profile