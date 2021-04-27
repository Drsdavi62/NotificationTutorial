import { Request, Response } from "express";
import knex from "../database/connection";
import * as admin from 'firebase-admin'

var serviceAccount = require("../../path/serviceAccountKey.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

class NotificationController {
    async registerToken(req : Request, res : Response) {
        const { token } = req.body
        console.log("Token: " + token)
    
        const trx = await knex.transaction()
    
        const existentTokens = await trx("tokens").select("token").where('id', 1);
    
        if (existentTokens.length < 1) {
            const tokenObj = {
                token: token
            }
            await trx("tokens").insert(tokenObj)
        } else {
            await trx("tokens").where('id', 1).update({token : token})
        }
            
        await trx.commit();

        return res.json({
            token: token
        }).status(200)
    }

    async sendNotification(req : Request, res : Response) {
        const {token} = await knex('tokens').select('token').first()

        req.body["token"] = token

        console.log(token)
    
        try {
            admin.messaging().send(req.body)
            .then((response) => {
                // Response is a message ID string.
                console.log('Successfully sent message to token:', token);
                return res.send('Successfully sent message')
            })
            .catch((error) => {
                console.log('Error sending message:', error);
                return res.send('Error sending message:').status(400)
            });
        } catch {
            return res.send('Invalid Message').status(400)
        }
    }
}

export default NotificationController