import express, { Application, Request, Response, NextFunction, json } from 'express'
import cors from 'cors'
import routes from './routes'

const app: Application = express()

app.use(cors())
app.use(express.json())
app.use(routes)

app.listen(process.env.PORT || 5000, () => console.log('Server running on port 5000'))
