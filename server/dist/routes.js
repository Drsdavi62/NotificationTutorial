"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const NotificationController_1 = __importDefault(require("./controllers/NotificationController"));
const routes = express_1.default.Router();
const notificationController = new NotificationController_1.default();
routes.post('/token', notificationController.registerToken);
routes.post('/notification', notificationController.sendNotification);
exports.default = routes;
