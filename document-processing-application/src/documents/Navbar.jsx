import React from 'react'
import { Link, NavLink } from 'react-router-dom'

const Navbar = () => {
  return (
    <>
      <div className='navbar'>



        <div>
          <NavLink>
            <Link to='/'>Home</Link>
          </NavLink>
        </div>
        <div>
          <NavLink>
            <Link to='/profile'>Profile</Link>
          </NavLink>

        </div>
        <div>
          <NavLink>
            <Link to='/Success'>Success</Link>
          </NavLink>

        </div>
        <div>
          <NavLink>
            <Link to='/profile'>Profile</Link>
          </NavLink>

        </div>


      </div>
    </>
  )
}

export default Navbar