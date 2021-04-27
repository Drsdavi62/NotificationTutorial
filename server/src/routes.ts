import express from 'express'
import NotificationController from './controllers/NotificationController'

const routes = express.Router()

const notificationController = new NotificationController()

routes.post('/token', notificationController.registerToken)
routes.post('/notification', notificationController.sendNotification)

export default routes