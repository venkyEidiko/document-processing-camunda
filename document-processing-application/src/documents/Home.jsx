import React from 'react'
import { useState, useHistory, useNavigate } from 'react';
import axios from 'axios';
import Base from './Base';
const Home = () => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadStatus, setUploadStatus] = useState('');
  // const history = useHistory();
  // navigate = useNavigate();
  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    try {
      if (!selectedFile) {
        alert('Please select a file.');
        return;
      }

      const formData = new FormData();
      formData.append('file', selectedFile);
      console.log("file entre into handle upload method : ", selectedFile);
      const response = await axios.post('http://10.0.0.42:8085/uploadDocument', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });

      setUploadStatus(response.data.message); // Assuming server returns a message
    } catch (error) {
      console.error('Error uploading file: ', error);

      setUploadStatus('Failed to upload file.');
      //history.push('/success');
      // navigate('/success')
    }
  };

  return (
    <>
      <Base>
        <div className='home'>
          <h2>File Upload</h2>
          <input type="file" onChange={handleFileChange} />
          <button onClick={handleUpload}>Upload</button>
          <p>{uploadStatus}</p>
        </div>
      </Base>
    </>
  );
};

export default Home