import React from 'react'
import Navbar from './Navbar'
import Footer from './Footer'

const Base = ({ Title = "Welcome to Document Proccess Application", children }) => {
    return (
        <>
            <div>
                <Navbar />
            </div>
            <div className='base-children'>
                {children}
            </div>
            <div>
                <Footer />
            </div>
        </>
    )
}

export default Base