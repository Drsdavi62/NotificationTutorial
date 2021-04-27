"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    Object.defineProperty(o, k2, { enumerable: true, get: function() { return m[k]; } });
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const connection_1 = __importDefault(require("../database/connection"));
const admin = __importStar(require("firebase-admin"));
var serviceAccount = require("../../path/serviceAccountKey.json");
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});
class NotificationController {
    registerToken(req, res) {
        return __awaiter(this, void 0, void 0, function* () {
            const { token } = req.body;
            console.log("Token: " + token);
            const trx = yield connection_1.default.transaction();
            const existentTokens = yield trx("tokens").select("token").where('id', 1);
            if (existentTokens.length < 1) {
                const tokenObj = {
                    token: token
                };
                yield trx("tokens").insert(tokenObj);
            }
            else {
                yield trx("tokens").where('id', 1).update({ token: token });
            }
            yield trx.commit();
            return res.json({
                token: token
            }).status(200);
        });
    }
    sendNotification(req, res) {
        return __awaiter(this, void 0, void 0, function* () {
            const { token } = yield connection_1.default('tokens').select('token').first();
            req.body["token"] = token;
            console.log(token);
            try {
                admin.messaging().send(req.body)
                    .then((response) => {
                    // Response is a message ID string.
                    console.log('Successfully sent message to token:', token);
                    return res.send('Successfully sent message');
                })
                    .catch((error) => {
                    console.log('Error sending message:', error);
                    return res.send('Error sending message:').status(400);
                });
            }
            catch (_a) {
                return res.send('Invalid Message').status(400);
            }
        });
    }
}
exports.default = NotificationController;
