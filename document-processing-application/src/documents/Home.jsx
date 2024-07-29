import React, { useState } from 'react';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFile } from '@fortawesome/free-solid-svg-icons'; // Import the faFile icon
import Base from './Base';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  const [selectedFile, setSelectedFile] = useState(null);
const navigate = useNavigate()
  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    try {
      if (!selectedFile) {
        toast.error("Please Select File");
        return;
      }
      const url=`http://10.0.0.96:8085/`;
      const formData = new FormData();
      formData.append('file', selectedFile);
      console.log("file enter into handle upload method: ", selectedFile);
      const response = await axios.post( `${url}uploadDocument`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      console.log("upload file response : ",response)
      toast.success(response.data);
      navigate('/task')
    } catch (error) {
      console.error('Error uploading file: ', error);
      toast.error('Failed to upload file.');
    }
  };

  const fileDetails = selectedFile ? (
    <div>
      <p><FontAwesomeIcon icon={faFile} /> {selectedFile.name}</p>
    </div>
  ) : (
    <p>No file chosen</p>
  );

  return (
    <>
      <Base>
        <div className='home'>
        <h2>Document Proccessing </h2>
          <h3>Upload File</h3>
          <div className='file-input-wrapper'>
            <input 
              type="file" 
              id="file-upload" 
              className="file-input" 
              onChange={handleFileChange}
            />
            <label 
              htmlFor="file-upload" 
              className="file-input-label"
            >
              Choose a file
            </label>
          </div>
          <div className='file-name'>
            {fileDetails}
          </div>
          <div className='home-button'>
            <button onClick={handleUpload}>Upload</button>
          </div>
        </div>
      </Base>
    </>
  );
};

export default Home;
