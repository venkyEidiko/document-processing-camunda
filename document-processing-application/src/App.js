
import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from './documents/Home';
import Success from './documents/Success';
import Profile from './documents/Profile';
import Task from './documents/Task';
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from 'react-toastify';

function App() {
  return (
    <>
    <BrowserRouter>
    <ToastContainer/>
    <Routes>
    <Route path='/' element={<Home/>}/>
    <Route path='/success' element={<Success/>}/>
    <Route path='/profile' element={<Profile/>}/>
    <Route path='/task' element={<Task/>}/>
    </Routes>
    </BrowserRouter>
    </>
  );
}

export default App;
