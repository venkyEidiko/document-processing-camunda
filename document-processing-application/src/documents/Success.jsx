import React from 'react'
import Base from './Base'
import { Card, CardBody } from 'react-bootstrap'

const Success = () => {
  return (
    <>
      <Base>
        <Card>
          <CardBody>
            <div>File Uploaded Successfully...</div>
          </CardBody>
        </Card>
      </Base>
    </>
  )
}

export default Success