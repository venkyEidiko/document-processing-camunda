
import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from './documents/Home';
import Success from './documents/Success';
import Profile from './documents/Profile';


function App() {
  return (
    <>
    <BrowserRouter>
    <Routes>
    <Route path='/' element={<Home/>}/>
    <Route path='/success' element={<Success/>}/>
    <Route path='/profile' element={<Profile/>}/>
    </Routes>
    </BrowserRouter>
    </>
  );
}

export default App;
